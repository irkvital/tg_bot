package bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

public class DataBase {
    private final static String db_container_name = System.getenv("POSTGRES_IP");
    private final static String db_port = System.getenv("POSTGRES_PORT");
    private final static String db_name = System.getenv("POSTGRES_DB");
    private final static String db_username = System.getenv("POSTGRES_USER");
    private final static String db_password = System.getenv("POSTGRES_PASSWORD");
    private final static String url = "jdbc:postgresql://" + db_container_name + ":" 
                                        + db_port + "/" + db_name + "?user=" 
                                        + db_username + "&password=" + db_password;
    
    private final static PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        dataSource.setURL(url);
    }

    public static void tryToConnect() {
        System.out.println(url);
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from users");
            ResultSet res = stmt.executeQuery();
            System.out.println("RESULT: "+res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

