package bot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Bot implements LongPollingSingleThreadUpdateConsumer  {
    private final static Logger logger = LoggerFactory.getLogger(Bot.class);
    private final TelegramClient telegramClient;
    String botToken;
    ExecutorService executorService;

    public Bot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        this.botToken = botToken;
        logger.info("Start bot: {}", botToken);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void consume(Update update) {
        BotTask task = new BotTask(update);
        executorService.execute(task);
    }


    private class BotTask implements Runnable {
        Update update;
        // final String url = "https://api.telegram.org/";

        BotTask(Update update) {
            this.update = update;
        }

        @Override
        public void run() {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasText()) {
                    caseText(message);
                }
                if (message.hasLocation()) {
                    caseLocation(message);
                }
            }
        }

        private void caseText(Message message) {
            logger.info("User {} input text: {}", message.getChatId(), message.getText());
            switch (message.getText()) {
                case ("/start"):
                    caseTextStart(message);
                    break;
                default:
                    caseTextDefault(message);
                    break;
            }
        }

        private void sendText(long chatId, String text) {
            SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(text)
                        .build();

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Can't execute message", e);
            }
        }

        private void caseTextStart(Message message) {
            User user = message.getFrom();
            long chatId = user.getId();
            String username = user.getUserName();
            String firsName = user.getFirstName();
            String lastName = user.getLastName();
            DataBase.addNewUser(chatId, username, firsName, lastName);
            SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("Welcome, " + username + "!")
                        .build();
    
            // Add the keyboard to the message
            sendMessage.setReplyMarkup(Buttons.replyKeyboard());

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Can't execute message", e);
            }
        }
    
        private void caseTextDefault(Message message) {
            String messageText = message.getText();
            long chatId = message.getChatId();
            sendText(chatId, messageText);
        }

        private void caseLocation(Message message) {
            Double latitude = message.getLocation().getLatitude();
            Double longitude = message.getLocation().getLongitude();
            long chatId = message.getChatId();
            logger.info("User {} send location: lat {} lon {}", chatId, latitude, longitude);
            sendText(chatId, latitude.toString() + " " + longitude.toString()); // !!!!!!!!!!
            DataBase.addLocation(chatId, longitude, latitude);
        }
    }

   
    
}
