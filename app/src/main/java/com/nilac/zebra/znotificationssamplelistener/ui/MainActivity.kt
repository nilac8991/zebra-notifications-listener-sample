package com.nilac.zebra.znotificationssamplelistener.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilac.zebra.znotificationssamplelistener.R
import com.nilac.zebra.znotificationssamplelistener.data.NotificationStore
import com.nilac.zebra.znotificationssamplelistener.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = NotificationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        binding.openAccessButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                NotificationStore.records.collect(adapter::submitList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateAccessStatus()
    }

    private fun updateAccessStatus() {
        val granted = NotificationManagerCompat
            .getEnabledListenerPackages(this)
            .contains(packageName)
        binding.statusText.setText(
            if (granted) R.string.status_granted else R.string.status_denied
        )
    }
}
