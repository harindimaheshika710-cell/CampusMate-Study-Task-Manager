/*
 * SOURCE ACKNOWLEDGEMENT REQUIRED BY ASSIGNMENT
 * Project: CampusMate - Study Task Manager mobile application.
 * This file was created for this assignment by the project team with AI code support from
 * OpenAI ChatGPT: https://chat.openai.com/
 * Android implementation concepts referenced from official Android Developers documentation:
 * - Activity / app components: https://developer.android.com/guide/components/activities
 * - SQLite data storage: https://developer.android.com/training/data-storage/sqlite
 * - RecyclerView list UI: https://developer.android.com/develop/ui/views/layout/recyclerview
 * - CountDownTimer: https://developer.android.com/reference/android/os/CountDownTimer
 * External API documentation used in this project:
 * - Open-Meteo Geocoding API: https://open-meteo.com/en/docs/geocoding-api
 * - Open-Meteo Forecast API: https://open-meteo.com/en/docs
 * - Open Library Search API: https://openlibrary.org/dev/docs/api/search
 * If a group member edits or owns this file, add their real name/student ID in
 * INDIVIDUAL_CONTRIBUTIONS.txt before submission.
 */
package com.example.campusmate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.campusmate.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

public class WeatherActivity extends Activity {
    private EditText edtCity;
    private TextView txtWeatherStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        edtCity = findViewById(R.id.edtCity);
        txtWeatherStatus = findViewById(R.id.txtWeatherStatus);
        Button btnGetWeather = findViewById(R.id.btnGetWeather);
        Button btnCityColombo = findViewById(R.id.btnCityColombo);
        Button btnCityKandy = findViewById(R.id.btnCityKandy);
        Button btnCityKurunegala = findViewById(R.id.btnCityKurunegala);

        btnGetWeather.setOnClickListener(v -> searchCityAndWeather());
        btnCityColombo.setOnClickListener(v -> setCityAndSearch("Colombo"));
        btnCityKandy.setOnClickListener(v -> setCityAndSearch("Kandy"));
        btnCityKurunegala.setOnClickListener(v -> setCityAndSearch("Kurunegala"));
    }

    private void setCityAndSearch(String city) {
        edtCity.setText(city);
        searchCityAndWeather();
    }

    private String encodeText(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    private void searchCityAndWeather() {
        String city = edtCity.getText().toString().trim();
        if (city.isEmpty()) {
            edtCity.setError("City is required");
            edtCity.requestFocus();
            return;
        }

        txtWeatherStatus.setText("Searching city...");
        String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + encodeText(city) + "&count=1&language=en&format=json";

        // Source: Open-Meteo Geocoding API - https://open-meteo.com/en/docs/geocoding-api
        // Purpose: convert a city name into latitude and longitude for the weather screen.
        NetworkUtils.getJson(url, new NetworkUtils.JsonCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray results = object.optJSONArray("results");
                    if (results == null || results.length() == 0) {
                        txtWeatherStatus.setText("City not found. Try another city name.");
                        return;
                    }

                    JSONObject cityObj = results.getJSONObject(0);
                    double latitude = cityObj.getDouble("latitude");
                    double longitude = cityObj.getDouble("longitude");
                    String cityName = cityObj.optString("name", city);
                    String country = cityObj.optString("country", "");
                    fetchWeather(latitude, longitude, cityName, country);
                } catch (Exception e) {
                    txtWeatherStatus.setText("Could not read city data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                txtWeatherStatus.setText(message);
            }
        });
    }

    private void fetchWeather(double latitude, double longitude, String cityName, String country) {
        txtWeatherStatus.setText("Loading weather and 3-day forecast...");
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                "&longitude=" + longitude +
                "&current_weather=true" +
                "&daily=weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max" +
                "&forecast_days=3&timezone=auto";

        // Source: Open-Meteo Forecast API - https://open-meteo.com/en/docs
        // Purpose: show current weather and a short travel suggestion for students.
        NetworkUtils.getJson(url, new NetworkUtils.JsonCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject current = object.getJSONObject("current_weather");
                    double temperature = current.getDouble("temperature");
                    double windSpeed = current.getDouble("windspeed");
                    int weatherCode = current.getInt("weathercode");
                    String time = current.optString("time", "Not available");

                    String condition = getWeatherCondition(weatherCode);
                    String suggestion = getSuggestion(weatherCode, windSpeed);

                    StringBuilder output = new StringBuilder();
                    output.append("📍 Location: ").append(cityName).append(", ").append(country).append("\n");
                    output.append("🕒 Updated: ").append(time).append("\n\n");
                    output.append("🌡️ Temperature: ").append(temperature).append(" °C\n");
                    output.append("💨 Wind speed: ").append(windSpeed).append(" km/h\n");
                    output.append("☁️ Condition: ").append(condition).append("\n\n");
                    output.append("🎒 Student suggestion: ").append(suggestion).append("\n\n");
                    output.append(readDailyForecast(object));

                    txtWeatherStatus.setText(output.toString());
                } catch (Exception e) {
                    txtWeatherStatus.setText("Could not read weather data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                txtWeatherStatus.setText(message);
            }
        });
    }

    private String readDailyForecast(JSONObject object) {
        try {
            JSONObject daily = object.optJSONObject("daily");
            if (daily == null) return "3-day forecast not available.";

            JSONArray times = daily.optJSONArray("time");
            JSONArray codes = daily.optJSONArray("weathercode");
            JSONArray maxTemps = daily.optJSONArray("temperature_2m_max");
            JSONArray minTemps = daily.optJSONArray("temperature_2m_min");
            JSONArray rainChance = daily.optJSONArray("precipitation_probability_max");

            if (times == null || codes == null || maxTemps == null || minTemps == null) {
                return "3-day forecast not available.";
            }

            StringBuilder forecast = new StringBuilder("📅 3-Day Forecast\n");
            int limit = Math.min(3, times.length());
            for (int i = 0; i < limit; i++) {
                forecast.append("• ").append(times.optString(i)).append(": ")
                        .append(getWeatherCondition(codes.optInt(i))).append(", ")
                        .append(minTemps.optDouble(i)).append("°C - ")
                        .append(maxTemps.optDouble(i)).append("°C");
                if (rainChance != null) {
                    forecast.append(", rain chance ").append(rainChance.optInt(i)).append("%");
                }
                forecast.append("\n");
            }
            return forecast.toString();
        } catch (Exception e) {
            return "3-day forecast not available.";
        }
    }

    private String getWeatherCondition(int code) {
        if (code == 0) return "Clear sky";
        if (code == 1 || code == 2 || code == 3) return "Mainly clear / partly cloudy";
        if (code == 45 || code == 48) return "Foggy";
        if (code >= 51 && code <= 67) return "Drizzle or rain";
        if (code >= 71 && code <= 77) return "Snow";
        if (code >= 80 && code <= 82) return "Rain showers";
        if (code >= 95) return "Thunderstorm";
        return "Unknown weather";
    }

    private String getSuggestion(int code, double windSpeed) {
        if ((code >= 51 && code <= 82) || code >= 95) {
            return "Carry an umbrella and leave early for campus.";
        }
        if (code == 45 || code == 48) {
            return "Foggy weather. Be careful during travel.";
        }
        if (windSpeed > 35) {
            return "Windy weather. Travel carefully and check transport delays.";
        }
        return "Weather looks fine for campus travel.";
    }
}
