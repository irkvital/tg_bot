package bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) {
        String botToken = System.getenv("BOT_TOKEN");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new Bot(botToken));
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error(e.getMessage() + e);
        }

    }
}