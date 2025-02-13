WeatherController
The WeatherController class handles all the HTTP requests related to weather data and provides the following endpoints:
1. Fetch weather forecast for a city (/forecast/{city})
2. Compare daylight hours between two cities (/compare/daylight/{city1}/{city2})
3. Check if it is raining in two cities (/compare/raining/{city1}/{city2})

WeatherServiceTest
The WeatherServiceTest is a test suite for the WeatherController. I have used JUnit5 and Mockito to mock dependencies and validate the controller's behaviour. I have organised the tests into nested classes and used parameterised test to cover multiple scenarios. 

Key features and design choices

1. WeatherController

Endpoints
1. /forecast/{city}
This fetches the weather forecast for a specific city and returns a CityInfo object containing weather details.
2. /compare/daylight/{city1}/{city2}
This compares the daylight hours between two cities and returns a message indicating which city has longer
daylight hours.
3. /compare/raining/{city1/{city2}
This checks if it is raining in two cities and returns a message indicating the raining status of both cities.

Error Handling
Both compareDaylightHours and isItRaining methods include try-catch blocks to handle exceptions and return
appropriate error messages.


2. WeatherServiceTest

Mocking Dependencies
The WeatherController depends on the WeatherService to fetch weather data. 
Mockito is used to mock the WeatherService and isolate the controller for testing.

Test Organisation
The tests are organised into nested classes using JUnit 5's @Nested annotation:
- CompareDaylightTests this tests for comparing daylight hours
- CompareRainTests this tests for checking if it is raining

Parameterised Tests
The @ParameterisedTest annotation is used to test multiple scenarios with minimal code duplication.
Test cases are provided using the @MethodSource annotation.

Helper Methods
createMockCityInfo
- This creates a mock CityInfo object with specified sunrise, sunset and weather conditions.
- It reduces code duplication and improves maintainability.

Assertions
- assertAll is used to group multiple assertions together and ensures they are all executed
even if one fails
