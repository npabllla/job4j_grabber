package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        int n = 1;
        while (n != 6) {
            Document doc = Jsoup.connect(String.format("https://www.sql.ru/forum/job-offers/%d", n)).get();
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
                    String date = temp.text();
                    System.out.println(parser(date));
                }
            }
            n++;
        }
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
}