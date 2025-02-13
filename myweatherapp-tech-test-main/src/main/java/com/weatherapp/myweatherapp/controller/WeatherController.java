package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  // given two city names, compare the length of the daylight hours and
  // return the city with the longest day

  public LocalTime parseTime(String timeStr) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");
    return LocalTime.parse(timeStr, formatter);
  }

  @GetMapping("/compare/daylight/{city1}/{city2}")
  public ResponseEntity<String> compareDaylightHours(@PathVariable("city1") String city1,
      @PathVariable("city2") String city2) {

    try {
      CityInfo cityInfo1 = weatherService.forecastByCity(city1);
      CityInfo cityInfo2 = weatherService.forecastByCity(city2);

      LocalTime sunrise1 = parseTime(cityInfo1.getCurrentConditions().getSunrise());
      LocalTime sunset1 = parseTime(cityInfo1.getCurrentConditions().getSunset());
      long daylightHours1 = Duration.between(sunrise1, sunset1).toHours();

      LocalTime sunrise2 = parseTime(cityInfo2.getCurrentConditions().getSunrise());
      LocalTime sunset2 = parseTime(cityInfo2.getCurrentConditions().getSunset());
      long daylightHours2 = Duration.between(sunrise2, sunset2).toHours();

      if (daylightHours1 > daylightHours2) {
        return ResponseEntity.ok(city1 + " has the longest daylight hours.");
      } else if (daylightHours2 > daylightHours1) {
        return ResponseEntity.ok(city2 + " has the longest daylight hours.");
      } else {
        return ResponseEntity.ok("Both cities have the same daylight hours.");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error comparing daylight hours: " + e.getMessage());
    }
  }

  // given two city names, check which city its currently raining in

  @GetMapping("/compare/raining/{city1}/{city2}")
  public ResponseEntity<String> isItRaining(@PathVariable("city1") String city1,
      @PathVariable("city2") String city2) {
    try {
      CityInfo cityInfo1 = weatherService.forecastByCity(city1);
      CityInfo cityInfo2 = weatherService.forecastByCity(city2);

      boolean rainCity1 = cityInfo1.getCurrentConditions().getConditions().toLowerCase().contains("rain");
      boolean rainCity2 = cityInfo2.getCurrentConditions().getConditions().toLowerCase().contains("rain");

      if (rainCity1 && rainCity2) {
        return ResponseEntity.ok("It is raining in " + city1 + " and " + city2 + ".");
      } else if (rainCity1) {
        return ResponseEntity.ok("It is raining in " + city1 + " but not in " + city2 + ".");
      } else if (rainCity2) {
        return ResponseEntity.ok("It is raining in " + city2 + " but not in " + city1 + ".");
      } else {
        return ResponseEntity.ok("It isn't raining in either city.");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error checking for rain: " + e.getMessage());

    }

  }

}