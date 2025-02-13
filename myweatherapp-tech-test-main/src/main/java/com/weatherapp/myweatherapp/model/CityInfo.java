package com.weatherapp.myweatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CityInfo {

  @JsonProperty("address")
  String address;

  @JsonProperty("description")
  String description;

  @JsonProperty("currentConditions")
  CurrentConditions currentConditions;

  @JsonProperty("days")
  List<Days> days;

  // Getters
  public CurrentConditions getCurrentConditions() {
    return currentConditions;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCurrentConditions(CurrentConditions currentConditions) {
    this.currentConditions = currentConditions;
  }

  public static class CurrentConditions {
    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("sunrise")
    String sunrise;

    @JsonProperty("sunset")
    String sunset;

    @JsonProperty("feelslike")
    String feelslike;

    @JsonProperty("humidity")
    String humidity;

    @JsonProperty("conditions")
    String conditions;

    // Getters and setters for sunrise and sunset and conditions
    public String getSunrise() {
      return sunrise;
    }

    public void setSunrise(String sunrise) {
      this.sunrise = sunrise;
    }

    public String getSunset() {
      return sunset;
    }

    public void setSunset(String sunset) {
      this.sunset = sunset;
    }

    public String getConditions() {
      return conditions;
    }

    public void setConditions(String conditions) {
      this.conditions = conditions;
    }
  }

  public static class Days {

    @JsonProperty("datetime")
    String date;

    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("tempmax")
    String maxTemperature;

    @JsonProperty("tempmin")
    String minTemperature;

    @JsonProperty("conditions")
    String conditions;

    @JsonProperty("description")
    String description;

  }

}
