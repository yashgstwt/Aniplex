package com.example.aniplex.UILayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aniplex.R

@Preview(showSystemUi = true, )
@Composable
fun ErrorScreen(msg:String= "ERROR 404"){

    Box(modifier = Modifier.fillMaxWidth().height(200.dp)){
        Text(text = "ERROR 404" ,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            textAlign = TextAlign.Center ,
            fontSize = 35.sp ,
            fontWeight = FontWeight.Bold ,
            color = Color.Gray
        )
     Image(painter = painterResource(id = R.drawable.error), contentDescription = msg , alignment = Alignment.Center, modifier = Modifier.fillMaxWidth())

    }

}