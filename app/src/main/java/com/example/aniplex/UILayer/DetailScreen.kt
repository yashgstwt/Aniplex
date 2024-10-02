package com.example.aniplex.UILayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aniplex.R
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor


@Preview(showSystemUi = true )
@Composable
fun DetailScreen () {
    val scrollState = rememberScrollState()

    var brush: List<Color> = listOf(gradiantColor , black)
    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(brush)).padding(10.dp))  {

        Box(modifier = Modifier.size(200.dp, 300.dp)
            .displayCutoutPadding()
            .height(400.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clip(shape = RoundedCornerShape(25.dp))
            .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.TopEnd
        ){
            Image(painter= painterResource(R.drawable.naruto) ,
                contentDescription = "" ,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .border(2.dp,
                        color = Color.White ,
                        shape = RoundedCornerShape(25.dp)
                    ) ,
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.padding(10.dp)
                    .clip(RoundedCornerShape(25.dp)).border(1.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
                    .size(35.dp , 25.dp)
                    .background(Color.Gray)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center ,
                ){
                Text("2020", modifier= Modifier, fontSize = 10.sp, color = Color.White)
            }
        }


        Row (modifier = Modifier.fillMaxWidth().
                    padding(top = 20.dp) ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Naruto" , fontSize = 35.sp , color = Color.White, modifier = Modifier , fontFamily = FontFamily.Serif )

            Box(modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .border(1.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
                    .size(40.dp , 30.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center ,
            ){
                Text("TV", modifier= Modifier, fontSize = 10.sp, color = Color.White)
            }
        }


        Row (modifier= Modifier.padding(top = 20.dp).fillMaxWidth().height(70.dp) , horizontalArrangement = Arrangement.SpaceEvenly ){
            Box(modifier = Modifier.clip(shape = RoundedCornerShape(25.dp))
                .background(gradiantColor)
                .size(100.dp , 50.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "play",modifier= Modifier.fillMaxSize())
            }

            var favColor by remember { mutableStateOf(Color.Black) }
            Box(modifier = Modifier.clickable{
                favColor = if (favColor == Color.Black) Color.Red else Color.Black
            }
                .clip(shape = RoundedCornerShape(25.dp))
                .background(gradiantColor).size(100.dp , 50.dp) ,
                contentAlignment = Alignment.Center
            ){
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "play",modifier= Modifier.fillMaxSize(.8f) , tint = favColor)
            }

        }

        Text("Genera"  , fontSize = 20.sp , color = Color.White , fontFamily = FontFamily.Serif)
        val data = listOf("Item 1", "Item 2", "Itemdw 3", "Item 4", "Itwwdwd 5","Item 2", "Itemdw 3", "Item 4", "Itwwdwd 5")
        Box(modifier = Modifier.fillMaxWidth()){
          LazyVerticalGrid(
              columns = GridCells.Adaptive(100.dp),
              contentPadding = PaddingValues(10.dp)
          ){
              items(data){
                  Box(modifier = Modifier.padding(10.dp)
                      .clip(RoundedCornerShape(25.dp)).border(1.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
                      .size(35.dp , 25.dp)
                      .background(Color.Gray)
                      .fillMaxWidth(),
                      contentAlignment = Alignment.Center ,
                  ){
                      Text(it, modifier= Modifier, fontSize = 10.sp, color = Color.White)
                  }
              }
          }

        }


        Text("Description"  , fontSize = 20.sp , color = Color.White , fontFamily = FontFamily.Serif)
        Column (modifier = Modifier.fillMaxWidth().height(500.dp).padding(top= 10.dp).verticalScroll(scrollState)){
            Text("One final piece that is left out to use in CompositionLocalProvider is layout width. This can get from LocalConfiguration#ScreenWidthDp which is the width of the App at any point of time (single app, split mode, window mode, orientation changed, or any other in future).\n" +
                    "\n" +
                    "Note: layout width need not be the complete width of the app, it can also be part of the screen in situations like a two-pane layout, sliding-pane, etc.,\n" +
                    "\n" +
                    "Everything is done, ready to code by just passing the formed GridConfiguration value in CompositionLocalProvider" , color = Color.White)

        }

    }

}