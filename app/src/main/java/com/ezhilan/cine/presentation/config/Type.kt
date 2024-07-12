package com.ezhilan.cine.presentation.config

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.ezhilan.cine.R

val defaultTypography = Typography()

fun getTypography(sizeFactor: Float) = Typography(

    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),

    headlineLarge = defaultTypography.headlineLarge.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),

    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),

    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
    ),

    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
    ),
)


data class TextStyles(

    val sizeFactor: Float = 1f,
    val defaultTitle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontSize = (sizeFactor * 16).sp,
    ),
    val defaultTitleSmall: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = (sizeFactor * 12).sp,
    ),

    //Carousel
    val carouselTitle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = (sizeFactor * 16).sp,
    ),
    val carouselRating: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = (sizeFactor * 14).sp,
    ),
    val carouselMediaType: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontSize = (sizeFactor * 10).sp,
    ),
    val carouselYear: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = (sizeFactor * 12).sp,
    ),
    val carouselGenre: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontSize = (sizeFactor * 12).sp,
    ),
    val carouselOverview: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_thin)),
        fontSize = (sizeFactor * 10).sp,
        fontStyle = FontStyle.Italic,
    ),

    //Default media card
    val cardMediaType: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontSize = (sizeFactor * 8).sp,
    ),
    val cardYear: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = (sizeFactor * 10).sp,
    ),
    val cardRating: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = (sizeFactor * 10).sp,
    ),
    val cardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontSize = (sizeFactor * 14).sp,
    ),

    //Settings
    val settingsLabelSmall: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = (sizeFactor * 12).sp,
    ),
)

val LocalTextStyle = compositionLocalOf { TextStyles() }

val MaterialTheme.textStyle: TextStyles
    @Composable @ReadOnlyComposable get() = LocalTextStyle.current

