package ru.sapiapps.randomword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sapiapps.randomword.ui.theme.RandomWordTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomWordTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}

@Composable
fun WordGeneratorScreen() {
    // Задаем список слов
    val words = listOf("Кот", "Собака", "Птица", "Дракон", "Феникс")

    // Переменная для хранения случайного слова
    var randomWord by remember { mutableStateOf("Нажми кнопку для слова") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображаем текст со случайным словом
        Text(
            text = randomWord,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Кнопка для генерации случайного слова
        Button(onClick = {
            // Генерация случайного индекса и обновление переменной
            randomWord = words[Random.nextInt(words.size)]
        }) {
            Text(text = "Сгенерировать слово")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWordGeneratorScreen() {
    RandomWordTheme {
        WordGeneratorScreen()
    }
}