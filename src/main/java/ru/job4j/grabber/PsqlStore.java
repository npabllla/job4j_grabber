package ru.job4j.grabber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection connection;

    public PsqlStore(Properties cfg) throws SQLException {
        try (InputStream in = new FileInputStream("./src/main/resources/psql.properties")) {
            cfg.load(in);
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        connection = DriverManager.getConnection(cfg.getProperty("url"),
                cfg.getProperty("login"),
                cfg.getProperty("password"));
    }


    @Override
    public void save(Post post) {
        try (PreparedStatement statement = this.connection
                .prepareStatement("insert into post (id, name, text, link, created) values (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, Integer.parseInt(post.getId()));
            statement.setString(2, post.getName());
            statement.setString(3, post.getText());
            statement.setString(4, post.getLink());
            statement.setString(5, post.getCreated());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = this.connection
                .prepareStatement("select * from post", Statement.RETURN_GENERATED_KEYS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post temp = new Post();
                    temp.setId(resultSet.getString("id"));
                    temp.setName(resultSet.getString("name"));
                    temp.setText(resultSet.getString("text"));
                    temp.setLink(resultSet.getString("link"));
                    temp.setCreated(resultSet.getString("created"));
                    posts.add(temp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        try (PreparedStatement statement = connection.prepareStatement("select * from post where id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getString("id"));
                    post.setName(resultSet.getString("name"));
                    post.setText(resultSet.getString("text"));
                    post.setLink(resultSet.getString("link"));
                    post.setCreated(resultSet.getString("created"));
                    return post;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        PsqlStore psqlStore = new PsqlStore(properties);
        Post post1 = new Post("1", "PostName1", "SomeText", "PostLink1", "16 02 2021");
        Post post2 = new Post("2", "PostName2", "TexTexText", "PostLink2", "15 02 2021");
        Post post3 = new Post("3", "PostName3", "Text", "PostLink3", "14 02 2021");
        psqlStore.save(post1);
        psqlStore.save(post2);
        psqlStore.save(post3);
        System.out.println(psqlStore.getAll());
        System.out.println(psqlStore.findById("2"));
    }
}