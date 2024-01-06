package com.zerobase.stockservice.scraper;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.dto.constants.Month;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class YahooFinanceScraper implements Scraper {
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; //60 * 60 * 24

    @Override
    public ScrapedResult scrap(CompanyDto company) {
        try {
            Document document = Jsoup.connect(String.format(STATISTICS_URL, company.getTicker(), START_TIME, System.currentTimeMillis() / 1000)).get();
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);

            Element tbody = tableEle.children().get(1);
            List<DividendDto> dividends = new ArrayList<>(tbody.children().size());
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0].toUpperCase());
                if (month < 0) {
                    //TODO: 예외처리
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];
                dividends.add(
                        DividendDto.builder()
                                .date(LocalDateTime.of(year, month, day, 0, 0))
                                .dividend(dividend)
                                .build()
                );
            }
            return ScrapedResult.of(company, dividends);
        } catch (IOException e) {
            //TODO: 예외처리
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CompanyDto> scrapCompanyByTicker(String ticker) {
        try {
            Document document = Jsoup.connect(String.format(SUMMARY_URL, ticker, ticker)).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split("\\(")[0].trim();
            return Optional.of(
                CompanyDto.builder()
                    .ticker(ticker)
                    .name(title)
                    .build()
            );
        } catch (IOException e) {
            //TODO: 예외처리
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
