package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var tvWeatherInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCity = findViewById(R.id.etCity)
        btnGetWeather = findViewById(R.id.btnGetWeather)
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo)

        btnGetWeather.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeather(city)
            } else {
                Toast.makeText(this, "Enter a city name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather(city: String) {
        val apiKey = "YOUR_OPENWEATHERMAP_API_KEY"  // Replace with your actual API key

        RetrofitInstance.api.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        val temperature = weatherResponse.main.temp
                        val description = weatherResponse.weather[0].description
                        tvWeatherInfo.text = "Temperature: $temperature°C\nCondition: $description"
                    }
                } else {
                    tvWeatherInfo.text = "City not found!"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvWeatherInfo.text = "Failed to fetch weather!"
            }
        })
    }
}
