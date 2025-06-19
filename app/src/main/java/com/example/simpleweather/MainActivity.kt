package com.example.simpleweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simpleweather.databinding.ActivityMainBinding
import com.example.simpleweather.ui.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)

        // Add WelcomeFragment if this is the first launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WelcomeFragment())
                .commit()
        }
    }
}