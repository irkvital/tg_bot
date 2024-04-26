package bot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
                if (update.getMessage().hasText()) {
                    caseText(update);
                }
                if (update.getMessage().hasLocation()) {
                    caseLocation(update);
                }
            }
        }

        private void caseText(Update update) {
            switch (update.getMessage().getText()) {
                case ("/start"):
                    caseTextStart(update);
                    break;
                default:
                    caseTextDefault(update);
                    break;
            }
        }

        private void caseTextStart(Update update) {
            long chatId = update.getMessage().getChatId();
            SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("Welcome!")
                        .build();
    
            // Add the keyboard to the message
            KeyboardButton keyboardButton = new KeyboardButton("Get my Location");
            keyboardButton.setRequestLocation(true);
            KeyboardRow keyboardRow = new KeyboardRow(keyboardButton);
            message.setReplyMarkup(ReplyKeyboardMarkup
                            .builder()
                            .resizeKeyboard(true)
                            .keyboardRow(keyboardRow)
                            .build());
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    
        private void caseTextDefault(Update update) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
        
            SendMessage sendMessage = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(messageText)
                                        .build();
            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        private void caseLocation(Update update) {
            Location location = update.getMessage().getLocation();
            long chatId = update.getMessage().getChatId();
    
            SendMessage sendMessage = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(location.toString())
                                        .build();
            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

   
    
}
