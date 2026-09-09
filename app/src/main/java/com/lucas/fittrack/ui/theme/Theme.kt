package com.lucas.fittrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF0B3D12),

    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFC8E6C9),

    secondary = Color(0xFF90A4AE),
    onSecondary = Color(0xFF1C2529),

    background = Color(0xFF101410),
    onBackground = Color(0xFFE6E6E6),

    surface = Color(0xFF181C18),
    onSurface = Color(0xFFE6E6E6),

    surfaceVariant = Color(0xFF2A302A),
    onSurfaceVariant = Color(0xFFB8BDB8),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF0B3D12),

    secondary = Color(0xFF546E7A),
    onSecondary = Color.White,

    background = Color(0xFFF7F9F7),
    onBackground = Color(0xFF1B1B1B),

    surface = Color.White,
    onSurface = Color(0xFF1B1B1B),

    surfaceVariant = Color(0xFFE8ECE8),
    onSurfaceVariant = Color(0xFF5F6360),

    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun FitTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = FitTrackShapes,
        content = content
    )
}

val FitTrackShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp)
)