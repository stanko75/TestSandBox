package com.example.testsandbox

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SeparateClassReceiverExample : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val msg = intent?.getStringExtra("message")

        val tickIntent = Intent(IntentGlobalActions.UI_UPDATE)
        tickIntent.setPackage(context.packageName)
        tickIntent.putExtra("message", msg)
        context.sendBroadcast(tickIntent)
    }
}