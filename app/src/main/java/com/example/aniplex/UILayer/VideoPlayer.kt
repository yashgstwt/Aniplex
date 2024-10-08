package com.example.aniplex.UILayer

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(viewModal: AniplexViewModal) {
    var currentEpPlaying by remember { mutableStateOf(viewModal.streamingEpisodes[0].url) }
    Log.d("Streaming" ,"hayyyyyy" +currentEpPlaying)
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
    val uri = Uri.Builder().encodedPath(currentEpPlaying).build()
    val hlsMediaItem = MediaItem.Builder().setUri(uri).build()
    val mediaSource = remember { HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var height by remember { mutableStateOf(0.3f) }
    if (isLandscape) {
       height = 1f
    } else {
        height = 0.3f
    }
    val systemUiController: SystemUiController = rememberSystemUiController()

    LaunchedEffect(key1 = isLandscape) {
        if (isLandscape) {
            systemUiController.isStatusBarVisible = false
            systemUiController.isNavigationBarVisible = false
            systemUiController.isSystemBarsVisible = false // Optional: Hide both bars at once
        } else {
            systemUiController.isStatusBarVisible = true
            systemUiController.isNavigationBarVisible = true
            systemUiController.isSystemBarsVisible = true // Optional: Show both bars at once
        }
    }


    exoPlayer.setMediaSource(mediaSource)
    exoPlayer.prepare()


    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    val playerState = exoPlayer.playbackState


    // Use AndroidView to embed an Android View (PlayerView) into Compose
    var brush: List<Color> = listOf(gradiantColor , black)
    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(brush))) {
        if (playerState == Player.STATE_READY) {
            // Start playback or perform other actions when the player is ready
        }
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .background(Color.Black.copy(alpha = .5f))
                .fillMaxWidth()
                .fillMaxHeight(height) // Set your desired height
        )

        Text("Quality" , modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), fontSize = 20.sp)
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.Transparent)) {
            items(viewModal.streamingEpisodes){
                ep->
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
                        .background(Color.Gray)
                        .clickable {
                            currentEpPlaying = ep.id
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(ep.number.toString(), modifier = Modifier.padding(start = 5.dp , end = 5.dp), fontSize = 10.sp, color = Color.White)
                }

            }

        }


//            LazyColumn (modifier = Modifier.fillMaxWidth().height(400.dp)){
//                items(5){
//                    ep->
//                    Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(color = Color.Red)){
//                        Text(text = ep.toString() , fontSize = 25.sp)
//                    }
//                }
//            }

    }


}




//  D  Success(streamingData=StreamingData(headers=Headers(Referer=https://s3taku.com/embedplus?id=MjM0MzM4&token=TAciz9L8o1TLMuvH4cu0NQ&expires=1728212952, UserAgent=null, watchsb=null), sources=[Source(isM3U8=true, quality=360p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.360.m3u8), Source(isM3U8=true, quality=480p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.480.m3u8), Source(isM3U8=true, quality=default, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8), Source(isM3U8=true, quality=backup, url=https://www118.anicdnstream.info/videos/hls/CHjqGbMrJcKUJElLNI5_oQ/1728220153/234338/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8)]))





