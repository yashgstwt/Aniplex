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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.example.aniplex.R
import com.example.aniplex.ViewModal.AniplexViewModal
import com.example.aniplex.ViewModal.GetStreamingData
import com.example.aniplex.ui.theme.Vibrant
import com.example.aniplex.ui.theme.VibrantDark
import com.example.aniplex.ui.theme.black
import com.example.aniplex.ui.theme.gradiantColor
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController



@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(viewModal: AniplexViewModal) {
    var currentEpPlaying by remember { mutableStateOf(viewModal.AnimeEpisodesIDs[viewModal.currentEpisode].id) }
    var playQualityIndex by remember { mutableIntStateOf(0) }
    var URL by remember { mutableStateOf(viewModal.playQuality[playQualityIndex].url) }

   // Log.d("Streaming" ,"hayyyyyy" +currentEpPlaying)
    // method to get streaming link from episode id  i.e from currentEpPlaying
    viewModal.getStreamingLink( currentEpPlaying , viewModal.playbackServer)

    LaunchedEffect(viewModal.StreamingLink) {
        when(viewModal.StreamingLink){
            is GetStreamingData.Error ->  Log.d("Streaming" , (viewModal.StreamingLink as GetStreamingData.Error).message)
            is GetStreamingData.Loading -> Log.d("Streaming" , "Loading")
            is GetStreamingData.Success -> {
                viewModal.playQuality= (viewModal.StreamingLink as GetStreamingData.Success).streamingData.sources
                URL = viewModal.playQuality[playQualityIndex].url
            }
        }
    }

    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }


    LaunchedEffect (URL){
        if(URL.isNotEmpty()) {
            val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
            val uri = Uri.Builder().encodedPath(URL).build() // streaming url
            val hlsMediaItem = MediaItem.Builder().setUri(uri).build()
            val mediaSource = HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        }
    }


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    var height by remember { mutableStateOf(0.3f) }
    if (isLandscape) {
       height = 1f
    } else {
        height = 0.27f
    }
    val systemUiController: SystemUiController = rememberSystemUiController()

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    val brush: List<Color> = listOf(VibrantDark , black)
    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(brush))
    ) {
        Box(modifier = Modifier.fillMaxWidth().statusBarsPadding().fillMaxHeight(height).wrapContentHeight(Alignment.CenterVertically) , contentAlignment = Alignment.Center) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                    }
                },
                modifier = Modifier
                    .background(Color.Black.copy(alpha = .5f))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .also {
                        if (isLandscape) {
                            systemUiController.isStatusBarVisible = false
                            systemUiController.isNavigationBarVisible = false
                            systemUiController.isSystemBarsVisible =
                                false // Optional: Hide both bars at once
                        } else {
                            systemUiController.isStatusBarVisible = true
                            systemUiController.isNavigationBarVisible = true
                            systemUiController.isSystemBarsVisible =
                                true // Optional: Show both bars at once
                        }

                    }
            )
            if (exoPlayer.playbackState != ExoPlayer.STATE_READY || exoPlayer.playbackState == ExoPlayer.STATE_BUFFERING) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(height)
                        .wrapContentHeight(Alignment.CenterVertically)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        Text("Quality" , modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            fontSize = 20.sp,
            color = Color.White
        )

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.Transparent),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(viewModal.playQuality){
                ep->
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .clip(RoundedCornerShape(35.dp))
                        .border(
                            1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .height(height = 55.dp)
                        .background(Vibrant.copy(.5f))
                        .clickable {
                          //  URL = ep.url
                            playQualityIndex = viewModal.playQuality.indexOf(ep)
                            Log.d("Streaming", URL)
                        },

                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        ep.quality,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }



        Text("Episodes" , modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            fontSize = 20.sp,
            color = Color.White
        )

        //Episodes ids
    var currentEp by remember { mutableStateOf(viewModal.currentEpisode) }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .background(brush = Brush.verticalGradient(listOf(Vibrant.copy(.5f), Color.Transparent)))
            , contentPadding = PaddingValues(10.dp)
        ) {
            items(viewModal.AnimeEpisodesIDs.asReversed()){
                    ep->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 70.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .background(if ( ep.number == currentEp ) VibrantDark.copy(.5f) else Vibrant)
                        .clickable {
                            currentEpPlaying = ep.id
                            currentEp = ep.number
                        },
                    contentAlignment = Alignment.Center,
                ) {

                    Text("Episode "+ ep.number.toString(), modifier = Modifier.padding(start = 5.dp , end = 5.dp), fontSize = 10.sp, color = Color.White, textAlign = TextAlign.Center)
                }

            }

        }

    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        exoPlayer.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer.play()
                    }
                    else -> {}
                }
            }
        })
    }
}




//  D  Success(streamingData=StreamingData(headers=Headers(Referer=https://s3taku.com/embedplus?id=MjM0MzM4&token=TAciz9L8o1TLMuvH4cu0NQ&expires=1728212952, UserAgent=null, watchsb=null), sources=[Source(isM3U8=true, quality=360p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.360.m3u8), Source(isM3U8=true, quality=480p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.480.m3u8), Source(isM3U8=true, quality=default, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8), Source(isM3U8=true, quality=backup, url=https://www118.anicdnstream.info/videos/hls/CHjqGbMrJcKUJElLNI5_oQ/1728220153/234338/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8)]))





