package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
        }
        Elements rw = doc.select(".altCol");
        for (int i = 1; i < rw.size(); i++) {
            if (i % 2 == 1) {
                Element temp = rw.get(i);
                temp.attr("class");
                System.out.println(temp.text());
            }
        }
    }
}