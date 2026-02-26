package com.example.hfc_keyboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var switchHfcMode: Switch
    private lateinit var switchSound: Switch
    private lateinit var switchVibration: Switch
    private lateinit var switchTheme: Switch
    private lateinit var btnEnableKeyboard: Button
    private lateinit var btnSelectKeyboard: Button
    private lateinit var btnReset: Button
    private lateinit var txtPreview: TextView
    private lateinit var layoutPreview: LinearLayout

    companion object {
        const val PREFS_NAME = "com.example.hfc_keyboard_prefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        initViews()
        loadSettings()
        setupListeners()
        updatePreview()
    }

    private fun initViews() {
        switchHfcMode = findViewById(R.id.switchHfcMode)
        switchSound = findViewById(R.id.switchSound)
        switchVibration = findViewById(R.id.switchVibration)
        switchTheme = findViewById(R.id.switchTheme)
        btnEnableKeyboard = findViewById(R.id.btnEnableKeyboard)
        btnSelectKeyboard = findViewById(R.id.btnSelectKeyboard)
        btnReset = findViewById(R.id.btnReset)
        txtPreview = findViewById(R.id.txtPreview)
        layoutPreview = findViewById(R.id.layoutPreview)
    }

    private fun loadSettings() {
        switchHfcMode.isChecked = sharedPrefs.getBoolean("hfcModeEnabled", true)
        switchSound.isChecked = sharedPrefs.getBoolean("keypressSoundEnabled", true)
        switchVibration.isChecked = sharedPrefs.getBoolean("keypressVibrationEnabled", true)
        switchTheme.isChecked = sharedPrefs.getBoolean("isDarkTheme", true)
    }

    private fun setupListeners() {
        switchHfcMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("hfcModeEnabled", isChecked).apply()
        }

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("keypressSoundEnabled", isChecked).apply()
        }

        switchVibration.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("keypressVibrationEnabled", isChecked).apply()
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("isDarkTheme", isChecked).apply()
            updateTheme()
        }

        btnEnableKeyboard.setOnClickListener {
            openInputMethodSettings()
        }

        btnSelectKeyboard.setOnClickListener {
            showInputMethodPicker()
        }

        btnReset.setOnClickListener {
            showResetDialog()
        }
    }

    private fun openInputMethodSettings() {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Settings not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInputMethodPicker() {
        try {
            val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imeManager.showInputMethodPicker()
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot show keyboard picker", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Reset Settings")
            .setMessage("Are you sure you want to reset all settings to default?")
            .setPositiveButton("Reset") { _, _ ->
                sharedPrefs.edit()
                    .clear()
                    .apply()
                loadSettings()
                Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updatePreview() {
        val hfcEnabled = switchHfcMode.isChecked
        txtPreview.text = if (hfcEnabled) {
            "HELLO → | = < < ()\nWORLD → & () ® < ÷"
        } else {
            "HFC Mode Disabled\nNormal typing will work"
        }
    }

    private fun updateTheme() {
        // Theme update logic can be added here
        recreate()
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
        updatePreview()
    }
}
