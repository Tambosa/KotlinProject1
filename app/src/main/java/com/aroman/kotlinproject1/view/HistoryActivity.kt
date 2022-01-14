package com.aroman.kotlinproject1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.model.LocalRepositoryImpl

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        findViewById<RecyclerView>(R.id.history_recycler_view).apply {
            adapter = HistoryAdapter(LocalRepositoryImpl(App.getHistoryDAO()).getAllHistory()).also {
                it.notifyDataSetChanged()
            }
        }
    }
}