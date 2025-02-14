package com.example.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import jakarta.annotation.PostConstruct;

@Component
public class TgBotInitializer {

    @Autowired
    TgBot tgBot;

    
    @PostConstruct
    public void init() {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(tgBot.getBotToken(), tgBot);
            Thread.currentThread().join();
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            System.out.println(e);
        }
    }
}
