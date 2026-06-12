package com.nilac.zebra.znotificationssamplelistener.model

data class NotificationRecord(
    val key: String,
    val packageName: String,
    val appLabel: String,
    val title: String?,
    val text: String?,
    val postTime: Long,
    val removed: Boolean = false,
)
