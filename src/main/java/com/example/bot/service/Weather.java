package com.example.bot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bot.config.WeatherConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Weather {
    private static final String units = "metric";
    private static final String weatherUrl = "https://api.openweathermap.org";

    @Autowired
    private final WeatherConfig weatherConfig;

    public Weather(WeatherConfig weatherConfig) {
        this.weatherConfig = weatherConfig;
    }

    public WeatherData get(double latitude, double longitude) throws IOException {
        return getWeatherFromApi(latitude, longitude);
    }

    private WeatherData getWeatherFromApi(double latitude, double longitude) throws IOException {
            URL url = new URL(String.format("%s/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s&lang=RU",
                                weatherUrl,
                                latitude,
                                longitude,
                                weatherConfig.getToken(),
                                units));

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            log.info("request {}", url.toString());
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException(String.format("Response code =%d from: %s", responseCode, weatherUrl));
            }
            return readData(conn);
    }

    @Autowired
    private ObjectProvider<WeatherData> weatherDataProvider;

    private WeatherData readData(HttpsURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        StringBuilder response = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        JSONObject jsonWeather = json.getJSONObject("main");

        WeatherData data = weatherDataProvider.getObject();

        data.setDescription(    json.getJSONArray("weather").getJSONObject(0).getString("description"));
        data.setLocationName(   json.getString("name"));
        data.setTemp(           jsonWeather.getFloat("temp"));
        data.setFeelsLike(      jsonWeather.getFloat("feels_like"));
        data.setPressure(       jsonWeather.getFloat("pressure"));
        data.setHumidity(       jsonWeather.getInt("humidity"));
        data.setWind(           json.getJSONObject("wind").getFloat("speed"));

        return data;
    }
}