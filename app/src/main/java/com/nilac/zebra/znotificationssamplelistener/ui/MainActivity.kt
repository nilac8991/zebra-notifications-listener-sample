package com.nilac.zebra.znotificationssamplelistener.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

        applyWindowInsets()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

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
        // Re-check on resume: the user may have toggled access while we were in Settings.
        updateAccessStatus()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val bars = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.updatePadding(left = bars.left, top = bars.top, right = bars.right)
            binding.recyclerView.updatePadding(bottom = bars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun updateAccessStatus() {
        val granted = NotificationManagerCompat
            .getEnabledListenerPackages(this)
            .contains(packageName)

        binding.statusText.setText(
            if (granted) R.string.status_granted else R.string.status_denied
        )
        val bg = if (granted) R.color.banner_granted_bg else R.color.banner_denied_bg
        val fg = if (granted) R.color.banner_granted_text else R.color.banner_denied_text
        binding.statusText.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, bg))
        binding.statusText.setTextColor(ContextCompat.getColor(this, fg))
    }
}
