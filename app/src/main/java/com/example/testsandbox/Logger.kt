package com.example.testsandbox

import android.view.View
import android.widget.ScrollView
import android.widget.TextView


class Logger: ILogger {

    lateinit var scrollLog: ScrollView
    lateinit var tvLog: TextView
    var maxLines = 500

    override fun log(message: String) {
        tvLog.append("\n$message")

        val lines = tvLog.text.split("\n")
        if (lines.size > maxLines) {
            val trimmed = lines.takeLast(maxLines).joinToString("\n")
            tvLog.text = trimmed
        }

        scrollLog.post {
            scrollLog.fullScroll(View.FOCUS_DOWN)
        }
    }
}