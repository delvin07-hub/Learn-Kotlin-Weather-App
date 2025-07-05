package com.example.cuaca

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather> // <-- tambahkan ini
)

data class Main(
    val temp: Float
)

data class Weather(
    val description: String,
    val icon: String
)

