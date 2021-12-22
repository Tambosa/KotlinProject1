package com.aroman.kotlinproject1.view

import android.content.SharedPreferences
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.simpleSnack(text: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, length).show()
}

fun <T> SharedPreferences.set(key: String, value: T) {
    edit().apply {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
        }
    }.apply()
}

inline fun <reified T> SharedPreferences.get(key: String, default: T = defaultValue()) =
    when(default) {
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Long -> getLong(key, default) as T
        else -> throw UnsupportedOperationException("Type ${T::class} is not supported yet")
}

inline fun <reified T> defaultValue(): T =
    when (T::class) {
        String::class -> "" as T
        Int::class -> 0 as T
        Boolean::class -> false as T
        Float::class -> 0F as T
        Long::class -> 0L as T
        else -> {throw IllegalArgumentException("Default value not found")}
    }
