package bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weather {
    private static final Logger logger = LoggerFactory.getLogger(Weather.class);
    private static final String weatherToken = System.getenv("WEATHER_TOKEN");
    private static final String units = "metric";
    private static final String weatherUrl = "https://api.openweathermap.org";

    private String description;
    private String locationName;
    private float temp;
    private float feelsLike;
    private float pressure;
    private float wind;
    private int humidity;


    public String get(double latitude, double longitude) {
        String out;
        try {
            getWeather(latitude, longitude);
            out = String.format("%s\n%s\nТемпература: %.1f°C\nОщущается как: %.1f°C\nДавление: %.0f мм рт.ст.\nВлажность: %d%%\nСкорость ветра: %.1f м/с",
                                locationName,
                                description,
                                temp,
                                feelsLike,
                                pressure * 0.75,
                                humidity,
                                wind);
        } catch (IOException e) {
            logger.error(e.getMessage());
            out = "Простите, у нас ошибочка.";
        }

        return out;
    }

    private void getWeather(double latitude, double longitude) throws IOException {
            URL url = new URL(String.format("%s/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s&lang=RU",
                                weatherUrl,
                                latitude,
                                longitude,
                                weatherToken,
                                units));

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            logger.info("request {}", url.toString());
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException(String.format("Response code =%d from: %s", responseCode, weatherUrl));
            }
            setWeatherData(conn);
    }

    private void setWeatherData(HttpsURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        JSONObject jsonWeather = json.getJSONObject("main");

        this.description =  json.getJSONArray("weather").getJSONObject(0).getString("description");
        this.locationName = json.getString("name");
        this.temp =         jsonWeather.getFloat("temp");
        this.feelsLike =    jsonWeather.getFloat("feels_like");
        this.pressure =     jsonWeather.getFloat("pressure");
        this.humidity =     jsonWeather.getInt("humidity");
        this.wind =         json.getJSONObject("wind").getFloat("speed");
    }
}