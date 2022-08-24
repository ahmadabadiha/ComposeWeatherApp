package com.example.composeweatherapp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun convertToTempString(temp: Double): String{
    return "$temp°"
}

val textModifierWithPadding4 = Modifier.padding(4.dp)
val textModifierWithPadding8 = Modifier.padding(8.dp)
@Composable
fun LoadingLottieView() {
    Spacer(modifier = Modifier.height(120.dp))
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_anim))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.size(300.dp)
    )
}