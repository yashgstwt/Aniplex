package com.example.aniplex.UILayer

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.aniplex.DataLayer.aniplexApi.AnimeInfo
import com.example.aniplex.Navigation.NavigationRoutes
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetAnimeInfo
import com.example.aniplex.ui.theme.Vibrant
import com.example.aniplex.ui.theme.VibrantDark
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor


@Composable
fun DetailScreen(
    viewModal: AniplexViewModal,
    navController: NavHostController,
    animeId: String?,
    isFavScreen: Boolean? = false
) {

    LaunchedEffect(key1 = Unit ) {
        viewModal.AnimeInfo = GetAnimeInfo.Loading
        viewModal.getAnimeInfo(animeId!!)
    }

    when (viewModal.AnimeInfo) {
        is GetAnimeInfo.Success -> {
            Log.d("DetailScreen"," ${ (viewModal.AnimeInfo as GetAnimeInfo.Success).animeInfo }")
            var animeInfo: AnimeInfo =  (viewModal.AnimeInfo as GetAnimeInfo.Success).animeInfo
            viewModal.AnimeEpisodesIDs = animeInfo.episodes
            DetailScreenUi(animeInfo, navController, viewModal , isFavScreen)
        }

        is GetAnimeInfo.Error -> {
            ErrorScreen((viewModal.AnimeInfo as GetAnimeInfo.Error).message.toString())
        }
        is  GetAnimeInfo.Loading -> {
            FullLoadingScreen()
        }
    }

}

@Composable
fun DetailScreenUi(
    animeInfo: AnimeInfo,
    navController: NavHostController,
    viewModal: AniplexViewModal,
    isFavScreen: Boolean? = false
) {
    var darkVibrant by remember { mutableStateOf(gradiantColor) }
    var vibrant by remember { mutableStateOf(Color.LightGray) }

    var brush: List<Color> = listOf(darkVibrant,black)
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var palette:Palette? = null

    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(brush))
        .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .size(200.dp, 300.dp)
                    .displayCutoutPadding()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .align(Alignment.CenterHorizontally)
                ,contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    animeInfo.image, contentDescription = "",
                    modifier = Modifier
                        .size(200.dp, 300.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Success) {
                            imageBitmap = state.result.drawable.toBitmap()
                            val softwareBitmap = imageBitmap!!.copy(Bitmap.Config.ARGB_8888, false)
                            palette = Palette.from(softwareBitmap).generate()
                            darkVibrant = palette?.darkVibrantSwatch?.rgb?.let { Color(it) } ?: gradiantColor
                            vibrant = palette?.vibrantSwatch?.rgb?.let { Color(it) } ?: Color.LightGray
                            Vibrant = vibrant
                            VibrantDark = darkVibrant
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(1.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
                        .size(35.dp, 25.dp)
                        .background(Color.Gray)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        animeInfo.subOrDub,
                        modifier = Modifier,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }

            AdaptiveText(
                text = animeInfo.title,
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                color = Color.White,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.Serif,
            )


            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(70.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(vibrant)
                        .size(100.dp, 50.dp)
                        .clickable {
                            navController.navigate(NavigationRoutes.VIDEOPLAYER_SCREEN.toString())
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "play",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                var favColor by remember { mutableStateOf(Color.Black) }
                var isClicked by remember { mutableStateOf(isFavScreen!!) }

                if(isClicked){
                    favColor = Color.Red
                }else{
                    favColor = Color.Black
                }

                if (isClicked && !isFavScreen!!) {
                    viewModal.insertFav(animeInfo.id,animeInfo.title,animeInfo.image,animeInfo.subOrDub)
                } else if( !isClicked && isFavScreen!!) {
                    viewModal.DeleteFav(animeInfo.id)
                }


                Box(modifier = Modifier
                    .clickable {
                        isClicked = !isClicked
                    }
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(vibrant.copy(.7f))
                    .size(100.dp, 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "fav",
                        modifier = Modifier.fillMaxSize(.8f),
                        tint = favColor
                    )
                }

            }

            Text("Genera", fontSize = 20.sp, color = Color.White, fontFamily = FontFamily.Serif)
            Row(modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(50.dp)
                .horizontalScroll(rememberScrollState())) {
                animeInfo.genres.forEach {
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
                            .background(Vibrant.copy(.5f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(it, modifier = Modifier.padding(start = 5.dp , end = 5.dp), fontSize = 10.sp, color = Color.White)
                    }
                }
            }
            Text(
                "Description",
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = FontFamily.Serif
            )

            Text(
                animeInfo.description,
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )

            )
        }
    }
}


@Composable
fun AdaptiveText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    color: Color,
    textAlign: TextAlign,
    fontFamily: GenericFontFamily,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val placeable = subcompose("text") {
            Text(text, style = TextStyle(fontSize = fontSize , color = color , textAlign = textAlign , fontFamily = fontFamily)) // Initial font size
        }[0].measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}