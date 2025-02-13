package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.weatherapp.myweatherapp.controller.WeatherController;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.stream.Stream;

class WeatherServiceTest {

  @InjectMocks
  private WeatherController weatherController;

  @Mock
  private WeatherService weatherService;

  @Mock
  private VisualcrossingRepository weatherRepo;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testForecastByCity() {
    CityInfo mockCityInfo = new CityInfo();
    mockCityInfo.setAddress("London");

    when(weatherService.forecastByCity("London")).thenReturn(mockCityInfo);

    CityInfo result = weatherService.forecastByCity("London");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("London", result.getAddress()),
        () -> verify(weatherService, times(1)).forecastByCity("London"));
  }

  @Test
  void testParseTime() {
    String timeStr = "12:34:29";

    LocalTime parsedTime = weatherController.parseTime(timeStr);

    assertAll(
        () -> assertNotNull(parsedTime),
        () -> assertEquals(LocalTime.of(12, 34, 29), parsedTime));
  }

  @Nested
  class CompareDaylightTests {
    @ParameterizedTest
    @MethodSource("com.weatherapp.myweatherapp.service.WeatherServiceTest#provideDaylightComparisonTestCases")
    void testCompareDaylight(String city1, String city2, String sunrise1, String sunset1, String sunrise2,
        String sunset2, String expectedResponse) {
      CityInfo mockCityInfo1 = createMockCityInfo(sunrise1, sunset1, "clear");
      CityInfo mockCityInfo2 = createMockCityInfo(sunrise2, sunset2, "clear");

      when(weatherService.forecastByCity(city1)).thenReturn(mockCityInfo1);
      when(weatherService.forecastByCity(city2)).thenReturn(mockCityInfo2);

      ResponseEntity<String> response = weatherController.compareDaylightHours(city1, city2);

      /*
       * By using assertAll this ensures all assertions are executed even if one
       * fails.
       */
      assertAll(
          () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
          () -> assertEquals(expectedResponse, response.getBody()));
    }
  }

  /*
   * I have nested these test cases because it keeps the code organised and easier
   * to read.
   */
  @Nested
  class CompareRainTests {
    /*
     * Here I have used JUnit5's @ParameterizedTest to avoid writing repetitive test
     * methods.
     */
    @ParameterizedTest
    @MethodSource("com.weatherapp.myweatherapp.service.WeatherServiceTest#provideRainComparisonTestCases")
    void testCompareRain(String city1, String city2, String conditions1, String conditions2, String expectedResponse) {
      CityInfo mockCityInfo1 = createMockCityInfo("06:00:00", "18:00:00", conditions1);
      CityInfo mockCityInfo2 = createMockCityInfo("06:00:00", "18:00:00", conditions2);

      when(weatherService.forecastByCity(city1)).thenReturn(mockCityInfo1);
      when(weatherService.forecastByCity(city2)).thenReturn(mockCityInfo2);

      ResponseEntity<String> response = weatherController.isItRaining(city1, city2);

      assertAll(
          () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
          () -> assertEquals(expectedResponse, response.getBody()));
    }
  }

  /*
   * I have used a helper method here to reduce code duplication for every test.
   */
  private CityInfo createMockCityInfo(String sunrise, String sunset, String conditions) {
    CityInfo mockCityInfo = new CityInfo();
    CityInfo.CurrentConditions currentConditions = new CityInfo.CurrentConditions();
    currentConditions.setSunrise(sunrise);
    currentConditions.setSunset(sunset);
    currentConditions.setConditions(conditions);
    mockCityInfo.setCurrentConditions(currentConditions);
    return mockCityInfo;
  }

  private static Stream<Arguments> provideDaylightComparisonTestCases() {
    return Stream.of(
        Arguments.of("City1", "City2", "06:00:00", "18:00:00", "09:00:00", "17:00:00",
            "City1 has the longest daylight hours."),
        Arguments.of("City1", "City2", "09:00:00", "17:00:00", "06:00:00", "18:00:00",
            "City2 has the longest daylight hours."),
        Arguments.of("City1", "City2", "09:00:00", "17:00:00", "08:00:00", "16:00:00",
            "Both cities have the same daylight hours."));
  }

  private static Stream<Arguments> provideRainComparisonTestCases() {
    return Stream.of(
        Arguments.of("City1", "City2", "rain", "rain", "It is raining in City1 and City2."),
        Arguments.of("City1", "City2", "rain", "overcast", "It is raining in City1 but not in City2."),
        Arguments.of("City1", "City2", "overcast", "rain", "It is raining in City2 but not in City1."),
        Arguments.of("City1", "City2", "clear", "overcast", "It isn't raining in either city."));
  }
}