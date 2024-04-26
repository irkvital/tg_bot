package bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class DataBase {
    private final static String dbContainerName = System.getenv("POSTGRES_IP");
    private final static String dbPort = System.getenv("POSTGRES_PORT");
    private final static String dbName = System.getenv("POSTGRES_DB");
    private final static String dbUsername = System.getenv("POSTGRES_USER");
    private final static String dbPassword = System.getenv("POSTGRES_PASSWORD");
    private final static String dbUrl = "jdbc:postgresql://" + dbContainerName + ":" 
                                        + dbPort + "/" + dbName + "?user=" 
                                        + dbUsername + "&password=" + dbPassword;
    
    private final static PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        dataSource.setURL(dbUrl);
    }

    // public static void tryToConnect() {
    //     System.out.println(dbUrl);
    //     try (Connection conn = dataSource.getConnection()) {
    //         PreparedStatement stmt = conn.prepareStatement("select * from users");
    //         ResultSet res = stmt.executeQuery();
    //         System.out.println("RESULT: " + res);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    public static String readSqlFile(String file) throws IOException {
        String out = null;
        StringBuilder strBuild = new StringBuilder();
        BufferedReader buffRead = new BufferedReader(new FileReader(file));
        try {
            while ((out = buffRead.readLine()) != null) {
                strBuild.append(out);
                strBuild.append('\n');
            }
            out = strBuild.toString();
            return out;
        } finally {
            buffRead.close();
        }
    }

    public static void addNewUser(Message message) {
        User user = message.getFrom();
        Long id = user.getId();
        String username = user.getUserName();
        String firsName = user.getFirstName();
        String lastName = user.getLastName();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(readSqlFile("./sql/exists_user.sql"));
            stmt.setLong(1, id);
            ResultSet res = stmt.executeQuery();
            boolean exists;
            if (res.next()) {
                exists = res.getBoolean(1);
            } else {
                throw new SQLException("Some problem when searching for a user");
            }

            if (!exists) {
                stmt = conn.prepareStatement(readSqlFile("./sql/add_new_user.sql"));
                stmt.setLong(1, id);
                stmt.setString(2, username);
                stmt.setString(3, firsName);
                stmt.setString(4, lastName);
                int addedUser = stmt.executeUpdate();
                if (addedUser > 0) {
                    System.out.println("Added user: " + username);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


        
    }
    
}
