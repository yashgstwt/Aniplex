package com.example.aniplex.Helper


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp

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
