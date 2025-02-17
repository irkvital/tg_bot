package com.example.bot.service;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class Buttons {
    private static final KeyboardButton LOCATION_BUTTON = new KeyboardButton("Get my Location");

    public static ReplyKeyboardMarkup replyKeyboard() {
        LOCATION_BUTTON.setRequestLocation(true);
        KeyboardRow keyboardRow = new KeyboardRow(LOCATION_BUTTON);

        ReplyKeyboardMarkup replyKeyboard = ReplyKeyboardMarkup
                                    .builder()
                                    .resizeKeyboard(true)
                                    .keyboardRow(keyboardRow)
                                    .build();
        return replyKeyboard;
    }
}