package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParse implements Parse {
    @Override
    public List<Post> list(String link) {
        List<Post> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String url = href.attr("href");
                String description = getDescription(href);
                result.add(new Post(url, description));
            }
            Elements rw = doc.select(".altCol");
            List<String> dates = new ArrayList<>();
            for (int i = 1; i < rw.size(); i++) {
                if (i % 2 == 1) {
                    Element temp = rw.get(i);
                    temp.attr("class");
                    String parsedDate = temp.text();
                    String date = parser(parsedDate);
                    dates.add(date);
                }
            }
            int k = 0;
            for (Post rsl : result) {
                rsl.setDate(dates.get(k));
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
        result.setLink(link);
        try {
            Document document = Jsoup.parse(new URL(link), 3000);
            Element table = document.select("table[class=msgTable]").first();
            result.setDescription(table.select("td[class=msgBody]").get(1).text());
            String date = table.select("td[class=msgFooter]").get(0).text().split(" \\[")[0];
            result.setDate(parser(date));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String parser(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy HH:mm");
        if (date.split(",")[0].equals("сегодня")) {
            String parsedDateToday = LocalDateTime.now().format(formatter);
            return replaceTime(date, parsedDateToday);
        } else if (date.split(",")[0].equals("вчера")) {
            String parsedDateYesterday = LocalDateTime.now().minusDays(1).format(formatter);
            return replaceTime(date, parsedDateYesterday);
        } else {
            return transformToDate(date).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(formatter);
        }
    }

    private static String replaceTime(String date, String parsedDate) {
        String time = LocalTime.parse(date.split(", ")[1]).toString();
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(parsedDate);
        return matcher.replaceAll(time);
    }

    private static Date transformToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", new Locale("ru"));
        return dateFormat.parse(date);
    }

    private static String getDescription(Element href) throws IOException {
        String link = href.attr("href");
        Document tempDoc = Jsoup.parse(new URL(link), 3000);
        Element table = tempDoc.select("table[class=msgTable]").first();
        return table.select("td[class=msgBody]").get(1).text();
    }
}