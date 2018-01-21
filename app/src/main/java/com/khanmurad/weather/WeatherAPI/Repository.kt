package com.khanmurad.weather.WeatherAPI
import com.khanmurad.weather.Chart

object Repository {
    internal val appid = "a5d69b16a86fd8a97b4d51b7eb55a47a"

    suspend fun getCityCharts(city: String): List<Chart> {
        val forecasts = getForecastsByCity(city, 10, "metric").list?.toList() ?: return emptyList()
        forecasts.size > 1 || return emptyList()
        return listOf(
                forecasts.tempChart,
                forecasts.humidityAndCloudinessChart,
                forecasts.windSpeedChart,
                forecasts.pressureChart,
                forecasts.rainAndSnowChart
        )
    }

    private suspend fun getForecastByCity(city: String, units: String? = null)
        : OpenWeatherAPI.Forecast = OpenWeatherAPI.service.getForecastByCity(appid, city, units).await()

    private suspend fun getForecastsByCity(city: String, cnt: Long? = null, units: String? = null)
        : OpenWeatherAPI.Forecasts = OpenWeatherAPI.service.getForecastsByCity(appid, city, cnt, units).await()

    private suspend fun getDailyForecastsByCity(city: String, cnt: Long? = null, units: String? = null)
        : OpenWeatherAPI.DailyForecasts = OpenWeatherAPI.service.getDailyForecastsByCity(appid, city, cnt, units).await()
}