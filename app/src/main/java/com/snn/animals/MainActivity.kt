package com.snn.animals

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    @SuppressLint("SetTextI18n")
    private fun setup() {
        findViewById<Button>(R.id.buttonStart).setOnClickListener(this)

        val highScore = findViewById<TextView>(R.id.high_score)

        val sharedPref = getSharedPreferences("score_file", Activity.MODE_PRIVATE)
        val score = sharedPref!!.getInt("score", 0)

        highScore.text = "High Score: $score"
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonStart -> {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}