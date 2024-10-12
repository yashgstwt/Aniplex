package com.example.aniplex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.aniplex.Navigation.Navigation
import com.example.aniplex.ViewModal.AniplexViewModal
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
                //ExoPlayerView("https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.360.m3u8")

            }
        }
    }
}

