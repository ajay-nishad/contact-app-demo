package com.example.demo.extension

import android.app.Activity
import android.util.Log
import android.widget.Toast


fun Activity.ShowToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.ShowLog() {
    Log.d("Log ", this)
}