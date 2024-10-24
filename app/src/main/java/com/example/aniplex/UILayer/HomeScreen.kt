@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.aniplex.UILayer



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aniplex.DataLayer.Result
import com.example.aniplex.DataLayer.ResultX
import com.example.aniplex.Navigation.NavigationRoutes
import com.example.aniplex.R
import com.example.aniplex.RoomDb.Favourite
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetRecentEpisodes
import com.example.aniplex.ViewModal.GetTopAirings
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(AniplexViewModal: AniplexViewModal, navController: NavHostController) {


    LaunchedEffect(AniplexViewModal.topAiringsPage) {
        AniplexViewModal.getTopAirings(AniplexViewModal.topAiringsPage)
       // Log.d("topairings", "HomeScreen: ${AniplexViewModal.topAiringsPage}")
       // Log.d("topairings", ": ${AniplexViewModal.topAirings}")

    }

    LaunchedEffect(AniplexViewModal.recentReleasedPage){
        AniplexViewModal.getRecentEpisode(page = AniplexViewModal.recentReleasedPage)
      //  Log.d("recentReleased", "HomeScreen: ${AniplexViewModal.recentReleasedPage}")
    }


    //Log.d("LOG","HomeScreen............................................................................")
    var brush: List<Color> = listOf(gradiantColor , black)

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
        .padding(top = 10.dp , start=10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier= Modifier.displayCutoutPadding())
            Box(modifier = Modifier.fillMaxWidth().padding(end=10.dp).border(2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))){
                Image(painter = painterResource(R.drawable.mochuro) ,
                    contentDescription = "cover image " ,
                    modifier = Modifier.padding(start = 10.dp)
                        .size(70.dp)
                        .align(Alignment.BottomStart)
                        .clip(RoundedCornerShape(bottomStart = 25.dp)) ,
                    contentScale = ContentScale.Crop)
                Text(
                    text = "Aniplex",
                    fontSize = 35.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    fontFamily = FontFamily.Serif,
                    color = Color.White,
                    letterSpacing = 7.sp,

                )
            }

            Text(
                "Trending",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 15.dp)
            )

            Box(
                modifier = Modifier
                    .shadow(55.dp, shape = RoundedCornerShape(25.dp), spotColor = Color.Blue)
                    .padding(top = 10.dp, end = 10.dp)
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
                            if (currentPoster < trendingPageList.size - 1) currentPoster += 1 else currentPoster = 0
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
//----------------------------------------------------------RecentReleased----------------------------------------------------------------------------------------------------------------

            Row (modifier = Modifier.fillMaxWidth().padding(top=20.dp).height(50.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text("RecentReleased ", fontSize = 25.sp , fontFamily = FontFamily.Serif , color = Color.White )
                Row (modifier= Modifier.height(50.dp).padding(end=20.dp) , horizontalArrangement = Arrangement.End){
                    if (AniplexViewModal.recentReleasedPage > 0){
                    Icon(imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "BackArrow",
                        modifier = Modifier.clickable{

                            AniplexViewModal.recentReleasedPage--
                            //Log.d("recentReleased", "HomeScreen: ${AniplexViewModal.recentReleasedPage}")

                        }.size(35.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(25.dp)).background(Color.LightGray),
                        tint = Color.White
                    )
                    }
                    Spacer(modifier = Modifier.size(40.dp))
                    Icon(imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "ForwardArrow",
                        modifier = Modifier.clickable {
                            AniplexViewModal.recentReleasedPage++
                            //Log.d("topairings", "HomeScreen: ${AniplexViewModal.recentReleasedPage}")

                        }.size(35.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(25.dp)).background(Color.LightGray),
                        tint = Color.White
                    )
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)){
                when(AniplexViewModal.recentEpisodes){
                    is  GetRecentEpisodes.Success -> {
                        LazyRow (modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) ,
                            horizontalArrangement = Arrangement.Center ,
                            verticalAlignment = Alignment.CenterVertically,

                        ){
                            items(items = (AniplexViewModal.recentEpisodes as GetRecentEpisodes.Success).data.results ){
                                data -> NewAnimeCard(data){
                                    navController.navigate(NavigationRoutes.DETAIL_SCREEN.toString()+"/$it"+"?isFavScreen=${false}")
                                }
                            }
                        }
                    }

                    is GetRecentEpisodes.Error -> {
                        ErrorScreen((AniplexViewModal.recentEpisodes as GetRecentEpisodes.Error).error.toString())
                    }

                    is GetRecentEpisodes.Loading -> {
                        Loading()
                    }
                }
            }

            // --------------------------------------------------TopAiring----------------------------------------------------------------------

            Row (modifier = Modifier.fillMaxWidth().padding(top=20.dp).height(50.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text(" TopAirings ", fontSize = 25.sp , fontFamily = FontFamily.Serif , color = Color.White )
                Row (modifier= Modifier.height(50.dp).padding(end= 20.dp) , horizontalArrangement = Arrangement.End){

                    Icon(imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "arrow",
                        modifier = Modifier.clickable{
                            //if (AniplexViewModal.topAiringsPage > 0)
                                AniplexViewModal.topAiringsPage--
                           // Log.d("topairings", "HomeScreen: ${AniplexViewModal.topAiringsPage}")

                        }.size(35.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(25.dp)).background(Color.LightGray),
                        tint = Color.White
                    )
                    Spacer(Modifier.size(40.dp))
                    Icon(imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "arrow",
                        modifier = Modifier.clickable {
                            AniplexViewModal.topAiringsPage++
                            //Log.d("topairings", "HomeScreen: ${AniplexViewModal.topAiringsPage}")

                        }.size(35.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(25.dp)).background(Color.LightGray),
                        tint = Color.White
                    )
                }
            }


            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.CenterStart ,){
                when(AniplexViewModal.topAirings){
                    is GetTopAirings.Error -> ErrorScreen((AniplexViewModal.topAirings as GetTopAirings.Error).message)
                    GetTopAirings.Loading -> Loading()
                    is GetTopAirings.Success -> {
                        LazyRow(modifier = Modifier.matchParentSize(), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center) {
                            items((AniplexViewModal.topAirings as GetTopAirings.Success).airings.results){ DATA->

                                AnimeCard(DATA){ id , isFavScreen ->
                                    navController.navigate(NavigationRoutes.DETAIL_SCREEN.toString()+"/$id"+"?isFavScreen=${false}")
                                }
                            }
                        }
                    }
                }
            }
//-----------------------------------------------------------------Favourite----------------------------------------------------------------------------------------
            Text(
                "Favourite",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 15.dp)
            )
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.CenterStart ,){

                var favouriteList :List<Favourite> by remember { mutableStateOf(emptyList()) }
                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {   AniplexViewModal.db.getFavouriteList().collectLatest { list ->
                        favouriteList = list
                       // favouriteList.reversed()
                    // Log.d("room" , "${favouriteList}")
                        }
                    }
                }

                LazyRow(modifier = Modifier.matchParentSize(), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center) {
                    items(favouriteList){ DATA->
                            //Log.d("room" , "${DATA.animeId} ${DATA.name} ${DATA.imageUrl} ${DATA.dubOrSub}")
                        FavAnimeCard(DATA.animeId , DATA.name , DATA.imageUrl , DATA.dubOrSub){ id , isFavScreen ->
                            navController.navigate(NavigationRoutes.DETAIL_SCREEN.toString()+"/$id"+"?isFavScreen=${true}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NewAnimeCard(data: Result , OnClick : (id:String) -> Unit = {} ){
    Box(
        modifier = Modifier
            .padding(start = 5.dp , end=5.dp )
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .size(140.dp, 200.dp)
            .clickable {
                OnClick(data.id)

            },
        )
    {

        AsyncImage(model = data.image , contentDescription = data.title , alignment = Alignment.BottomStart ,  modifier = Modifier.fillMaxSize(),contentScale = ContentScale.Crop)
        Text("Ep : ${data.episodeNumber}" ,
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
            Text( text = data.title ,
                modifier = Modifier
                .padding(bottom = 2.dp)
                .fillMaxWidth(),
                fontSize = 10.sp ,
                color = Color.White ,
                textAlign = TextAlign.Center ,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun AnimeCard(result: ResultX , OnClick : (id:String , isFavScreen:Boolean) -> Unit = { s: String, b: Boolean -> }) {
    Box(
        modifier = Modifier.padding(5.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .size(140.dp, 200.dp).clickable {
                OnClick(result.id , false)
            }

    ){

        AsyncImage(model = result.image ,
            contentDescription = result.title ,
            alignment = Alignment.BottomStart ,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
            ,verticalArrangement = Arrangement.Bottom ,
            horizontalAlignment =  Alignment.CenterHorizontally,
            ){
            Text( text = result.title ,
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



@Composable
fun FavAnimeCard(id: String,name:String,imgUrl:String,dubOrSub:String , OnClick : (id:String , isFavScreen:Boolean ) -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 5.dp , end=5.dp )
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .size(140.dp, 200.dp)
            .clickable {
                OnClick(id , true)
            },
    )
    {

        AsyncImage(model =imgUrl , contentDescription = name , alignment = Alignment.BottomStart ,  modifier = Modifier.fillMaxSize(),contentScale = ContentScale.Crop)
        Text(dubOrSub ,
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
            Text( text = name ,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .fillMaxWidth(),
                fontSize = 10.sp ,
                color = Color.White ,
                textAlign = TextAlign.Center ,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
