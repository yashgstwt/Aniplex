package com.example.aniplex.UILayer

import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aniplex.DataLayer.aniplexApi.ResultXX
import com.example.aniplex.Navigation.NavigationRoutes
import com.example.aniplex.R
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetSearch
import com.example.aniplex.ui.theme.gradiantColor


@OptIn(UnstableApi::class)
@Composable
fun SearchScreen(viewModal: AniplexViewModal, navController: NavHostController) {
    var search: String by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(listOf(gradiantColor, Color.Black)))
    ) {
        TextField(
            value = search ,
            onValueChange = { search = it},
            modifier = Modifier
                .padding(10.dp)
                .statusBarsPadding()
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(25.dp))
                .border(
                    border = BorderStroke(1.dp, color = Color.White),
                    shape = RoundedCornerShape(25.dp)
                )
                .onFocusChanged {
                    if (it.isFocused) {
                        isFocused = true
                    } else {
                        isFocused = false
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray ,
                focusedTextColor = Color.White ,
                unfocusedTextColor = Color.Gray ,
                cursorColor = Color.Gray
            ),
             placeholder = { Text("Search", color = Color.LightGray)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            },
            trailingIcon = {
                if (isFocused){
                Icon(imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                    tint = Color.White,
                    modifier = Modifier.clickable { search = "" }
                )
                }
            },
           singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions ( onSearch = {
                //make network request here
                    viewModal.getSearch(search)
            }
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        when(viewModal.search){
            is GetSearch.ERROR -> {
                Text("${ viewModal.search as GetSearch.ERROR }" , color = Color.White , fontSize = 20.sp)
            }
            is GetSearch.Loading -> {}
            is GetSearch.Success -> {
                if ((viewModal.search as GetSearch.Success).search.results.isEmpty()){
                    Column(modifier =  Modifier.padding(15.dp).fillMaxWidth(), verticalArrangement = Arrangement.Top , horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(R.drawable.search_result), contentDescription = "no result found", modifier = Modifier.size(150.dp,170.dp).shadow(elevation = 55.dp, shape = RoundedCornerShape(25.dp), spotColor = Color.Red, ambientColor = Color.Blue))
                        Text("No Result Found" , color = Color.White , fontSize = 20.sp , modifier = Modifier.padding(10.dp) , fontFamily = FontFamily.Serif)
                    }
                }
                LazyColumn() {
                    items((viewModal.search as GetSearch.Success).search.results){
                        SearchCard(it){ id ->
                            navController.navigate(NavigationRoutes.DETAIL_SCREEN.toString() + "/${id}" + "?isFavScreen=${false}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchCard(result: ResultXX, onClick : (id:String) -> Unit = {}){
    Row(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .height(100.dp)
        .clip(RoundedCornerShape(25.dp))
        .background(Color.DarkGray)
        .clickable {
         onClick(result.id)
        }
    ) {
   AsyncImage(model = result.image ,
       contentDescription = "image",
       contentScale = ContentScale.Crop,
       modifier = Modifier
           .padding(10.dp)
           .aspectRatio(11f / 16f)
           .clip(
               RoundedCornerShape(15.dp)
           )
       )
        Column (modifier = Modifier
            .fillMaxHeight()
            .weight(1f), verticalArrangement = Arrangement.Center){
            Text(
                text = result.title,
                fontSize = 20.sp ,
                color = Color.White ,
                fontFamily = FontFamily.Serif,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text("Release data : ${result.releaseDate}" , fontSize = 10.sp, color = Color.LightGray)
            Text("Type: ${result.subOrDub}" , fontSize = 10.sp, color = Color.LightGray)
        }
    }
}