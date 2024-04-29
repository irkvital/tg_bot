package bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DataBase {
    static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    public static DataBase init() {
        try {
            return new DataBaseCorrect();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            logger.warn("The database module will be disabled.");
            return new DataBase() {};
        }
    }

    public default void addNewUser(long id, String username, String firstName, String lastName) {}
    public default void addLocation(long id, double longitude, double latitude) {}
}
