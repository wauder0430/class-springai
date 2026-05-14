package com.test.java.tool;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WeatherTool {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WeatherTool(ObjectMapper objectMapper) {
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }
    
    @Tool(description = """
    	    현재 날씨를 조회합니다.
    	    city 매개변수에는 반드시 도시 이름만 넣으세요.
    	    예: 서울, 부산, 제주, Tokyo, New York
    	    잘못된 예: 서울 날씨 알려줘, 부산 지금 비 와?
    	    """)
    public String getCurrentWeather(String city) {
    	
    	System.out.println("WeatherTool 호출됨");
    	System.out.println("city = " + city);

    	try {
            System.out.println("원본 city = " + city);

            city = normalizeCity(city);

            System.out.println("보정 city = " + city);

            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search"
                    + "?name=" + encodedCity
                    + "&count=1"
                    + "&language=ko"
                    + "&format=json";

            System.out.println("geoUrl = " + geoUrl);

            String geoResult = restClient.get()
                    .uri(geoUrl)
                    .retrieve()
                    .body(String.class);

            System.out.println("geoResult = " + geoResult);

            JsonNode geoRoot = objectMapper.readTree(geoResult);
            JsonNode results = geoRoot.path("results");

            if (!results.isArray() || results.size() == 0) {
                return "날씨 조회 실패: 해당 도시를 찾을 수 없습니다. city=" + city;
            }

            JsonNode location = results.get(0);

            double latitude = location.path("latitude").asDouble();
            double longitude = location.path("longitude").asDouble();
            String name = location.path("name").asText();
            String country = location.path("country").asText();
            String timezone = location.path("timezone").asText();

            String weatherUrl = "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m"
                    + "&timezone=Asia/Seoul";

            System.out.println("weatherUrl = " + weatherUrl);

            String weatherResult = restClient.get()
                    .uri(weatherUrl)
                    .retrieve()
                    .body(String.class);

            System.out.println("weatherResult = " + weatherResult);

            JsonNode weatherRoot = objectMapper.readTree(weatherResult);
            JsonNode current = weatherRoot.path("current");

            System.out.println("current = " + current);

            double temperature = current.path("temperature_2m").asDouble();
            int humidity = current.path("relative_humidity_2m").asInt();
            double windSpeed = current.path("wind_speed_10m").asDouble();
            int weatherCode = current.path("weather_code").asInt();

            String weatherText = convertWeatherCode(weatherCode);

            String result = """
                지역: %s, %s
                현재 기온: %.1f℃
                습도: %d%%
                풍속: %.1f km/h
                날씨 상태: %s
                """.formatted(name, country, temperature, humidity, windSpeed, weatherText);

            System.out.println("tool result = " + result);

            return result;

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return "날씨 조회 중 오류가 발생했습니다: " + e.getMessage();
    	}
    }
    
    private String normalizeCity(String city) {

        if (city == null) {
            return "";
        }

        city = city.trim();

        if (city.contains("서울")) return "Seoul";
        if (city.contains("부산")) return "Busan";
        if (city.contains("대구")) return "Daegu";
        if (city.contains("인천")) return "Incheon";
        if (city.contains("광주")) return "Gwangju";
        if (city.contains("대전")) return "Daejeon";
        if (city.contains("울산")) return "Ulsan";
        if (city.contains("세종")) return "Sejong";
        if (city.contains("제주")) return "Jeju";

        return city;
    }

    private String convertWeatherCode(int code) {

        return switch (code) {
            case 0 -> "맑음";
            case 1, 2, 3 -> "대체로 맑음 또는 흐림";
            case 45, 48 -> "안개";
            case 51, 53, 55 -> "약한 이슬비";
            case 56, 57 -> "어는 이슬비";
            case 61, 63, 65 -> "비";
            case 66, 67 -> "어는 비";
            case 71, 73, 75 -> "눈";
            case 77 -> "싸락눈";
            case 80, 81, 82 -> "소나기";
            case 85, 86 -> "눈 소나기";
            case 95 -> "뇌우";
            case 96, 99 -> "우박을 동반한 뇌우";
            default -> "알 수 없음";
        };
    }

}
