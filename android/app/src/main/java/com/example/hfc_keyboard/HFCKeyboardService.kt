package com.example.hfc_keyboard

import android.inputmethodservice.InputMethodService
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.graphics.Typeface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class HFCKeyboardService : InputMethodService() {

    private var vibrator: Vibrator? = null
    private var audioManager: AudioManager? = null
    private var keyboardView: LinearLayout? = null

    private var hfcModeEnabled = true
    private var keypressSoundEnabled = true
    private var keypressVibrationEnabled = true
    private var isShifted = false
    private var isCaps = false
    private var currentKeyboardMode = KeyboardMode.QWERTY

    companion object {
        const val PREFS_NAME = "com.example.hfc_keyboard_prefs"
    }

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
    }

    override fun onCreateInputView(): View {
        loadSettings()
        keyboardView = createKeyboardView()
        return keyboardView!!
    }

    override fun onStartInputView(attribute: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(attribute, restarting)
        loadSettings()
    }

    private fun loadSettings() {
        val sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        hfcModeEnabled = sharedPref.getBoolean("hfcModeEnabled", true)
        keypressSoundEnabled = sharedPref.getBoolean("keypressSoundEnabled", true)
        keypressVibrationEnabled = sharedPref.getBoolean("keypressVibrationEnabled", true)
    }

    private fun createKeyboardView(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0D0D0D"))
            setPadding(dpToPx(4), dpToPx(6), dpToPx(4), dpToPx(6))
            
            addView(createQwertyRow1())
            addView(createQwertyRow2())
            addView(createQwertyRow3())
            addView(createBottomRow())
        }
    }

    private fun createQwertyRow1(): LinearLayout {
        return createRow(listOf(
            KeyData("Q", 'Q'), KeyData("W", 'W'), KeyData("E", 'E'), KeyData("R", 'R'),
            KeyData("T", 'T'), KeyData("Y", 'Y'), KeyData("U", 'U'), KeyData("I", 'I'),
            KeyData("O", 'O'), KeyData("P", 'P')
        ))
    }

    private fun createQwertyRow2(): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
        }

        row.addView(createModifierKey(if (isCaps) "CAPS" else "SHIFT", KeyType.SHIFT, 1.5f))
        
        listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L').forEach { char ->
            row.addView(createLetterKey(char))
        }
        
        row.addView(createModifierKey("⌫", KeyType.DELETE, 1.5f))
        
        return row
    }

    private fun createQwertyRow3(): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
        }

        row.addView(createModifierKey("123", KeyType.NUMBERS, 1.2f))
        
        listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M').forEach { char ->
            row.addView(createLetterKey(char))
        }
        
        row.addView(createModifierKey("⌫", KeyType.DELETE, 1.2f))
        
        return row
    }

    private fun createBottomRow(): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
        }

        val symbolKeyText = when (currentKeyboardMode) {
            KeyboardMode.QWERTY -> "!#1"
            else -> "ABC"
        }
        row.addView(createModifierKey(symbolKeyText, KeyType.SYMBOLS, 1.2f))
        row.addView(createSpaceKey())
        row.addView(createModifierKey("↵", KeyType.ENTER, 1.2f))
        row.addView(createModifierKey("😀", KeyType.EMOJI, 1f))
        
        return row
    }

    private fun createRow(keys: List<KeyData>): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
            
            keys.forEach { keyData ->
                addView(createKeyButton(keyData.label, keyData.character, false))
            }
        }
    }

    private fun createKeyButton(label: String, char: Char, isModifier: Boolean): TextView {
        return TextView(this).apply {
            text = if (isShifted && !isCaps && char.isLetter()) char.uppercaseChar().toString() else label
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            
            val bg = GradientDrawable().apply {
                setColor(Color.parseColor(if (isModifier) "#1A1A1A" else "#00D9FF"))
                cornerRadius = dpToPx(6).toFloat()
            }
            background = bg
            
            layoutParams = LinearLayout.LayoutParams(
                0,
                dpToPx(50),
                1f
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
        }
    }

    private fun createLetterKey(char: Char): TextView {
        return createKeyButton(char.toString(), char, false).apply {
            setOnClickListener {
                handleCharacterInput(if (isShifted && !isCaps) char.uppercaseChar() else char.lowercaseChar())
                if (isShifted && !isCaps) {
                    isShifted = false
                    refreshKeyboard()
                }
            }
        }
    }

    private fun createModifierKey(label: String, type: KeyType, flex: Float): TextView {
        return TextView(this).apply {
            text = label
            textSize = 16f
            setTextColor(Color.parseColor("#00D9FF"))
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            
            val bg = GradientDrawable().apply {
                setColor(Color.parseColor("#1A1A1A"))
                cornerRadius = dpToPx(6).toFloat()
            }
            background = bg
            
            layoutParams = LinearLayout.LayoutParams(
                0,
                dpToPx(50),
                flex
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
            
            setOnClickListener {
                handleModifierKey(type)
            }
        }
    }

    private fun createSpaceKey(): TextView {
        return TextView(this).apply {
            text = " "
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            
            val bg = GradientDrawable().apply {
                setColor(Color.parseColor("#1A1A1A"))
                cornerRadius = dpToPx(6).toFloat()
            }
            background = bg
            
            layoutParams = LinearLayout.LayoutParams(
                0,
                dpToPx(50),
                3f
            ).apply {
                setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
            }
            
            setOnClickListener {
                handleCharacterInput(' ')
            }
        }
    }

    private fun handleModifierKey(type: KeyType) {
        when (type) {
            KeyType.SHIFT -> {
                isShifted = !isShifted
                isCaps = false
                refreshKeyboard()
            }
            KeyType.CAPS -> {
                isCaps = !isCaps
                isShifted = false
                refreshKeyboard()
            }
            KeyType.NUMBERS -> {
                currentKeyboardMode = if (currentKeyboardMode == KeyboardMode.QWERTY) 
                    KeyboardMode.NUMBERS else KeyboardMode.QWERTY
                refreshKeyboard()
            }
            KeyType.SYMBOLS -> {
                currentKeyboardMode = if (currentKeyboardMode == KeyboardMode.QWERTY) 
                    KeyboardMode.SYMBOLS else KeyboardMode.QWERTY
                refreshKeyboard()
            }
            KeyType.DELETE -> {
                handleBackspace()
            }
            KeyType.ENTER -> {
                currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                playFeedback()
            }
            KeyType.EMOJI -> {
                showEmojiPicker()
            }
            KeyType.SPACE -> {
                handleCharacterInput(' ')
            }
        }
    }

    private fun handleCharacterInput(char: Char) {
        val inputConnection = currentInputConnection ?: return
        
        val textToInsert = if (hfcModeEnabled && char.isLetter()) {
            HFCKeyboard.encodeCharacter(char)
        } else {
            char.toString()
        }
        
        inputConnection.commitText(textToInsert, 1)
        playFeedback()
    }

    private fun handleBackspace() {
        val inputConnection = currentInputConnection ?: return
        
        val selectedText = inputConnection.getSelectedText(0)
        if (selectedText != null && selectedText.isNotEmpty()) {
            inputConnection.commitText("", 1)
        } else {
            val textBeforeCursor = inputConnection.getTextBeforeCursor(10, 0)
            if (textBeforeCursor != null && textBeforeCursor.isNotEmpty()) {
                val deleteCount = when {
                    textBeforeCursor.endsWith("[]") -> 2
                    textBeforeCursor.endsWith("()") -> 2
                    else -> 1
                }
                repeat(deleteCount) {
                    inputConnection.deleteSurroundingText(1, 0)
                }
            }
        }
        playFeedback()
    }

    private fun showEmojiPicker() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showInputMethodPicker()
    }

    private fun refreshKeyboard() {
        keyboardView?.let {
            val parent = it.parent as? LinearLayout
            parent?.removeView(it)
            keyboardView = createKeyboardView()
            parent?.addView(keyboardView, 0)
        }
    }

    private fun playFeedback() {
        if (keypressSoundEnabled) {
            audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK)
        }
        if (keypressVibrationEnabled) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(50)
                }
            } catch (e: Exception) {
                // Ignore vibration errors
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator = null
        audioManager = null
        keyboardView = null
    }
}

enum class KeyType {
    SHIFT, CAPS, NUMBERS, SYMBOLS, DELETE, ENTER, EMOJI, SPACE
}

enum class KeyboardMode {
    QWERTY, NUMBERS, SYMBOLS
}

data class KeyData(val label: String, val character: Char)
