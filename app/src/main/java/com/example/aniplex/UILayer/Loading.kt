package com.example.aniplex.UILayer

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showSystemUi = true)
@Composable
fun Loading(modifier: Modifier = Modifier){

    var colo = Color(0xFFBDBDBD)
    var currentColor by remember { mutableStateOf(Color.Gray) }
    val color = remember { Animatable(currentColor) }

    LaunchedEffect(key1 = Unit) { // Trigger animation when composable enters composition
        while (true) {
            color.animateTo(
                targetValue = if (currentColor == Color.Gray) colo else Color.Gray,
                animationSpec = tween(durationMillis = 2000)
            )
            currentColor = if (currentColor == Color.Gray) colo else Color.Gray
        }
    }
    LazyRow() {
        items(4){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .size(140.dp, 200.dp)
                    .background( color.value)
                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp),)
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }

    }
}