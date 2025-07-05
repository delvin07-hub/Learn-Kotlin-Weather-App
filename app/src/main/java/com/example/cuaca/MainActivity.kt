package com.example.cuaca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cuaca.ui.theme.CuacaTheme
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cuaca.WeatherService
import com.example.cuaca.WeatherResponse
import com.example.cuaca.BuildConfig



class MainActivity : ComponentActivity() {
    private val apiKey = BuildConfig.WEATHER_API_KEY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuacaTheme {
                WeatherScreen(apiKey)
            }
        }
    }
}

@Composable
fun WeatherScreen(apiKey: String) {
    var city by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Hasil cuaca akan tampil di sini") }

    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service = retrofit.create(WeatherService::class.java)

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Masukkan nama kota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val call = service.getWeather(city, apiKey, "id", "metric")
            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful) {
                        val weather = response.body()
                        val condition = weather?.weather?.firstOrNull()?.description ?: "kondisi tidak tersedia"
                        val temperature = weather?.main?.temp
                        val cityName = weather?.name
                        result = "Cuaca di $cityName: $condition, $temperature°C"

                    } else {
                        result = "Kota tidak ditemukan"
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    result = "Gagal: ${t.message}"
                }
            })
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cek Cuaca")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result)
    }
}
