package ru.sapiapps.randomword

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import ru.sapiapps.randomword.databinding.ActivityMainBinding
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.AnimationUtils
import org.json.JSONObject
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    // Объявление переменных
    lateinit var wordLists: WordLists  // Списки слов
    var currentLanguage: String = "RUS"  // Начальный язык
    val activeWordList = mutableListOf<String>()  // Список активных слов
    private var countDownTimer: CountDownTimer? = null  // Таймер

    /*
    private lateinit var soundPool: SoundPool
    private var tickSoundId: Int = 0
    private var finSoundId: Int = 0
     */

    data class WordLists(
        val russian: Map<String, List<String>>,
        val english: Map<String, List<String>>
    )

    private fun loadWordsFromJson(): WordLists {
        val json = assets.open("words.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)

        val russian = jsonObject.getJSONObject("russian").toMap().mapValues { it.value.shuffled() }
        val english = jsonObject.getJSONObject("english").toMap().mapValues { it.value.shuffled() }

        return WordLists(russian, english)
    }

    private fun JSONObject.toMap(): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        keys().forEach { key ->
            val list = getJSONArray(key)
            map[key] = List(list.length()) { list.getString(it) }
        }
        return map
    }

    // Запуск таймера
    private fun startTimer() {
        val time: Long = 16000
        val tick: Long = 1000
        val timerTextView = binding.timerTextView

        countDownTimer?.cancel() // Отменяем предыдущий таймер, если есть
        binding.timerToggle.isChecked = true
        binding.timerToggle.text = getString(R.string.time_on)

        countDownTimer = object : CountDownTimer(time, tick) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / tick
                /*
                if (secondsLeft != 15L && secondsLeft != 0L) {
                    soundPool.play(tickSoundId, 1f, 1f, 0, 0, 1f)
                }
                 */
                timerTextView.text = (secondsLeft).toString()

                if (secondsLeft == 0L) {
                    // soundPool.play(finSoundId, 1f, 1f, 0, 0, 1f)
                    vibrate()
                    binding.timerToggle.text = getString(R.string.time_off)
                }
            }

            override fun onFinish() {
                timerTextView.text = "0"
            }
        }.start()
    }

    private fun updateWordList(category: String, isChecked: Boolean) {
        // Определяем, к какому полю обращаться в зависимости от языка
        val languageWordList = when (currentLanguage) {
            "RUS" -> wordLists.russian
            "ENG" -> wordLists.english
            else -> emptyMap()  // В случае неизвестного языка, возвращаем пустую карту
        }

        // Получаем список слов для выбранной категории
        val selectedWords = languageWordList[category] ?: emptyList()

        if (isChecked) {
            activeWordList.addAll(selectedWords)
        } else {
            activeWordList.removeAll(selectedWords)
        }
    }

    private fun switchLanguage(newLanguage: String) {
        currentLanguage = newLanguage  // Сначала меняем язык

        // Меняем локаль приложения
        setLocale(if (newLanguage == "RUS") "ru" else "en")

        // Очищаем активный список слов и обновляем его
        activeWordList.clear()

        // Обновляем список слов на основе нового языка
        val languageWordList = when (currentLanguage) {
            "RUS" -> wordLists.russian
            "ENG" -> wordLists.english
            else -> emptyMap()
        }

        for (category in listOf("Nature", "Society", "Technology", "Fantastic")) {
            val toggleState = getToggleState(category)
            if (toggleState) {
                activeWordList.addAll(languageWordList[category] ?: emptyList())
            }
        }

        // Обновляем UI
        updateUILanguage()
        countDownTimer?.cancel()
        binding.timerTextView.text = ""
    }


    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration.apply { setLocale(locale) }
        val context = createConfigurationContext(config)
        resources.updateConfiguration(config, context.resources.displayMetrics)

        updateUILanguage()
        updateToggleTexts(binding)
    }


    private fun updateUILanguage() {
        binding.toggleNature.text = getString(R.string.toggle_nature)
        binding.toggleSociety.text = getString(R.string.toggle_society)
        binding.toggleTechnology.text = getString(R.string.toggle_technology)
        binding.toggleFantastic.text = getString(R.string.toggle_fantastic)
        binding.generateWordButton.text = getString(R.string.btn_text)
        binding.randomWordText.text = getString(R.string.text_field)

        binding.randomWordText.text = if (activeWordList.isEmpty()) {
            getString(R.string.no_words)
        } else {
            getString(R.string.text_field) // Текст поля по умолчанию
        }
    }

    private fun updateToggleTexts(binding: ActivityMainBinding) {
        binding.toggleNature.textOn = getString(R.string.toggle_nature)
        binding.toggleNature.textOff = getString(R.string.toggle_nature)

        binding.toggleSociety.textOn = getString(R.string.toggle_society)
        binding.toggleSociety.textOff = getString(R.string.toggle_society)

        binding.toggleTechnology.textOn = getString(R.string.toggle_technology)
        binding.toggleTechnology.textOff = getString(R.string.toggle_technology)

        binding.toggleFantastic.textOn = getString(R.string.toggle_fantastic)
        binding.toggleFantastic.textOff = getString(R.string.toggle_fantastic)
    }

    private fun getToggleState(category: String): Boolean {
        return when (category) {
            "Nature" -> binding.toggleNature.isChecked
            "Society" -> binding.toggleSociety.isChecked
            "Technology" -> binding.toggleTechnology.isChecked
            "Fantastic" -> binding.toggleFantastic.isChecked
            else -> false
        }
    }

    private fun initializeLanguageToggle() {
        binding.languageToggleGroup.check(
            if (currentLanguage == "RUS") R.id.btn_rus else R.id.btn_eng
        )

        binding.languageToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val newLanguage = when (checkedId) {
                    R.id.btn_rus -> "RUS"
                    R.id.btn_eng -> "ENG"
                    else -> return@addOnButtonCheckedListener
                }
                switchLanguage(newLanguage)
            }
        }
    }


    private fun populateActiveWordList() {
        // Определяем, к какому словарю обращаться в зависимости от языка
        val languageWordList = when (currentLanguage) {
            "RUS" -> wordLists.russian
            "ENG" -> wordLists.english
            else -> emptyMap() // Если язык неизвестен
        }

        // Перебираем все категории и добавляем слова из них
        val allCategories = listOf("Nature", "Society", "Technology", "Fantastic")
        for (category in allCategories) {
            val words = languageWordList[category] ?: emptyList() // Получаем слова из категории
            activeWordList.addAll(words) // Добавляем их в активный список
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(30) // Для старых версий
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomWordText = binding.randomWordText
        setLocale("ru")

        initializeLanguageToggle()

        wordLists = loadWordsFromJson()

        // Инициализация списка активных слов
        populateActiveWordList()

        // Установить начальный язык и обновить UI
        updateUILanguage()


        binding.toggleNature.setOnCheckedChangeListener { _, isChecked ->
            updateWordList("Nature", isChecked)
        }
        binding.toggleSociety.setOnCheckedChangeListener { _, isChecked ->
            updateWordList("Society", isChecked)
        }
        binding.toggleTechnology.setOnCheckedChangeListener { _, isChecked ->
            updateWordList("Technology", isChecked)
        }
        binding.toggleFantastic.setOnCheckedChangeListener { _, isChecked ->
            updateWordList("Fantastic", isChecked)
        }


        binding.timerToggle.setOnCheckedChangeListener { _, isChecked ->
            if (binding.timerTextView.text == "" && (randomWordText.text == getString(R.string.no_words) || randomWordText.text == "")) {
                vibrate()
            } else if (!isChecked && randomWordText.text != " " && (binding.timerTextView.text == "" || binding.timerTextView.text == "0")) {
                vibrate()
                startTimer()
            } else if (isChecked && randomWordText.text != " " && (binding.timerTextView.text == "" || binding.timerTextView.text == "0")) {
                vibrate()
                startTimer()
            } else {
                vibrate()
                countDownTimer?.cancel()
                binding.timerTextView.text = ""
                binding.timerToggle.text = getString(R.string.time_off)
            }
        }


        /*

        if (!isChecked && randomWordText.text != " " && (binding.timerTextView.text == "" || binding.timerTextView.text == "0") && binding.timerToggle.text == getString(R.string.time_off)) {
            vibrate()
            startTimer()
        } else if (isChecked && (randomWordText.text != getString(R.string.no_words) || randomWordText.text != " ")) {
            vibrate()
            startTimer()
        } else if (isChecked) {
            vibrate()
            countDownTimer?.cancel()
            binding.timerTextView.text = ""
            binding.timerToggle.text = getString(R.string.time_off)
        } else if (randomWordText.text == getString(R.string.no_words) || randomWordText.text == " ") {
            vibrate()
        }

        */

        /*
        // Инициализация SoundPool
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        // Загрузка звуков
        tickSoundId = soundPool.load(this, R.raw.tick, 1)
        finSoundId = soundPool.load(this, R.raw.fin, 1)
        */


        // Обработчик для генерации случайного слова
        binding.generateWordButton.setOnClickListener {
            if (activeWordList.isNotEmpty()) {
                val randomIndex = activeWordList.indices.random()
                val randomWord = activeWordList[randomIndex]
                activeWordList.removeAt(randomIndex)
                randomWordText.text = randomWord.replaceFirstChar { it.uppercase() }

                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                randomWordText.startAnimation(animation)


                if (binding.timerToggle.isChecked) {
                    startTimer()
                    binding.timerTextView.startAnimation(animation)
                }

            } else {
                countDownTimer?.cancel()
                binding.timerTextView.text = ""
                binding.timerToggle.text = getString(R.string.time_off)
                randomWordText.text = getString(R.string.no_words)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Остановка таймера при сворачивании приложения
        countDownTimer?.cancel()
        binding.timerTextView.text = ""
        binding.timerToggle.text = getString(R.string.time_off)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Освобождаем ресурсы SoundPool
        // soundPool.release()
    }
}