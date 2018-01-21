package com.khanmurad.weather.WeatherAPI

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object OpenWeatherAPI {
    const val url = "http://api.openweathermap.org"

    class Clouds {
        var all: Int = 0
    }
    class Coord {
        var lat: Float = 0f
        var lon: Float = 0f
    }
    class City {
        var id: Long = 0
        var name: String? = null
        var coord: Coord? = null
        var country: String? = null
        var population: Long = 0
    }
    class Wind {
        var speed: Float = 0f
        var direction: Float = 0f
    }
    class Sys {
        var message: Float = 0f
        var country: String? = null
        var sunrise: Long = 0
        var sunset: Long = 0
    }
    class Weather {
        var id: Int = 0
        var icon: String? = null
        var description: String? = null
        /** Group of weather parameters (Rain, Snow, Extreme, etc.) */
        var main: String? = null
    }
    class Main {
        /** Humidity, % */
        var humidity: Int = 0
        /** Atmospheric pressure on the sea level by default, hPa */
        var pressure: Float = 0f
        var temp_max: Float = 0f
        /** Atmospheric pressure on the sea level, hPa */
        var sea_level: Float = 0f
        var temp_min: Float = 0f
        /** Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
        var temp: Float = 0f
        /** Atmospheric pressure on the ground level, hPa */
        var grnd_level: Float = 0f
    }
    class Temp {
        /** temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
        var min: Float = 0f
        var max: Float = 0f
        var day: Float = 0f
        var night: Float = 0f
        var eve: Float = 0f
        var morn: Float = 0f
    }
    class Forecast {
        var id: Long = 0
        var time: Long = 0
        var clouds: Clouds? = null
        var coord: Coord? = null
        var wind: Wind? = null
        var cod: Int = 0
        var sys: Sys? = null
        var name: String? = null
        var base: String? = null
        /** More info weather condition codes */
        var weather: Array<Weather>? = null
        /** Main conditions */
        var main: Main? = null
    }
    class DailyForecast {
        var time: Long = 0
        var temp: Temp? = null
        var pressure: Float = 0f
        var humidity: Int = 0
        var weather: Array<Weather>? = null
        var speed: Float = 0f
        var deg: Float = 0f
        var clouds: Int = 0
        var rain: Float = 0f
        var snow: Float = 0f
    }
    class Forecasts {
        var city: City? = null
        var cod: String? = null
        var message: String? = null
        /** Number of forecasts returned in "list" */
        var cnt: Long = 0
        var list: Array<Forecast>? = null
    }
    class DailyForecasts {
        var city: City? = null
        var cod: String? = null
        var message: String? = null
        /** Number of daily forecasts returned in "list" */
        var cnt: Long = 0
        var list: Array<DailyForecast>? = null
    }

    interface Service {
        @GET("/data/2.5/weather") fun getForecastByCity (
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("units") units: String? = null
        ) : Call<Forecast>

        @GET("/data/2.5/forecast") fun getForecastsByCity (
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("cnt") cnt: Long? = null,
                @Query("units") units: String? = null
        ): Call<Forecasts>

        @GET("/data/2.5/forecast/daily") fun getDailyForecastsByCity (
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("cnt") cnt: Long? = null,
                @Query("units") units: String? = null
        ): Call<DailyForecasts>
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val service = retrofit.create(Service::class.java)
}