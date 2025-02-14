package com.example.bot.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@Scope("prototype")
public class WeatherData {
    private String description;
    private String locationName;
    private float temp;
    private float feelsLike;
    private float pressure;
    private float wind;
    private int humidity;


    @Override
    public String toString() {
        String out = String.format("%s\n%s\nТемпература: %.1f°C\nОщущается как: %.1f°C\nДавление: %.0f мм рт.ст.\nВлажность: %d%%\nСкорость ветра: %.1f м/с",
        locationName,
        description,
        temp,
        feelsLike,
        pressure * 0.75,
        humidity,
        wind);
        return out;
    }

}
