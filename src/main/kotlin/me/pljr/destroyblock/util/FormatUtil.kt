package me.pljr.destroyblock.util

import org.bukkit.ChatColor
import java.util.*
import kotlin.math.roundToInt

/**
 * Applies default [ChatColor] codes to String and returns it.
 *
 * @param string String that should get default ChatColor applied
 * @return Colored string
 */
fun colorString(string: String): String {
    return ChatColor.translateAlternateColorCodes('&', string)
}

/**
 * Returns a [String] as a progress bar.
 *
 * @param current Current amount of progress.
 * @param max Maximum amount of progress.
 * @param symbol Symbol, that will represent the bar.
 * @param lockedColor Color of unfinished progress.
 * @param unlockedColor Color of finished progress.
 * @return [String] that will represent the progress bar.
 */
fun createProgressBar(current: Float, max: Float, symbol: String, lockedColor: String, unlockedColor: String): String {
    val onePercent = max / 100
    val percent = current / onePercent
    val unlocked = (percent / 10).roundToInt()
    val progress = StringBuilder()
    for (i in 1..10) {
        progress.append(symbol)
    }
    progress.insert(unlocked, lockedColor)
    progress.insert(0, unlockedColor)
    return colorString(progress.toString())
}