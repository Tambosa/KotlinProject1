package com.aroman.kotlinproject1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.model.LocalRepositoryImpl

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView = findViewById<RecyclerView>(R.id.history_recycler_view).apply {
            adapter =
                HistoryAdapter(LocalRepositoryImpl(App.getHistoryDAO()).getAllHistory()).also {
                    it.notifyDataSetChanged()
                }
        }

        findViewById<SearchView>(R.id.history_search).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(word: String?): Boolean {
                    clearFocus()
                    recyclerView.adapter =
                        HistoryAdapter(LocalRepositoryImpl(App.getHistoryDAO()).getHistoryByCity("%${word.toString()}%")).also {
                            it.notifyDataSetChanged()
                        }
                    return false
                }

                override fun onQueryTextChange(word: String?): Boolean {
                    recyclerView.adapter =
                        HistoryAdapter(LocalRepositoryImpl(App.getHistoryDAO()).getHistoryByCity("%${word.toString()}%")).also {
                            it.notifyDataSetChanged()
                        }
                    return false
                }
            })
        }
    }
}