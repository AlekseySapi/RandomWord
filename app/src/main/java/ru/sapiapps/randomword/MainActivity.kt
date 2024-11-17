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
        val natureWords = mutableListOf(
            "лес", "гора", "река", "озеро", "облако", "звезда", "луна", "ветер", "камень", "солнце",
            "океан", "песок", "планета", "почва", "сосна", "шум", "птица", "волна", "цветок",
            "трава", "болото", "сезон", "вулкан", "пейзаж", "дождь", "молния", "пыльца", "степь",
            "гриб", "земля", "песчаник", "сирень", "дерево", "жук", "капля", "полет", "поляна",
            "животное", "скала", "сумрак", "рябина", "погода", "замерзание", "туча", "шторм",
            "свет", "восход", "запах", "весна", "период", "цвет", "простор", "вечер", "мороз",
            "прилив", "край", "голубь", "долина", "небо", "гребень", "лист", "ручей", "пруд",
            "роза", "рельеф", "туман", "гладь", "крест", "сила", "глубина", "пещера", "щука",
            "космос", "обрыв", "луч", "вода", "рассвет", "порох", "щука", "осина", "бор", "русло",
            "источник", "каньон", "сурок", "осень", "цветение", "планетарий", "дуга", "дождик",
            "гребень", "олень", "падение", "тропинка", "жёлудь", "петух", "гроза", "кедр", "жизнь",
            "кипарис", "сельди", "черепаха", "лоза", "копытце", "борозда", "тропа", "полевка",
            "стрела", "кувшинка", "пахарь", "каштан", "папоротник", "листик", "пчела", "лаванда",
            "тополь", "лён", "вьюн", "вихрь", "ягодник", "берёза", "анемона", "песчаник", "поросль",
            "лён", "бамбук", "коряга", "пальма", "шмель", "параллель", "корень", "серпантин", "туман",
            "бугор", "пруд", "попугай", "посох", "аллея", "цветок", "багульник", "пламя", "перо",
            "щавель", "грибок", "муравей", "сойка", "флора", "заказник", "дикая", "плотина", "топот",
            "пожар", "седина", "заря", "сельце", "шишка", "мертвая", "камыш", "мох", "фауна", "камнепад",
            "паутина", "король", "вихрь", "побережье", "разлив", "популяция", "парк", "скворечник",
            "папа", "липка", "кочка", "век", "камень", "стрела", "сосна", "мука", "покров", "проводник",
            "север", "песчаная", "плотина", "горох", "море", "мелодия", "крест", "попугай", "светильник",
            "подросток", "вихрь", "петр", "сухопутный", "паук", "камыш", "кукушкин", "песня", "берег",
            "трудность", "юг", "восток"
        )

        val technologyWords = mutableListOf(
            "компьютер", "интернет", "робот", "смартфон", "дрон", "квант", "код", "платформа", "сеть", "алгоритм",
            "нейросеть", "бит", "программирование", "цифровизация", "система", "антивирус", "сигнал", "модем",
            "кодировка", "протокол", "данные", "облачный", "гаджет", "хакер", "веб", "обновление", "объект", "секунда",
            "тест", "запуск", "дебаг", "класс", "вопрос", "подключение", "конфигурация", "доступ", "игра", "аппликация",
            "сборка", "модуль", "файл", "сигнализация", "работа", "компиляция", "разработка", "необходимость", "сервера",
            "проект", "партнер", "дисплей", "параметр", "контроль", "метод", "сетевой", "слой", "разделение", "вход",
            "выход", "разрешение", "виртуальный", "копия", "скрипт", "папка", "приложение", "вычисления", "устройство",
            "терминал", "механизм", "экран", "сетка", "график", "основной", "память", "планшет", "сервер", "клавиатура",
            "модель", "разрешение", "битрейт", "вебсайт", "интерфейс", "технология", "система", "обновление", "документ",
            "функция", "обратная связь", "сеть", "платформа", "скорость", "передача", "системы", "браузер", "провайдер",
            "ресурс", "анализ", "отладка", "архитектура", "масштаб", "схема", "папка", "архив", "система", "расширение",
            "поток", "память", "операция", "удаление", "пользователь", "команда", "основы", "обработка", "тестирование",
            "подключение", "параметры", "мобильность", "производительность", "аппарат", "платформа", "структура", "порт",
            "удаленный", "каталог", "поток", "синхронизация", "параметры", "защита", "функционал", "обработчик", "мобильность",
            "конфиденциальность", "программное", "обеспечение", "поток", "перезагрузка", "кэш", "комплекс", "платформа",
            "операционная", "система", "эмулятор", "обновление", "установка", "модификация", "сетевая", "карта", "отчёт",
            "драйвер", "интерфейс", "архиватор", "прокси", "обработчик", "размер", "сервер", "параметры", "команда",
            "оперативная", "память", "система", "данные", "логика", "вкладка", "страница", "параметр", "модернизация",
            "платформа", "разрешение", "модули", "сеть", "резервное", "копирование", "отключение", "инсталляция", "модули",
            "оптимизация", "калькулятор", "алгоритмизация", "анализатор", "синхронизация", "проектирование", "модуль",
            "интерфейс", "профиль", "рабочий", "среда", "платформа", "планирование"
        )

            val relationshipWords = mutableListOf(
            "дружба", "любовь", "семья", "поддержка", "доверие", "сочувствие", "близость", "уважение", "помощь", "эмпатия",
            "взаимопонимание", "счастье", "забота", "согласие", "соглашение", "сопереживание", "совместимость", "лояльность",
            "открытость", "тепло", "объятие", "искренность", "прощение", "комфорт", "радость", "партнер", "союз", "взаимопомощь",
            "дружелюбие", "освежение", "чувство", "удовлетворение", "вдохновение", "надежда", "ценность", "расставание", "разговор",
            "трудность", "путь", "ощущение", "состояние", "влияние", "восстановление", "конфликт", "встреча", "умиротворение",
            "сердце", "дружеский", "грусть", "нужда", "воспоминание", "улыбка", "спокойствие", "благодарность", "преданность",
            "независимость", "совместность", "общение", "чувствительность", "испытывать", "сожаление", "объятие", "интуиция",
            "доверие", "отношения", "семейный", "гармония", "мудрость", "любовь", "дружеские", "ответственность", "признание",
            "восхищение", "влияние", "поиск", "сплочение", "согласие", "искренность", "страсть", "откровенность", "разделение",
            "счастливые", "сплоченность", "горе", "согласованность", "участие", "близкие", "благо", "эмпатия", "благодарность",
            "цель", "влияние", "поддержка", "чувствительность", "понимание", "безопасность", "слушать", "устойчивость", "память",
            "терпимость", "увековечение", "покровительство", "согласие", "понимание", "сострадание", "поступок", "влияние",
            "честность", "уход", "счастье", "эмоции", "нежность", "доверие", "понимание", "решение", "гордость", "партнёрство",
            "любовь", "семейность", "отношения", "пожелания", "взаимодействие", "пристрастие", "разделение", "обязанности",
            "сострадание", "слабость", "сила", "интуиция", "союз", "сохранение", "согласие", "взаимопонимание"
        )

        val selectedWords = mutableListOf<String>()
        selectedWords.addAll(natureWords)
        selectedWords.addAll(technologyWords)
        selectedWords.addAll(relationshipWords)

        binding.toggleNature.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(natureWords)
            } else {
                selectedWords.removeAll(natureWords)
            }
        }

        binding.toggleTechnology.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(technologyWords)
            } else {
                selectedWords.removeAll(technologyWords)
            }
        }

        binding.toggleRelationships.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedWords.addAll(relationshipWords)
            } else {
                selectedWords.removeAll(relationshipWords)
            }
        }


        // Обработчик для генерации случайного слова
        generateWordButton.setOnClickListener {
            if (selectedWords.isNotEmpty()) {                       // Если есть доступные слова, генерируем случайное
                val randomIndex = selectedWords.indices.random()    // Получаем случайный индекс
                val randomWord = selectedWords[randomIndex]       // Получаем слово по индексу
                selectedWords.removeAt(randomIndex)               // Удаляем слово по индексу
                randomWordText.text = randomWord.replaceFirstChar { it.uppercase() }
            } else {
                // val count = relationshipWords.size
                randomWordText.text = "Слова закончились!"
            }
        }
    }
}

