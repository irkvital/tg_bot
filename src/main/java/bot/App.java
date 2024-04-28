package bot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class App 
{
    public static void main( String[] args ) {
        String botToken = System.getenv("BOT_TOKEN");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new Bot(botToken));
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}