import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private lateinit var btnChoice1: MaterialButton
    private lateinit var btnChoice2: MaterialButton
    private lateinit var btnChoice3: MaterialButton
    private lateinit var btnChoice4: MaterialButton
    private lateinit var tvQuestion: TextView
    private lateinit var currentQuestion: Question
    private lateinit var questionList: ArrayList<Question>
    private var questionCounter = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()

        questionList = ArrayList()
        getQuizList().forEach {
            questionList.add(it)
        }


        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<LinearProgressIndicator>(R.id.prog).progress =
                    (millisUntilFinished / 100).toInt()
            }

            override fun onFinish() {
                checkAnswer(null)
            }
        }

        showNextQuestion()

        btnChoice1.setOnClickListener {
            checkAnswer(it)
        }
        btnChoice2.setOnClickListener {
            checkAnswer(it)
        }
        btnChoice3.setOnClickListener {
            checkAnswer(it)
        }
        btnChoice4.setOnClickListener {
            checkAnswer(it)
        }

    }

    private fun showNextQuestion() {
        setBtnEnabledTo(true)

        if (questionCounter < questionList.size) {
            timer.start()

            currentQuestion = questionList[questionCounter]
            shuffleChoices(currentQuestion)

            tvQuestion.text = currentQuestion.question
            btnChoice1.text = currentQuestion.choice1
            btnChoice2.text = currentQuestion.choice2
            btnChoice3.text = currentQuestion.choice3
            btnChoice4.text = currentQuestion.choice4

            questionCounter++
        } else {
            Toast.makeText(this, "finish score: $score", Toast.LENGTH_SHORT).show()
            score = 0
            questionCounter = 0
            //showNextQuestion()
            // Quiz finished
            // Show a result screen or perform any other action
        }
    }

    private fun checkAnswer(btn: View?) {
        timer.cancel()
        setBtnEnabledTo(false)
        if (btn != null && (btn as MaterialButton).text.toString() == currentQuestion.correctChoice) {
            // Correct answer
            // Perform any action you want
            score++
            btn.setBackgroundColor(resources.getColor(R.color.green))
        } else {
            // Incorrect answer
            // Perform any action you want
            btn?.setBackgroundColor(resources.getColor(R.color.red))

        }
        showCorrect()
        next()
    }

    private fun next() {
        CoroutineScope(Main).launch {
            delay(1000)
            resetBtnColors()
            showNextQuestion()
        }
    }

    private fun setBtnEnabledTo(b: Boolean) {
        btnChoice1.isEnabled = b
        btnChoice2.isEnabled = b
        btnChoice3.isEnabled = b
        btnChoice4.isEnabled = b
    }

    // btn color set to default for next quiz
    private fun resetBtnColors() {
        btnChoice1.setBackgroundColor(0)
        btnChoice2.setBackgroundColor(0)
        btnChoice3.setBackgroundColor(0)
        btnChoice4.setBackgroundColor(0)
    }

    // Change Correct btn color to Green
    private fun showCorrect() {
        when (currentQuestion.correctChoice) {
            btnChoice1.text -> btnChoice1.setBackgroundColor(resources.getColor(R.color.green))
            btnChoice2.text -> btnChoice2.setBackgroundColor(resources.getColor(R.color.green))
            btnChoice3.text -> btnChoice3.setBackgroundColor(resources.getColor(R.color.green))
            btnChoice4.text -> btnChoice4.setBackgroundColor(resources.getColor(R.color.green))
        }
    }

    private fun setupViews() {
        tvQuestion = findViewById(R.id.tv_text)
        btnChoice1 = findViewById(R.id.btnChoice1)
        btnChoice2 = findViewById(R.id.btnChoice2)
        btnChoice3 = findViewById(R.id.btnChoice3)
        btnChoice4 = findViewById(R.id.btnChoice4)
    }


    private fun shuffleChoices(question: Question) {
        val choiceList = mutableListOf<String>()
        choiceList.add(0, question.choice1)
        choiceList.add(1, question.choice2)
        choiceList.add(2, question.choice3)
        choiceList.add(3, question.choice4)
        choiceList.shuffle()
        question.choice1 = choiceList[0]
        question.choice2 = choiceList[1]
        question.choice3 = choiceList[2]
        question.choice4 = choiceList[3]

    }

    override fun onPause() {
        super.onPause()
        checkAnswer(null)
    }


    //Generate Fake Question
    private fun getQuizList(): List<Question> {
        val qu = mutableListOf<Question>()

        qu.apply {
            add(
                Question(
                    0,
                    "شهرستان فسا در کدام استان قرار دارد؟",
                    "بوشهر",
                    "فارس",
                    "اردبیل",
                    "سمنان",
                    "2",
                )
            )
            add(
                Question(
                    1,
                    "امام هفتم شیعیان کدام است؟",
                    "هادی",
                    "مهدی",
                    "کاظم",
                    "باقر",
                    "3",
                )
            )

            add(
                Question(
                    1,
                    "نماد شیمیایی عنصر مس کدام گزینه است؟",
                    "Fe",
                    "Co",
                    "Hg",
                    "Cu",
                    "4",
                )
            )
        }
        //replace correctChoice from index to relative field content
        qu.forEachIndexed { i, question ->
            when (question.correctChoice) {
                "1" -> qu[i].correctChoice = qu[i].choice1
                "2" -> qu[i].correctChoice = qu[i].choice2
                "3" -> qu[i].correctChoice = qu[i].choice3
                "4" -> qu[i].correctChoice = qu[i].choice4
            }
        }


        return qu
    }

}

data class Question(
    var id: Int = 0,
    var question: String = "",
    var choice1: String = "",
    var choice2: String = "",
    var choice3: String = "",
    var choice4: String = "",
    var correctChoice: String = ""
)
