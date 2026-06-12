package com.nilac.zebra.znotificationssamplelistener.service

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.nilac.zebra.znotificationssamplelistener.data.NotificationStore
import com.nilac.zebra.znotificationssamplelistener.model.NotificationRecord


class MainListener : NotificationListenerService() {

    override fun onListenerConnected() {
        Log.d(TAG, "Listener connected")
        activeNotifications?.forEach { NotificationStore.upsert(it.toRecord()) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d(TAG, "posted | ${sbn.key}")
        NotificationStore.upsert(sbn.toRecord())
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d(TAG, "removed | ${sbn.key}")
        NotificationStore.markRemoved(sbn.key)
    }

    private fun StatusBarNotification.toRecord(): NotificationRecord {
        val extras = notification.extras
        return NotificationRecord(
            key = key,
            packageName = packageName,
            appLabel = extractAppLabel(packageName),
            title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString(),
            text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString(),
            postTime = postTime,
        )
    }

    /** Human-readable app name, falling back to the package name if it can't be resolved. */
    private fun extractAppLabel(packageName: String): String = try {
        val info = this.packageManager.getApplicationInfo(packageName, 0)
        this.packageManager.getApplicationLabel(info).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName
    }

    private companion object {
        const val TAG = "MainListener"
    }
}
