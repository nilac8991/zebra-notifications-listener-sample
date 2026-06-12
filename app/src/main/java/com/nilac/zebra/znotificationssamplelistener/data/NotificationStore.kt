package com.nilac.zebra.znotificationssamplelistener.data

import com.nilac.zebra.znotificationssamplelistener.model.NotificationRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NotificationStore {

    private val _records = MutableStateFlow<List<NotificationRecord>>(emptyList())
    val records: StateFlow<List<NotificationRecord>> = _records.asStateFlow()

    // Insert a freshly posted notification, replacing any existing entry with the same key
    fun upsert(record: NotificationRecord) = _records.update { current ->
        (listOf(record) + current.filterNot { it.key == record.key })
            .sortedByDescending { it.postTime }
    }

    // Mark the entry with [key] as removed, keeping it visible in the list. No-op if absent
    fun markRemoved(key: String) = _records.update { current ->
        current.map { if (it.key == key) it.copy(removed = true) else it }
    }
}
