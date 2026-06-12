package com.nilac.zebra.znotificationssamplelistener.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nilac.zebra.znotificationssamplelistener.databinding.ActivityMainBinding

/**
 * Single host activity. P1 keeps this intentionally bare — the real work happens in
 * [com.nilac.zebra.znotificationssamplelistener.service.MainListener] and is observed via Logcat.
 * P2 will add the status check, the "Open Notification access" button, and the live list.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
