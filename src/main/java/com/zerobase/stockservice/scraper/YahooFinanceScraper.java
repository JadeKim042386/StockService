package com.zerobase.stockservice.scraper;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.dto.constants.Month;
import com.zerobase.stockservice.exception.ErrorCode;
import com.zerobase.stockservice.exception.ScraperException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class YahooFinanceScraper implements Scraper {
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; //60 * 60 * 24

    @Override
    public ScrapedResult scrap(CompanyDto company) {
        String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, System.currentTimeMillis() / 1000);
        try {
            Document document = Jsoup.connect(url).get();
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);
            Elements children = tableEle.children();
            if (children.isEmpty()) {
                log.error("Not Found {} Table", company.getName());
                throw new ScraperException(ErrorCode.NOT_FOUND_TABLE);
            }
            Element tbody = children.get(1);
            List<DividendDto> dividends = new ArrayList<>(tbody.children().size());
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0].toUpperCase());
                if (month < 0) {
                    throw new ScraperException(ErrorCode.INVALID_ENUM);
                }
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];
                dividends.add(
                        DividendDto.of(
                                LocalDateTime.of(year, month, day, 0, 0),
                                dividend
                        )
                );
            }
            return ScrapedResult.of(company, dividends);
        } catch (IOException e) {
            log.error("Invalid URL {}", url);
            throw new ScraperException(ErrorCode.FAILED_GET_DOCUMENT, e);
        }
    }

    @Override
    public Optional<CompanyDto> scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);
        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split("\\(")[0].trim();
            return Optional.of(CompanyDto.of(ticker, title));
        } catch (IOException e) {
            log.error("{} => Invalid URL {}", ErrorCode.FAILED_GET_DOCUMENT, url);
        } catch (IndexOutOfBoundsException e){
            log.error("{}는 존재하지않는 기업입니다.", ticker);
        }
        return Optional.empty();
    }
}
