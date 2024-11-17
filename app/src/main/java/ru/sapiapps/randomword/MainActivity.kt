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
            "гриб", "земля", "песчаник", "сирень", "дерево", "жук", "капля", "полёт", "поляна",
            "животное", "скала", "сумрак", "рябина", "погода", "лёд", "туча", "шторм", "осётр",
            "свет", "восход", "запах", "весна", "период", "цвет", "простор", "вечер", "мороз",
            "прилив", "край", "голубь", "долина", "небо", "гребень", "лист", "ручей", "пруд",
            "роза", "рельеф", "туман", "гладь", "крест", "сила", "глубина", "пещера", "щука",
            "космос", "обрыв", "луч", "вода", "рассвет", "порох", "лосось", "осина", "бор", "русло",
            "источник", "каньон", "сурок", "осень", "цветение", "дуга", "дождик",
            "лодка", "олень", "падение", "тропинка", "жёлудь", "петух", "гроза", "кедр", "жизнь",
            "кипарис", "сельдь", "черепаха", "лоза", "копытце", "борозда", "тропа", "полёвка",
            "стрела", "кувшинка", "пахарь", "каштан", "папоротник", "листик", "пчела", "лаванда",
            "тополь", "лён", "вьюн", "вихрь", "ягода", "берёза", "зарево", "пёс", "поросль",
            "хлопок", "бамбук", "коряга", "пальма", "шмель", "широта", "долгота", "корень", "серпантин",
            "бугор", "град", "попугай", "посох", "аллея", "цапля", "бабочка", "пламя", "перо",
            "щавель", "грибок", "муравей", "сойка", "флора", "закат", "плотина", "топот",
            "пожар", "седина", "заря", "сельце", "шишка", "мертвая", "камыш", "мох", "фауна", "камнепад",
            "паутина", "побережье", "разлив", "популяция", "парк", "скворечник",
            "папа", "липка", "кочка", "век", "карп", "лук", "ель", "мука", "покров",
            "север", "песчаная", "горох", "море", "мелодия", "светильник",
            "петр", "паук", "кукушкин", "песня", "берег", "труд", "юг", "восток"
        )

        val technologyWords = mutableListOf(
            "компьютер", "интернет", "робот", "смартфон", "дрон", "квант", "код", "платформа", "сеть", "алгоритм",
            "нейросеть", "бит", "программирование", "цифровизация", "система", "антивирус", "сигнал", "модем",
            "кодировка", "протокол", "данные", "облако", "гаджет", "хакер", "веб", "обновление", "объект", "секунда",
            "тест", "запуск", "дебаг", "класс", "вопрос", "подключение", "конфигурация", "доступ", "игра", "программа",
            "сборка", "модуль", "файл", "сигнализация", "работа", "компиляция", "разработка", "сервера",
            "проект", "партнер", "дисплей", "параметр", "контроль", "метод", "сетевой", "слой", "разделение", "вход",
            "выход", "разрешение", "виртуальный", "копия", "скрипт", "папка", "приложение", "вычисления", "устройство",
            "терминал", "механизм", "экран", "сетка", "график", "основной", "память", "планшет", "клавиатура",
            "модель", "разрешение", "битрейт", "вебсайт", "технология", "документ",
            "функция", "обратная связь", "скорость", "передача", "браузер", "провайдер",
            "ресурс", "анализ", "отладка", "архитектура", "масштаб", "схема", "папка", "архив", "расширение",
            "поток", "операция", "удаление", "пользователь", "команда", "основы", "обработка", "тестирование",
            "подключение", "параметры", "мобильность", "производительность", "аппарат", "структура", "порт",
            "удалённый", "каталог", "синхронизация", "защита", "функционал", "обработчик",
            "конфиденциальность", "программное", "обеспечение", "перезагрузка", "кэш", "комплекс",
            "операционная", "эмулятор", "установка", "модификация", "карта", "отчёт",
            "драйвер", "архиватор", "прокси", "размер", "команда",
            "данные", "логика", "вкладка", "страница", "параметр", "модернизация",
            "разрешение", "резервное", "копирование", "отключение", "инсталляция",
            "оптимизация", "калькулятор", "алгоритмизация", "анализатор", "синхронизация", "проектирование",
            "интерфейс", "профиль", "рабочий", "среда", "планирование", "основание","ИИ"
        )

            val relationshipWords = mutableListOf(
            "дружба", "семья", "поддержка", "сочувствие", "близость", "уважение", "помощь", "эмпатия",
            "взаимопонимание", "счастье", "забота", "согласие", "соглашение", "сопереживание", "совместимость", "лояльность",
            "открытость", "тепло", "объятия", "искренность", "прощение", "комфорт", "радость", "партнёр", "союз", "взаимопомощь",
            "дружелюбие", "чувство", "удовлетворение", "вдохновение", "надежда", "ценность", "расставание", "разговор",
            "трусость", "путь", "ощущение", "состояние", "влияние", "восстановление", "конфликт", "встреча", "умиротворение",
            "сердце", "дружеский", "грусть", "нужда", "воспоминание", "улыбка", "спокойствие", "благодарность", "преданность",
            "независимость", "совместность", "общение", "чувствительность", "сожаление", "интуиция",
            "доверие", "отношения", "гармония", "мудрость", "ответственность", "признание",
            "восхищение", "поиск", "сплочение", "страсть", "откровенность", "разделение",
            "сплочённость", "горе", "согласованность", "участие", "близкие", "благо",
            "цель", "чувствительность", "понимание", "безопасность", "слушать", "устойчивость", "память",
            "терпимость", "увековечение", "покровительство", "поступок",
            "честность", "уход", "эмоции", "нежность", "решение", "гордость", "партнёрство",
            "любовь", "пожелания", "взаимодействие", "пристрастие", "разделение", "обязанности",
            "сострадание", "слабость", "сила", "интуиция", "сохранение", "взаимовыручка"
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

