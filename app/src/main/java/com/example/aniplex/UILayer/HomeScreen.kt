package com.example.aniplex.UILayer



import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.aniplex.DataLayer.Result
import com.example.aniplex.R
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetRecentEpisodes


@Composable
fun HomeScreen( AniplexViewModal: AniplexViewModal) {

    Log.d("LOG","HomeScreen............................................................................")
    var brush: List<Color> = listOf(Color(0xFF6B7777), Color(0xFF000000))

    val trendingPageList: List<Int> = listOf(
        R.drawable.nature,
        R.drawable.goku,
        R.drawable.violet,
        R.drawable.violetevergarden,
    )
    val scrollState = rememberScrollState()
    var currentPoster by remember { mutableStateOf(0) }

    var offsetX by remember { mutableStateOf(0f) }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(brush))
        .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Spacer(modifier= Modifier.displayCutoutPadding())
            Text(
                text = "Aniplex",
                fontSize = 35.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = FontFamily.Serif,
                color = Color.White,
                letterSpacing = 7.sp,
            )

            Text(
                "Trending",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 15.dp)
            )

            Box(
                modifier = Modifier
                    .shadow(55.dp, shape = RoundedCornerShape(25.dp), spotColor = Color.Blue)
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(Color.Gray)
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            offsetX += delta
                        },
                        onDragStopped = {
                            if (currentPoster < trendingPageList.size - 1) currentPoster += 1 else currentPoster =
                                0
                        }

                    )
            ) {
                Image(
                    painter = painterResource(trendingPageList[currentPoster]),
                    "img",
                    contentScale = ContentScale.FillBounds
                )
            }
            LazyRow(horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.Bottom , modifier = Modifier.fillMaxWidth()) {
                items(trendingPageList.size){
                    if (currentPoster == it ){
                        Text(".", fontSize = 40.sp , color = Color.White)

                    } else
                        Text(".", fontSize = 35.sp , color = Color.Gray)
                }
            }

            Text("Resent Released ", fontSize = 25.sp , fontFamily = FontFamily.Serif , color = Color.White , modifier = Modifier.padding(bottom = 10.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)){
                when(AniplexViewModal.recentEpisodes){
                    is  GetRecentEpisodes.Success -> {
                        LazyRow (modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) , horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.CenterVertically){
                            items(items = (AniplexViewModal.recentEpisodes as GetRecentEpisodes.Success).data.results ){
                                data -> NewAnimeCard(data)


                            }
                        }

                    }

                    is GetRecentEpisodes.Error -> {
                        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.error))
                        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, modifier = Modifier.size(150.dp))
                    }

                    is GetRecentEpisodes.Loading -> {
                        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))
                        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, modifier = Modifier.size(150.dp))
                    }

                }
            }

           // NewAnimeCard()
            Text(" Popular ", fontSize = 25.sp , fontFamily = FontFamily.Serif , color = Color.White , modifier = Modifier.padding(bottom = 10.dp))
            AnimeCard()

        }
    }
}


@Composable
fun NewAnimeCard(data: Result) {
    Box(
        modifier = Modifier
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .size(140.dp, 200.dp),
    ){

        AsyncImage(model = data.image , contentDescription = data.title , alignment = Alignment.BottomStart ,  modifier = Modifier.fillMaxSize(),contentScale = ContentScale.Crop)
        Text("Ep : ${data.episodeNumber.toString()}" ,
            fontSize = 10.sp ,
            color = Color.White ,
            textAlign = TextAlign.Start ,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomEnd = 25.dp))
                .background(Color(0xFF2582f3))
                .padding(start = 10.dp, top = 2.dp, bottom = 2.dp, end = 10.dp)
        )

        Column(
            Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black))),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text( text = data.title , modifier = Modifier
                .padding(bottom = 2.dp)
                .fillMaxWidth(), fontSize = 10.sp , color = Color.White , textAlign = TextAlign.Center , fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun AnimeCard (){
    Box(
        modifier = Modifier
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .size(140.dp, 200.dp),
    ){
        Image(
            painter = painterResource(R.drawable.naruto) ,
            contentDescription = "naruto img ",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black))),
            verticalArrangement = Arrangement.Bottom ,
            horizontalAlignment =  Alignment.CenterHorizontally,
            ){
            Text( text = "Naruto  " ,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .fillMaxWidth(),
                fontSize = 10.sp , color = Color.White ,
                textAlign = TextAlign.Center ,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun pre(){
//
//    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//        HomeScreen(innerPadding, AniplexViewModal)
//    }
//}