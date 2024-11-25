package ru.sapiapps.randomword

import android.os.Bundle
import androidx.activity.ComponentActivity
import ru.sapiapps.randomword.databinding.ActivityMainBinding
import android.os.CountDownTimer

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    // Переменная для таймера
    private var countDownTimer: CountDownTimer? = null

    // Запуск таймера
    private fun startTimer() {
        val timerTextView = binding.timerTextView // Ссылка на TextView для таймера

        // Отмена предыдущего таймера, если он запущен
        countDownTimer?.cancel()

        // Создаём новый таймер на 15 секунд (Поставлю +1 секунду, чтобы вначале отображалось 15)
        countDownTimer = object : CountDownTimer(16000, 1000) { // 15000 мс = 15 секунд
            override fun onTick(millisUntilFinished: Long) {
                // Обновляем текст таймера каждую секунду
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Когда таймер завершился
                timerTextView.text = "0"
            }
        }.start()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация и работа с элементами
        val randomWordText = binding.randomWordText
        val generateWordButton = binding.generateWordButton



        val selectedWords = mutableListOf<String>()
        selectedWords.addAll(natureWords)
        selectedWords.addAll(socialWords)
        selectedWords.addAll(technologyWords)
        selectedWords.addAll(fantasticWords)

        binding.wordsCountTextView.text = selectedWords.size.toString()


        binding.toggleNature.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(natureWords)
            } else {
                selectedWords.removeAll(natureWords)
            }
        }

        binding.toggleSocial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(socialWords)
            } else {
                selectedWords.removeAll(socialWords)
            }
        }

        binding.toggleTechnology.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(technologyWords)
            } else {
                selectedWords.removeAll(technologyWords)
            }
        }

        binding.toggleFantastic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(fantasticWords)
            } else {
                selectedWords.removeAll(fantasticWords)
            }
        }


        // Обработчик для генерации случайного слова
        generateWordButton.setOnClickListener {
            if (selectedWords.isNotEmpty()) {                       // Если есть доступные слова, генерируем случайное
                val randomIndex = selectedWords.indices.random()    // Получаем случайный индекс
                val randomWord = selectedWords[randomIndex]       // Получаем слово по индексу
                selectedWords.removeAt(randomIndex)               // Удаляем слово по индексу
                randomWordText.text = randomWord.replaceFirstChar { it.uppercase() }

                binding.wordsCountTextView.text = selectedWords.size.toString()

                // Запуск таймера
                if (binding.timerToggle.isChecked) {
                    startTimer()
                } else {
                    countDownTimer?.cancel()
                    binding.timerTextView.text = ""
                }
            } else {
                // val count = relationshipWords.size
                randomWordText.text = "Нет слов.."
                binding.wordsCountTextView.text = "0"

                    countDownTimer?.cancel()
                binding.timerTextView.text = ""
            }
        }
    }
}

