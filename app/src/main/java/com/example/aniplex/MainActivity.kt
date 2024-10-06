package com.example.aniplex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.example.aniplex.Navigation.Navigation
import com.example.aniplex.UILayer.DetailScreen
import com.example.aniplex.UILayer.ExoPlayerView
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetStreamingData
import com.example.aniplex.ui.theme.AniplexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AniplexTheme {
                val AniplexViewModal : AniplexViewModal by viewModels()

                Navigation( AniplexViewModal)
                //ExoPlayerView()

//                when(AniplexViewModal.getStreamingLink){
//                    is GetStreamingData.Error -> {
//                       var msg =  (AniplexViewModal.getStreamingLink as GetStreamingData.Error).message
//                        Text(text = msg , fontSize = 30.0.sp)
//                    }
//                    is GetStreamingData.Loading -> {
//                        Text(text = "Loading" , fontSize = 30.0.sp)
//                    }
//                    is GetStreamingData.Success -> {
//                        var url = (AniplexViewModal.getStreamingLink as GetStreamingData.Success).streamingData.sources[0].url
//                        ExoPlayerView(url)
//                    }
//                }
            }
        }
    }
}

