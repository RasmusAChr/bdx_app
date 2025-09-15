package com.rasmusac.bdx.ui.theme

import android.app.Activity
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

private val DarkColorScheme = darkColorScheme(
    // Primary colors - Main brand color (your blue)
    primary = Color(0xFF4FC3F7),           // Light blue for primary actions
    onPrimary = Color(0xFF003544),         // Dark blue text on primary
    primaryContainer = Color(0xFF004D61),  // Darker blue container
    onPrimaryContainer = Color(0xFFB8E6FF), // Light blue text on primary container

    // Secondary colors - Complementary accent
    secondary = Color(0xFF74D0E8),         // Cyan accent
    onSecondary = Color(0xFF00363F),       // Dark text on secondary
    secondaryContainer = Color(0xFF2A5357), // Dark cyan container
    onSecondaryContainer = Color(0xFFA2EEFF), // Light cyan text on container

    // Tertiary colors - Additional accent (warmer tone)
    tertiary = Color(0xFF9BB5FF),          // Light blue-purple
    onTertiary = Color(0xFF002C6B),        // Dark blue text
    tertiaryContainer = Color(0xFF0A4297), // Medium blue container
    onTertiaryContainer = Color(0xFFD6E3FF), // Very light blue text

    // Background colors
    background = Color(0xFF0F1419),        // Very dark blue-gray (almost black)
    onBackground = Color(0xFFE1F4F9),      // Light blue-white text

    // Surface colors
    surface = Color(0xFF0F1419),           // Same as background
    onSurface = Color(0xFFE1F4F9),         // Light text on surface
    surfaceVariant = Color(0xFF40484C),    // Medium gray-blue
    onSurfaceVariant = Color(0xFFC0C8CC),  // Light gray text

    // Surface containers (Material3 elevation system)
    surfaceContainer = Color(0xFF1A2025),          // Slightly lighter than background
    surfaceContainerHigh = Color(0xFF252B30),      // Medium elevation
    surfaceContainerHighest = Color(0xFF30363B),   // Highest elevation
    surfaceContainerLow = Color(0xFF171D22),       // Low elevation
    surfaceContainerLowest = Color(0xFF0A0E13),    // Lowest elevation

    // Outline colors
    outline = Color(0xFF8A9296),           // Medium gray for borders
    outlineVariant = Color(0xFF40484C),    // Darker outline variant

    // Error colors
    error = Color(0xFFFFB4AB),             // Light red
    onError = Color(0xFF690005),           // Dark red text
    errorContainer = Color(0xFF93000A),    // Dark red container
    onErrorContainer = Color(0xFFFFDAD6),  // Light red text on error container

    // Inverse colors (for special cases)
    inverseSurface = Color(0xFFE1F4F9),    // Light surface for contrast
    inversePrimary = Color(0xFF006781),     // Dark primary for inverse

    // Scrim (for overlays)
    scrim = Color(0xFF000000)              // Pure black for overlays
)

private val LightColorScheme = lightColorScheme(
    // Primary colors - Main brand color (your blue theme)
    primary = Color(0xFF006781),           // Deep blue for primary actions (from your dark inverse)
    onPrimary = Color(0xFFFFFFFF),         // White text on primary
    primaryContainer = Color(0xFFB8E6FF),  // Light blue container (from dark onPrimaryContainer)
    onPrimaryContainer = Color(0xFF001F28), // Very dark blue text on light container

    // Secondary colors - Complementary accent
    secondary = Color(0xFF4A6C70),         // Medium blue-gray
    onSecondary = Color(0xFFFFFFFF),       // White text on secondary
    secondaryContainer = Color(0xFFCDF1F6), // Very light cyan container
    onSecondaryContainer = Color(0xFF051F23), // Very dark cyan text

    // Tertiary colors - Additional accent (warmer tone)
    tertiary = Color(0xFF4A5BA6),          // Medium blue-purple
    onTertiary = Color(0xFFFFFFFF),        // White text on tertiary
    tertiaryContainer = Color(0xFFD6E3FF), // Light blue-purple container (from dark)
    onTertiaryContainer = Color(0xFF001947), // Very dark blue text

    // Background colors
    background = Color(0xFFFAFDFE),        // Very light blue-white
    onBackground = Color(0xFF191C1E),      // Dark blue-gray text

    // Surface colors
    surface = Color(0xFFFAFDFE),           // Same as background
    onSurface = Color(0xFF191C1E),         // Dark text on surface
    surfaceVariant = Color(0xFFDDE3E8),    // Light blue-gray
    onSurfaceVariant = Color(0xFF40484C),  // Medium gray text (same as dark)

    // Surface containers (Material3 elevation system) - progressively darker
    surfaceContainer = Color(0xFFF0F4F8),          // Light gray-blue
    surfaceContainerHigh = Color(0xFFE8EDF2),      // Medium light
    surfaceContainerHighest = Color(0xFFE1E6EB),   // Darkest light surface
    surfaceContainerLow = Color(0xFFF5F9FC),       // Very light
    surfaceContainerLowest = Color(0xFFFFFFFF),    // Pure white

    // Outline colors
    outline = Color(0xFF70787D),           // Medium gray for borders
    outlineVariant = Color(0xFFBFC8CD),    // Light outline variant

    // Error colors
    error = Color(0xFFBA1A1A),             // Dark red
    onError = Color(0xFFFFFFFF),           // White text on error
    errorContainer = Color(0xFFFFDAD6),    // Light red container (from dark)
    onErrorContainer = Color(0xFF410002),  // Very dark red text

    // Inverse colors (for special cases)
    inverseSurface = Color(0xFF2E3133),    // Dark surface for contrast
    inversePrimary = Color(0xFF4FC3F7),    // Light blue for inverse (from dark primary)

    // Scrim (for overlays)
    scrim = Color(0xFF000000)              // Pure black for overlays
)

@Composable
fun BdxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
        content = content
    )
}