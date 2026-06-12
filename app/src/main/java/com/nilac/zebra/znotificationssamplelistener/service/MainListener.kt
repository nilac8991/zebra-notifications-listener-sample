package com.nilac.zebra.znotificationssamplelistener.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class MainListener : NotificationListenerService() {

    override fun onListenerConnected() {
        Log.d(TAG, "Listener connected")
        activeNotifications?.forEach { logNotification("active", it) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        logNotification("posted", sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        logNotification("removed", sbn)
    }

    private fun logNotification(event: String, sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        Log.d(
            TAG,
            "$event | pkg=${sbn.packageName} | title=$title | text=$text | postTime=${sbn.postTime} | key=${sbn.key}"
        )
    }

    private companion object {
        const val TAG = "MainListener"
    }
}
