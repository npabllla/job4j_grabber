package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    @Override
    public List<Post> list(String link) {
        List<Post> result = new ArrayList<>();
        Parser parser = new Parser();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String url = href.attr("href");
                String text = parser.getDescription(href);
                result.add(new Post(url, text));
            }
            Elements rw = doc.select(".altCol");
            List<String> dates = new ArrayList<>();
            for (int i = 1; i < rw.size(); i++) {
                if (i % 2 == 1) {
                    Element temp = rw.get(i);
                    temp.attr("class");
                    String parsedDate = temp.text();
                    String date = parser.parser(parsedDate);
                    dates.add(date);
                }
            }
            int k = 0;
            for (Post rsl : result) {
                rsl.setCreated(dates.get(k));
                k++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post detail(String link) {
        Post result = new Post();
        Parser parser = new Parser();
        result.setLink(link);
        try {
            Document document = Jsoup.parse(new URL(link), 3000);
            Element table = document.select("table[class=msgTable]").first();
            result.setText(table.select("td[class=msgBody]").get(1).text());
            String date = table.select("td[class=msgFooter]").get(0).text().split(" \\[")[0];
            result.setCreated(parser.parser(date));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
