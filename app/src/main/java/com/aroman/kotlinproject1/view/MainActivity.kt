package com.aroman.kotlinproject1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment, MainFragment.newInstance())
            .commit()
    }
}