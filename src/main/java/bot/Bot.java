package bot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Bot implements LongPollingSingleThreadUpdateConsumer  {
    private final TelegramClient telegramClient;
    String botToken;
    ExecutorService executorService;

    public Bot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        this.botToken = botToken;
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
                e.printStackTrace();
            }
        }

        private void caseTextStart(Message message) {
            DataBase.addNewUser(message);
            long chatId = message.getChatId();
            String username = message.getFrom().getUserName();
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
                e.printStackTrace();
            }
        }
    
        private void caseTextDefault(Message message) {
            
            String messageText = message.getText();
            long chatId = message.getChatId();
            sendText(chatId, messageText);
            System.out.println(update.getMessage().getText());
        }

        private void caseLocation(Message message) {
            Location location = message.getLocation();
            long chatId = message.getChatId();
            sendText(chatId, location.toString());
        }

    }

   
    
}
