package com.example.aniplex.UILayer

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aniplex.Navigation.NavigationRoutes
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor
@Preview(showSystemUi = true)
@Composable
fun FullLoadingScreen(){

    var colo = Color(0xFFBDBDBD)
    var currentColor by remember { mutableStateOf(Color.Gray) }
    val color = remember { Animatable(currentColor) }

    LaunchedEffect(key1 = Unit) { // Trigger animation when composable enters composition
        while (true) {
            color.animateTo(
                targetValue = if (currentColor == Color.Gray) colo else Color.Gray,
                animationSpec = tween(durationMillis = 1500)
            )
            currentColor = if (currentColor == Color.Gray) colo else Color.Gray
        }
    }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(gradiantColor, black)))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .fillMaxSize()
            ){

                Column(
                    modifier = Modifier
                        .size(200.dp, 300.dp)
                        .displayCutoutPadding()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(color.value)
                        .align(Alignment.CenterHorizontally)

                ) {
                    Box(modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .fillMaxWidth()
                        .background(Color.Gray)
                        ,
                    )
                }


                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(70.dp), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(gradiantColor)
                        .size(100.dp, 50.dp),
                    )


                    Box(modifier = Modifier
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(gradiantColor)
                        .size(100.dp, 50.dp),
                    )

                }

                Text("Genera", fontSize = 20.sp, color = Color.White, fontFamily = FontFamily.Serif)

                Row(modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp)) {
                    for(it in 1..5) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .border(
                                    1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(height = 25.dp)
                                .background(Color.Gray)
                            ,
                        )
                    }
                }
                Text(
                    "Description",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )

              Box(modifier = Modifier.padding(10.dp)
                  .clip(RoundedCornerShape(25.dp))
                  .fillMaxWidth()
                  .height(200.dp)
                  .background(color.value)
                  .clip(RoundedCornerShape(25.dp)))

        }
    }
}