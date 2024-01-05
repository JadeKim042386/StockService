package com.zerobase.stockservice.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Scraper {
    public void getDividend() {
        try {
            Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/NVDA/history?period1=1672851683&period2=1704387683&interval=capitalGain%7Cdiv%7Csplit&filter=div&frequency=1d&includeAdjustedClose=true");
            Document document = connect.get();
            Elements eles = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element ele = eles.get(0);
            Element tbody = ele.children().get(1);
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }
                String[] splits = txt.split(" ");
                String month = splits[0];
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
