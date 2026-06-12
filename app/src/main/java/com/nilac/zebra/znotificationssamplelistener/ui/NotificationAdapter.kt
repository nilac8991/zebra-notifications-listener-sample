package com.nilac.zebra.znotificationssamplelistener.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nilac.zebra.znotificationssamplelistener.databinding.ItemNotificationBinding
import com.nilac.zebra.znotificationssamplelistener.model.NotificationRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter :
    ListAdapter<NotificationRecord, NotificationAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: NotificationRecord) = with(binding) {
            appLabel.text = record.appLabel
            packageName.text = record.packageName
            notificationTitle.text = record.title
            notificationText.text = record.text
            postTime.text = TIME_FORMAT.format(Date(record.postTime))

            removedTag.visibility = if (record.removed) ViewGroup.VISIBLE else ViewGroup.GONE
            val alpha = if (record.removed) 0.5f else 1f
            root.alpha = alpha
            appLabel.strike(record.removed)
            notificationTitle.strike(record.removed)
        }

        private fun TextView.strike(on: Boolean) {
            paintFlags = if (on) {
                paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    private companion object {
        private val TIME_FORMAT = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        private val DIFF = object : DiffUtil.ItemCallback<NotificationRecord>() {
            override fun areItemsTheSame(old: NotificationRecord, new: NotificationRecord) =
                old.key == new.key

            override fun areContentsTheSame(old: NotificationRecord, new: NotificationRecord) =
                old == new
        }
    }
}
