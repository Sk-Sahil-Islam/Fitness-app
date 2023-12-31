package com.example.fitnessapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R

val Kanit = FontFamily(
    Font(R.font.kanit_thin, FontWeight.Thin),
    Font(R.font.kanit_extra_light, FontWeight.ExtraLight),
    Font(R.font.kanit_bold_italic, FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.kanit_light_italic, FontWeight.Light, style = FontStyle.Italic)
)

val Bricolage = FontFamily(
    Font(R.font.bricolage_grotesque_regular, FontWeight.Normal),
    Font(R.font.bricolage_grotesque_bold, FontWeight.Bold)
)

val JosefinSans = FontFamily(
    Font(R.font.josefin_sans_light_italic, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val ownTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = Kanit
    )
)