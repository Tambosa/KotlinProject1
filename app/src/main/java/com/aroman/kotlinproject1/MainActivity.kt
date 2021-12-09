package com.aroman.kotlinproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.clickButton)
        textView = findViewById(R.id.text_view_number)
        data class MyClass(var Title: String, var number: Int)

        val my = MyClass("Test", 1)

        button.setOnClickListener {
            textView.text = my.number++.toString()
        }
    }
}