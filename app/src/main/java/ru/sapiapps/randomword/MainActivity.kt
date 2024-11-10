package ru.sapiapps.randomword

import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import ru.sapiapps.randomword.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    // Списки слов для каждой категории
    private val natureWords = listOf("дерево", "река", "гора", "цветок", "небо")
    private val techWords = listOf("компьютер", "смартфон", "интернет", "робот", "программа")
    private val relationshipsWords = listOf("любовь", "дружба", "поддержка", "общение", "понимание")

    // Активные категории для генерации слов
    private val activeCategories = mutableListOf<List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация и работа с элементами
        val categoryToggles = binding.categoryToggles
        val randomWordText = binding.randomWordText
        val generateWordButton = binding.generateWordButton

        // Списки слов
        val natureWords = listOf("лес", "гора", "река", "озеро", "облако", "звезда", "луна", "ветер", "камень", "солнце")
        val technologyWords = listOf("компьютер", "интернет", "робот", "смартфон", "дрон", "квант", "код", "платформа", "сеть", "алгоритм")
        val relationshipWords = listOf("дружба", "любовь", "семья", "поддержка", "доверие", "сочувствие", "близость", "уважение", "помощь", "эмпатия")

        // Обработчик для генерации случайного слова
        generateWordButton.setOnClickListener {
            val selectedWords = mutableListOf<String>()

            // Добавляем категории в зависимости от того, какие тумблеры выбраны
            if (binding.toggleNature.isChecked) {
                selectedWords.addAll(natureWords)
            }
            if (binding.toggleTechnology.isChecked) {
                selectedWords.addAll(technologyWords)
            }
            if (binding.toggleRelationships.isChecked) {
                selectedWords.addAll(relationshipWords)
            }

            // Если есть доступные слова, генерируем случайное
            if (selectedWords.isNotEmpty()) {
                val randomWord = selectedWords.random()
                randomWordText.text = generateNewWord(selectedWords, randomWord).replaceFirstChar { it.uppercase() }
            } else {
                randomWordText.text = "Слово"
            }
        }
    }
}


// Функция для генерации нового слова, отличного от текущего
fun generateNewWord(words: List<String>, currentWord: String): String {
    var newWord = currentWord
    while (newWord == currentWord) {
        newWord = words.random()
    }
    return newWord
}