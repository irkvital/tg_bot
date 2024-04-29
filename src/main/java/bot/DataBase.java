package bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {
    private final static Logger logger = LoggerFactory.getLogger(DataBase.class);
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
            logger.info("The file {} was read correctly.", file);
            return out;
        } finally {
            buffRead.close();
        }
    }

    public static void addNewUser(long id, String username, String firstName, String lastName) {
        boolean exists;
        String sqlFile = "./sql/exists_user.sql";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(readSqlFile(sqlFile));
            stmt.setLong(1, id);
            ResultSet res = stmt.executeQuery();
            
            if (res.next()) {
                exists = res.getBoolean(1);
                logger.info("Checked the user {}.", id);
            } else {
                throw new SQLException("Some problem when searching for a user");
            }
        } catch (SQLException | IOException e) {
            logger.error("Some problems with sql query at file {}", sqlFile, e);
            return;
        }

        if (!exists) {
            sqlFile = "./sql/add_new_user.sql";
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(readSqlFile(sqlFile));
                stmt.setLong(1, id);
                stmt.setString(2, username);
                stmt.setString(3, firstName);
                stmt.setString(4, lastName);
                int addedUser = stmt.executeUpdate();
                if (addedUser > 0) {
                    logger.info("Added new user: {}", id);
                }
            } catch (SQLException | IOException e) {
                logger.error("Some problems with sql query at file {}", sqlFile, e);
            }
        } else {
            logger.info("User {} already exists.", id);
        }
    }

    public static void addLocation(long id, double longitude, double latitude) {
        String sqlFile = "./sql/add_location.sql";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(readSqlFile(sqlFile));
            stmt.setLong(1, id);
            stmt.setDouble(2, longitude);
            stmt.setDouble(3, latitude);
            int addedLocation = stmt.executeUpdate();
            if (addedLocation > 0) {
                logger.info("Added location {} {} for user {}", longitude, latitude, id);
            }
        } catch (SQLException | IOException e) {
            logger.error("Some problems with sql query at file {}", sqlFile, e);
            return;
        }
    }
}
