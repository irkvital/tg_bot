package com.example.bot.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.example.bot.config.BotConfig;

@Component
public class TgBot implements LongPollingSingleThreadUpdateConsumer {

    private final BotConfig config;
    private final TelegramClient telegramClient;
    private ExecutorService executorService;

    @Autowired
    private ObjectProvider<BotTask> taskProvider;


    public TgBot(BotConfig config) {
        this.config = config;
        telegramClient = new OkHttpTelegramClient(getBotToken());
        // logger.info("Start bot: {}", botToken);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void consume(Update update) {
        BotTask task = taskProvider.getObject(update, telegramClient);
        executorService.execute(task);
    }


}

