package com.getstoryteller.storytellersampleapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class StorytellerColors(
  val background: Color = Color.Unspecified
)

val LocalStorytellerColorsPalette = staticCompositionLocalOf { StorytellerColors() }

private val StorytellerLightColorPalette = StorytellerColors(
  background = Color(0xFFE5E5E5)
)

private val StorytellerDarkColorPalette = StorytellerColors(
  background = Color(0xFF1A1A1A)
)

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
  primary = Color(0xFF1C62EB),
  onPrimary = Color(0xFFFFFFFF),
  secondary = Color(0xFF1C62EB),
  background = Color(0xFF000000),
  surface = Color(0xFF0D0D0D),
  primaryVariant = Color(0xFF1A1A1A),
  secondaryVariant = Color(0xFFFFFFFF),
  onSurface = Color(0xB3FFFFFF),
  onBackground = Color(0xFFFFFFFF),
  error = Color(0xFFE21219)
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
  primary = Color(0xFF1C62EB),
  onPrimary = Color(0xFFFFFFFF),
  secondary = Color(0xFF1C62EB),
  surface = Color(0xFFF5F5F5),
  background = Color(0xFFFFFFFF),
  primaryVariant = Color(0xFFFFFFFF),
  secondaryVariant = Color(0xFF000000),
  onSurface = Color(0xB31A1A1A),
  onBackground = Color(0xFF1A1A1A),
  error = Color(0xFFE21219)
)

@Composable
fun SampleAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  val materialColors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  val storytellerColorsPalette = if (darkTheme) {
    StorytellerDarkColorPalette
  } else {
    StorytellerLightColorPalette
  }

  CompositionLocalProvider(LocalStorytellerColorsPalette provides storytellerColorsPalette) {
    MaterialTheme(
      content = content,
      colors = materialColors
    )
  }
}
