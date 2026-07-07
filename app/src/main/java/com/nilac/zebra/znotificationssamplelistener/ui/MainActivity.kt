package com.nilac.zebra.znotificationssamplelistener.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
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

    private val mainViewModel: MainViewModel by viewModels()

    private val adapter = NotificationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        applyWindowInsets()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                NotificationStore.records.collect(adapter::submitList)
            }
        }

        mainViewModel.bindNotificationListenerResult.observe(this) {
            val result = it.contentIfNotHandled ?: return@observe
            if (!result) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to acquire Manage External Storage permission",
                    Toast.LENGTH_LONG
                ).show()
            }
            updateAccessStatus()
        }

        if (!isBindingNotificationListenerGranted()) {
            mainViewModel.preGrantNotificationListenerPermission()
        } else {
            updateAccessStatus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.bindNotificationListenerResult.removeObservers(this)
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
        val granted = isBindingNotificationListenerGranted()

        binding.statusText.setText(
            if (granted) R.string.status_granted else R.string.status_denied
        )
        val bg = if (granted) R.color.banner_granted_bg else R.color.banner_denied_bg
        val fg = if (granted) R.color.banner_granted_text else R.color.banner_denied_text
        binding.statusText.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, bg))
        binding.statusText.setTextColor(ContextCompat.getColor(this, fg))
    }

    private fun isBindingNotificationListenerGranted(): Boolean {
        return NotificationManagerCompat
            .getEnabledListenerPackages(this)
            .contains(packageName)
    }
}
