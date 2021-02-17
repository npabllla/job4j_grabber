package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public String parser(String date) throws ParseException {
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

    public String getDescription(Element href) throws IOException {
        String link = href.attr("href");
        Document tempDoc = Jsoup.parse(new URL(link), 3000);
        Element table = tempDoc.select("table[class=msgTable]").first();
        return table.select("td[class=msgBody]").get(1).text();
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
