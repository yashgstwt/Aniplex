package com.example.aniplex.Helper

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.example.aniplex.R
import com.example.aniplex.ViewModal.GetQuote

@Composable
fun AutoResizeText(text: String, style: TextStyle = MaterialTheme.typography.bodyMedium,modifier: Modifier = Modifier , color: Color){

    var  resizedTextStyle by remember { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }

    var dafaultFontSize = 25.sp
    Text(text,
        style = resizedTextStyle ,
        color = color ,
        modifier = modifier.drawWithContent {
            if (shouldDraw){
                drawContent()
            }
        },
        softWrap = true  ,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified){
                    resizedTextStyle = resizedTextStyle.copy(fontSize = dafaultFontSize)
                }
                resizedTextStyle = resizedTextStyle.copy(fontSize = resizedTextStyle.fontSize * 0.95)
            }else{
                shouldDraw = true
            }

        },
       // maxLines = 4
    )
}



@Composable
@Preview(showSystemUi = true,)
fun prview(){




    Column(modifier =  Modifier.padding(15.dp).fillMaxWidth(), verticalArrangement = Arrangement.Top , horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.search_result), contentDescription = "no result found", modifier = Modifier.size(150.dp,170.dp).shadow(elevation = 55.dp, shape = RoundedCornerShape(25.dp), spotColor = Color.Blue )
        Text("No Result Found" , color = Color.White , fontSize = 20.sp , modifier = Modifier.padding(10.dp))
    }







//    Column(
//        modifier = Modifier
//            .shadow(55.dp, shape = RoundedCornerShape(25.dp), spotColor = Color.Blue)
//            .padding(top = 10.dp, end = 10.dp)
//            .fillMaxWidth()
//            .height(200.dp)
//            .border(2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
//            .clip(shape = RoundedCornerShape(25.dp))
//            .background(Color.Gray)
//    ){
//        Text("Anime: DemonSlayer" ,
//            fontSize = 15.sp ,
//            color = Color.White ,
//            modifier = Modifier.background(Color.Gray).clip(RoundedCornerShape(25.dp)).padding(15.dp)
//        )
//
//        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Transparent).verticalScroll(ScrollState(0))){
//            AutoResizeText("“Don't talk about yourself so much. We'll do that when you leave.Don't talk about yourself so much. We'll do that when you leave ,  leave.Don't talk about yourself so much. We'll do that when you leave  leave.Don't talk about yourself so much. We'll do that when you leave  leave.Don't talk about yourself so much. We'll do that when you leave” ", color = Color.White , modifier = Modifier.padding(10.dp))
//        }
//
//        Text("~Yash Gupta", color = Color.LightGray, fontSize = 10.sp, textAlign = TextAlign.End, modifier = Modifier.padding(end = 15.dp).fillMaxWidth() )
//    }
}