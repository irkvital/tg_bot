package com.example.bot.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.example.bot.data_base.entity.DbLocation;
import com.example.bot.data_base.entity.DbUser;
import com.example.bot.data_base.service.LocationService;
import com.example.bot.data_base.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class BotTask implements Runnable {
    private Update update;
    private TelegramClient telegramClient;

    @Autowired
    private Weather weather;

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;

    BotTask(Update update, TelegramClient telegramClient) {
        this.update = update;
        this.telegramClient = telegramClient;
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
        log.info("User {} input text: {}", message.getChatId(), message.getText());
        switch (message.getText()) {
            case ("/start"):
                caseTextStart(message);
                break;
            case ("/users"):
                caseTextUsers(message);
                break;
            default:
                caseTextDefault(message);
                break;
        }
    }

    private void caseTextUsers(Message message) {
        long count = userService.count();
        sendText(message.getChatId(), "Количество пользователей: " + count);
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
            log.error("Can't execute message", e);
        }
    }

    private void caseTextStart(Message message) {
        User user = message.getFrom();
        long chatId = user.getId();
        String username = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if (!userService.existsById(chatId)) {
            userService.save(new DbUser(chatId, username, firstName, lastName));
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
                log.error("Can't execute message", e);
            }
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
        log.info("User {} send location: lat {} lon {}", chatId, latitude, longitude);

        String weatherText;
        try {
            weatherText = weather.get(latitude, longitude).toString();
            DbUser user = userService.getReference(chatId);
            DbLocation location = new DbLocation(user, latitude, longitude);
            locationService.save(location);
        } catch (IOException e) {
            e.printStackTrace();
            weatherText = "К сожалению, произошла ошибка";
        }
        sendText(chatId, weatherText);
    }
}
