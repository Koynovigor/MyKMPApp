package org.l3on1kl.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun App() {
    val weather = WeatherService("60b74e3943fe48cd86c23422243105")
    var city by remember { mutableStateOf("Москва") }
    var inputCity by remember { mutableStateOf(city) }
    var temperature by remember { mutableStateOf("Загрузка...") }
    var weatherConditionText by remember { mutableStateOf("Загрузка...") }
    var weatherConditionIcon by remember { mutableStateOf("Загрузка...") }

    fun getWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val weatherData = weather.getWeather(city)
            if (weatherData != null) {
                temperature = "${weatherData.current.temp_c}°C"
                weatherConditionText = weatherData.current.condition.text
                weatherConditionIcon = weatherData.current.condition.icon
            } else {
                temperature = "Фиг его знает, какая там погода"
                weatherConditionText = "Мб ошибся в названии города =D"
                weatherConditionIcon = ""
            }
        }
    }

    LaunchedEffect(city) {
        getWeather(city)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Погода в $city",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Температура: $temperature",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                text = weatherConditionText,
                fontSize = 18.sp
            )
            AsyncImage(
                model = "https:$weatherConditionIcon",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp)
            )
        }

        TextField(
            value = inputCity,
            onValueChange = { inputCity = it },
            label = { Text("Город") },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                city = inputCity
                getWeather(city)
            }
        ) {
            Text("Обновоить")
        }
    }
}
