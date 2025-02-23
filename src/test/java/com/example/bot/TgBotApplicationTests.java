package com.example.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bot.service.WeatherData;

@SpringBootTest(classes = WeatherData.class)
class TgBotApplicationTests {
	@Autowired
	WeatherData data;

	@Test
	public void testOne() {
		data.setDescription("aasdd");
		data.setLocationName("Moscow");
		data.setFeelsLike(4);
		data.setHumidity(56);
		data.setTemp(-3);
		data.setWind(7);
		data.setPressure(1000);

		String expected = "Moscow\n"+
		"aasdd\n"+
		"Температура: -3,0°C\n"+
		"Ощущается как: 4,0°C\n"+
		"Давление: 750 мм рт.ст.\n"+
		"Влажность: 56%\n"+
		"Скорость ветра: 7,0 м/с";

		assertEquals(expected, data.toString());
	}


}
