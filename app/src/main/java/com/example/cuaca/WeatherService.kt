package com.example.cuaca

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>
}
