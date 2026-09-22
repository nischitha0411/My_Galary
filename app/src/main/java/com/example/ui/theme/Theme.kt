package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ScrapbookCoral,
    secondary = ScrapbookGold,
    tertiary = ScrapbookSage,
    background = Color(0xFF1E1712),
    surface = Color(0xFF2B2018),
    onPrimary = Color.White,
    onSecondary = Color(0xFF281E15),
    onBackground = Color(0xFFF3EDE3),
    onSurface = Color(0xFFF3EDE3)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ScrapbookTerracotta,
    secondary = ScrapbookGold,
    tertiary = ScrapbookSage,
    background = ScrapbookParchment,
    surface = ScrapbookParchmentLight,
    onPrimary = Color.White,
    onSecondary = Color(0xFF332014),
    onBackground = ScrapbookTextDark,
    onSurface = ScrapbookTextDark
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Use our bespoke scrapbook color scheme by default to maintain consistent physical craft feel
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
