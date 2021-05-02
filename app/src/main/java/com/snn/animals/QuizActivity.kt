package com.snn.animals

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class QuizActivity : AppCompatActivity(), IClickListener {
    val animals = ArrayList<Animal>()

    private var score = 0
    private var indexOfQuestion = 0
    private var positionOfAnswer = -1
    private var mediaPlayer: MediaPlayer? = null
    private var answerSize = listOf(2, 2, 2, 3, 3, 3, 4, 4, 4, 6, 6, 6, 8, 8, 8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setup()
    }

    private fun setup() {
        animals.add(Animal("Bee", R.drawable.ic_bee, R.raw.bee))
        animals.add(Animal("Bird", R.drawable.ic_bullfinch, R.raw.robin))
        animals.add(Animal("Cat", R.drawable.ic_cat, R.raw.cat))
        animals.add(Animal("Cow", R.drawable.ic_cow, R.raw.cow))
        animals.add(Animal("Dog", R.drawable.ic_dog, R.raw.dog))
        animals.add(Animal("Duck", R.drawable.ic_platypus, R.raw.duck))
        animals.add(Animal("Elephant", R.drawable.ic_elephant, R.raw.elephant))
        animals.add(Animal("Frog", R.drawable.ic_frog, R.raw.frog))
        animals.add(Animal("Horse", R.drawable.ic_horse, R.raw.horse))
        animals.add(Animal("Lion", R.drawable.ic_lion, R.raw.lion))
        animals.add(Animal("Owl", R.drawable.ic_owl, R.raw.owl))
        animals.add(Animal("Sheep", R.drawable.ic_sheep, R.raw.sheep))
        animals.add(Animal("Gorilla", R.drawable.ic_gorilla, R.raw.monkey))
        animals.add(Animal("Whale", R.drawable.ic_whale, R.raw.whale))
        animals.add(Animal("Hippo", R.drawable.ic_hippo, R.raw.hippo))
        animals.add(Animal("Flamingo", R.drawable.ic_flamingo, R.raw.flamingo))
        animals.add(Animal("Parrot", R.drawable.ic_parrot, R.raw.parrot))
        animals.add(Animal("Pig", R.drawable.ic_pig, R.raw.pig))
        animals.add(Animal("Chicken", R.drawable.ic_chicken, R.raw.chicken))
        animals.add(Animal("Bat", R.drawable.ic_bat, R.raw.bat))

        positionOfAnswer = createQuestion(answerSize[indexOfQuestion])
    }

    private fun createQuestion(sizeOfAnswer: Int): Int {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val selected = mutableSetOf<Animal>()
        while (selected.size < sizeOfAnswer) {
            val item = animals.random()
            selected.add(item)
        }

        val answers = arrayListOf<Animal>()
        answers.addAll(selected)

        val adapter = AnimalAdapter(answers, this)
        recyclerView.adapter = adapter

        val correctAnswer = answers.random()
        val indexOfCorrectAnswer = answers.indexOf(correctAnswer)

        playSound(R.raw.question)
        mediaPlayer?.setOnCompletionListener {
            onStop()
            playSound(correctAnswer.sound)
        }

        return indexOfCorrectAnswer
    }

    private fun playSound(sound: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, sound)
            mediaPlayer!!.start()
        } else mediaPlayer!!.start()
    }

    override fun onStop() {
        super.onStop()

        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun writeToFile(point: Int) {
        val sharedPref = this.getSharedPreferences("score_file", Activity.MODE_PRIVATE)
        val highScore = sharedPref!!.getInt("score", 0)

        if (point > highScore) {
            val editor = sharedPref.edit()
            editor.putInt("score", point)
            editor.apply()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun listener(position: Int) {
        if (position == positionOfAnswer) {
            onStop()
            playSound(R.raw.true_answer)

            indexOfQuestion += 1
            if (indexOfQuestion < answerSize.size) {
                mediaPlayer?.setOnCompletionListener {
                    onStop()
                    positionOfAnswer = createQuestion(answerSize[indexOfQuestion])
                    score += answerSize[indexOfQuestion]
                    findViewById<TextView>(R.id.score).text = "Score: $score"
                }
            } else {
                writeToFile(score)
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else {
            onStop()
            playSound(R.raw.false_answer)
            Thread.sleep(1_000)
            writeToFile(score)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}