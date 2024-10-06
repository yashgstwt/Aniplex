package com.example.aniplex.UILayer

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView


@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView(episodeURL : String) {
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
//    val mediaSource = remember (episodeURL) {
//        MediaItem.fromUri(episodeURL)
//    }


    val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
    val uri = Uri.Builder().encodedPath(episodeURL).build()
    val hlsMediaItem = MediaItem.Builder().setUri(uri).build()
    val mediaSource = remember { HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem) }

    exoPlayer.setMediaSource(mediaSource)
    exoPlayer.prepare()


    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Set your desired height
    )
}























//  D  Success(streamingData=StreamingData(headers=Headers(Referer=https://s3taku.com/embedplus?id=MjM0MzM4&token=TAciz9L8o1TLMuvH4cu0NQ&expires=1728212952, UserAgent=null, watchsb=null), sources=[Source(isM3U8=true, quality=360p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.360.m3u8), Source(isM3U8=true, quality=480p, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.480.m3u8), Source(isM3U8=true, quality=default, url=https://www118.anzeat.pro/streamhls/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8), Source(isM3U8=true, quality=backup, url=https://www118.anicdnstream.info/videos/hls/CHjqGbMrJcKUJElLNI5_oQ/1728220153/234338/4bc8df880225830b6077fbccc564a0f4/ep.1.1728066302.m3u8)]))





