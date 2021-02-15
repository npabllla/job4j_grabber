package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/").get();
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
                    if (date.split(",")[0].equals("сегодня")) {
                        System.out.println(transformToDate(LocalDateTime.now().toString()));
                    } else if (date.split(",")[0].equals("вчера")) {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        System.out.println(cal.getTime());
                    } else {
                        System.out.println(transformToDate(date));
                    }
                }
            }
    }

    private static Date transformToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", new Locale("ru"));
        return dateFormat.parse(date);
    }
}