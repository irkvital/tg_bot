package com.example.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TgBotInitializer {

    @Autowired
    TgBot tgBot;

    
    @PostConstruct
    public void init() {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(tgBot.getToken(), tgBot);
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
