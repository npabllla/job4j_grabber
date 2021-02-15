package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    private static Connection connection;
    private static Properties properties = new Properties();

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            try (InputStream in = new FileInputStream("./src/main/resources/rabbit.properties")) {
                properties.load(in);
                Class.forName(properties.getProperty("driver-class-name"));
                String url = properties.getProperty("url");
                String login = properties.getProperty("login");
                String password = properties.getProperty("password");
                connection = DriverManager.getConnection(url, login, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into rabbit(create_date) values (?)",
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, 1);
                statement.execute();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println(generatedKeys.getInt(1));
                        System.out.println("Table entry completed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}