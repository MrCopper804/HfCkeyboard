package com.example.hfc_keyboard

object HFCKeyboard {
    // HFC Cipher Mapping - 1-to-1 character to symbol replacement
    private val cipherMap = mapOf(
        'A' to "+",
        'B' to "[]",
        'C' to "©",
        'D' to "÷",
        'E' to "=",
        'F' to "_",
        'G' to ">",
        'H' to "|",
        'I' to "!",
        'J' to "#",
        'K' to ":",
        'L' to "<",
        'M' to "*",
        'N' to "°",
        'O' to "()",
        'P' to "¶",
        'Q' to "?",
        'R' to "®",
        'S' to "$",
        'T' to "™",
        'U' to "'",
        'V' to "^",
        'W' to "&",
        'X' to "`",
        'Y' to "~",
        'Z' to "℅"
    )

    /**
     * Encode a single character to its HFC symbol equivalent
     */
    fun encodeCharacter(char: Char): String {
        val upperChar = char.uppercaseChar()
        return cipherMap[upperChar] ?: char.toString()
    }

    /**
     * Encode entire text to HFC symbols
     */
    fun encodeText(text: String): String {
        return text.map { encodeCharacter(it) }.joinToString("")
    }

    /**
     * Get the length of encoded symbol (for backspace handling)
     */
    fun getEncodedLength(char: Char): Int {
        return encodeCharacter(char).length
    }

    /**
     * Check if a character has an HFC mapping
     */
    fun hasMapping(char: Char): Boolean {
        return cipherMap.containsKey(char.uppercaseChar())
    }

    /**
     * Get all HFC mappings for display
     */
    fun getCipherMappings(): Map<Char, String> {
        return cipherMap
    }
}
