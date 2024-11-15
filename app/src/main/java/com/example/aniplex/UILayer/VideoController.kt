package com.example.aniplex.UILayer

import com.example.aniplex.R
import com.example.aniplex.ViewModal.AniplexViewModal
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.metrics.PlaybackStateEvent.STATE_ENDED
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.example.aniplex.ViewModal.GetStreamingData
import com.example.aniplex.ui.theme.Vibrant
import com.example.aniplex.ui.theme.VibrantDark
import com.example.aniplex.ui.theme.black
import java.util.concurrent.TimeUnit

private const val PLAYER_SEEK_BACK_INCREMENT = 10 * 1000L // 10 seconds
private const val PLAYER_SEEK_FORWARD_INCREMENT = 15 * 1000L // 15 seconds
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun CustomVideoPlayer(modifier: Modifier = Modifier.statusBarsPadding() , viewModal: AniplexViewModal) {

    var currentEpPlayingID by remember { mutableStateOf(viewModal.AnimeEpisodesIDs[viewModal.currentEpisode].id) }
    var playQualityIndex by remember { mutableIntStateOf(0) }
    var URL by remember { mutableStateOf(viewModal.playQuality[playQualityIndex].url) }

    // Log.d("Streaming" ,"hay" +currentEpPlaying)
    // method to get streaming link from episode id  i.e from currentEpPlaying

    LaunchedEffect (currentEpPlayingID){
        viewModal.getStreamingLink(currentEpPlayingID, viewModal.playbackServer)
    }

    val context = LocalContext.current
    val activity = context as? Activity
    var height by remember { mutableFloatStateOf(.3f) }

    LaunchedEffect(viewModal.isLandscape.collectAsState().value, LocalConfiguration.current) {
        // Log.d("viewmodal", viewModel.getIsLandscape().toString() + " from launched effect ")

        if (viewModal.getIsLandscape()) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            height = 1f

            // Log.d("viewmodal", " if block : $height from launched effect ${viewModel.getIsLandscape()}")
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            height = .31f
            // Log.d("viewmodal", " else block : $height from launched effect ${viewModel.getIsLandscape()}")
        }
    }

    LaunchedEffect(viewModal.StreamingLink) {
        when (viewModal.StreamingLink) {
            is GetStreamingData.Error -> Log.d(
                "Streaming",
                (viewModal.StreamingLink as GetStreamingData.Error).message
            )

            is GetStreamingData.Loading -> Log.d("Streaming", "Loading")
            is GetStreamingData.Success -> {
                viewModal.playQuality =
                    (viewModal.StreamingLink as GetStreamingData.Success).streamingData.sources
                URL = viewModal.playQuality[playQualityIndex].url
            }
        }
    }


    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .apply {
                setSeekBackIncrementMs(PLAYER_SEEK_BACK_INCREMENT)
                setSeekForwardIncrementMs(PLAYER_SEEK_FORWARD_INCREMENT)
            }
            .build()
    }

    LaunchedEffect(URL) {
        if (URL.isNotEmpty()) {
            val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
            val uri = Uri.Builder().encodedPath(URL).build() // streaming url
            val hlsMediaItem = MediaItem.Builder().setUri(uri).build()
            val mediaSource =
                HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            // Log.d("video1" , "isPlaying from exoplayer launched effect  : ${exoPlayer.isPlaying}")
        }
    }


    var shouldShowControles by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    //var currentTime by remember { mutableLongStateOf(0L) }
    var bufferedPercentage by remember { mutableIntStateOf(0) }
    var playbackState by remember { mutableStateOf(exoPlayer.playbackState) }

    val brush: List<Color> = listOf(VibrantDark, black)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(brush))
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(height)
        ) {
            val listener = object : Player.Listener {
                override fun onEvents(
                    player: Player,
                    events: Player.Events,
                ) {
                    Log.d("logee" , "onEvents called ${player.currentPosition}")
                    super.onEvents(player, events)
                    totalDuration = player.duration.coerceAtLeast(0L)
                    viewModal.updateCurrentVideoTime(player.currentPosition.coerceAtLeast(0L))
                    bufferedPercentage = player.bufferedPercentage
                    isPlaying = player.isPlaying
                    playbackState = player.playbackState
                }
            }
            exoPlayer.addListener(listener)
            DisposableEffect(key1 = Unit) {
                onDispose {
                    exoPlayer.removeListener(listener)
                    exoPlayer.release()
                    //Log.d("viewmodal", "dispose effect called")
                }
            }
            AndroidView(
                modifier = modifier
                    .clickable { shouldShowControles = shouldShowControles.not() }
                    .wrapContentSize(Alignment.Center),
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                    }
                }
            )

            PlayerControles(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = .5f)),
                isVisible = { shouldShowControles },
                isPlaying = {
                    //  Log.d("video1" , "isPlaying from playerControles : ${isPlaying}")
                    isPlaying
                },
                title = { " Episode : ${viewModal.currentEpisode} " },
                playbackState = { playbackState },
                onReplayClick = { exoPlayer.seekBack() },
                onForwardClick = { exoPlayer.seekForward() },
                onPauseToggle = {
                    when {
                        exoPlayer.isPlaying -> {
                            exoPlayer.pause()
                        }

                        exoPlayer.isPlaying.not() &&
                                playbackState == STATE_ENDED -> {
                            exoPlayer.seekTo(0)
                            exoPlayer.playWhenReady = true
                        }

                        else -> {
                            exoPlayer.play()
                        }
                    }
                    isPlaying = isPlaying.not()
                },
                totalDuration = { totalDuration },
                currentTime = { 0L },
                bufferedPercentage = { bufferedPercentage },
                viewModel = viewModal,
                onSeekChanged = { timeMs: Float ->
                    exoPlayer.seekTo(timeMs.toLong())
                }
            )
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
            items(viewModal.playQuality , key = {it.quality}){
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
                            playQualityIndex = viewModal.playQuality.indexOf(ep)
                          //  Log.d("Streaming", URL)
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
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .background(brush = Brush.verticalGradient(listOf(Vibrant.copy(.5f), Color.Transparent)))
            , contentPadding = PaddingValues(10.dp)
        ) {
            items(viewModal.AnimeEpisodesIDs , key = {it.id}){
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
                        .background(if ( ep.number == viewModal.currentEpisode ) VibrantDark.copy(.5f) else Vibrant)
                        .clickable {
                            viewModal.updateCurrentEpisode(ep.number)
                            currentEpPlayingID = ep.id
                        },
                    contentAlignment = Alignment.Center,
                ) {

                    Text("Episode "+ ep.number.toString(), modifier = Modifier.padding(start = 5.dp , end = 5.dp), fontSize = 10.sp, color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControles(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    title: () -> String,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    playbackState: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    viewModel: AniplexViewModal,
){
    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier ,
        visible = visible ,
        enter = fadeIn() ,
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(Color.Transparent)){

            TopControles(modifier = modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
                title = title
            )

            CenterControles(modifier= Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
                isPlaying = isPlaying,
                onReplayClick = { onReplayClick() },
                onForwardClick = { onForwardClick() },
                onPauseToggle = { onPauseToggle() },
                playbackState = { playbackState() }
            )
            BottomControles(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .animateEnterExit(
                        enter =
                        slideInVertically(
                            initialOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        ),
                        exit =
                        slideOutVertically(
                            targetOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        )
                    ),
                totalDuration = { totalDuration() },
                currentTime = {currentTime()},
                bufferedPercentage = {bufferedPercentage()},
                onSeekChanged = onSeekChanged,
                viewModel = viewModel
            )
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun CenterControles(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    playbackState: () -> Int,
){

    val isVideoPlaying = remember(isPlaying()) { isPlaying() }.also {
        //Log.d("video1" , "isplaying : ${isPlaying()}")
    }
    val playerState = remember(playbackState()) { playbackState() }.also {
        // Log.d("video1" , " playbackState : ${playbackState()}")
    }

    Row (modifier = modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceEvenly){
        IconButton(modifier = Modifier.size(40.dp), onClick = {onReplayClick()}) {
            Icon(
                painter = painterResource(id = R.drawable.backward_10s),
                contentDescription = "forward",
                tint = Color.White
            )
        }

        if (playerState == STATE_BUFFERING ){
            CircularProgressIndicator(modifier= Modifier.size(40.dp))
        }
        else{
            IconButton(modifier = Modifier.size(40.dp), onClick = {onPauseToggle()}) {
                Icon(
                    painter = when{
                        isVideoPlaying -> {
                            painterResource(id = R.drawable.pause)
                        }
                        isVideoPlaying.not() && playerState == STATE_ENDED -> {
                            painterResource(id= R.drawable.replay)
                        }
                        else -> {
                            painterResource(id = R.drawable.play)
                        }
                    },

                    tint = Color.White,
                    contentDescription = "play/pause",
                )
            }
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = {onForwardClick()}) {
            Icon(
                painter = painterResource(id = R.drawable.forward_15_seconds) ,
                contentDescription = "forward",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TopControles(modifier: Modifier = Modifier , title : () -> String ){
    val videoTitle = remember(title()) { title() }
    Row (modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
        Text(videoTitle, fontSize = 25.sp , textAlign = TextAlign.Start , color = Color.White)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomControles(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    viewModel: AniplexViewModal,
){


    val duration = remember(totalDuration()) { totalDuration() }

    val videoTime by viewModel.debouncedCurrentVideoTime.collectAsState(initial = 0f)

  //  val buffer = remember(bufferedPercentage()) { bufferedPercentage() }

    Column(modifier = modifier.statusBarsPadding()) {
        Box(modifier = modifier.fillMaxWidth()) {
//            Slider(                                   // for  showing buffer percentage
//                modifier = Modifier
//                    .padding(start = 10.dp, end = 10.dp)
//                    .fillMaxWidth(),
//                value = buffer.toFloat(),
//                enabled = false,
//                onValueChange = {},
//                valueRange = 0f..1f,
//                colors =
//                SliderDefaults.colors(
//                    disabledThumbColor = Color.Transparent,
//                    disabledActiveTrackColor = Color.Red
//                )
//            )

            Slider(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                value = videoTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..duration.toFloat(),

            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 25.dp, end = 25.dp)
            ,horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = "${videoTime.toLong().formatMinSec() }/${totalDuration().formatMinSec()}", color = Color.White)

            Box(modifier = Modifier
                .size(33.dp)
                .clickable {
                    viewModel.updateOrientation()
                    // Log.d("viewmodal", viewModel.isLandscape.value.toString() + " from button bottom composable ")
                }
                .wrapContentSize(Alignment.Center)
            ){
                Icon(
                    modifier = Modifier.size(23.dp),
                    painter = painterResource(id = R.drawable.screen_rotation_button) ,
                    contentDescription = "forward",
                    tint = Color.White
                )
            }
        }
    }
}


@SuppressLint("DefaultLocale")
fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -  TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(this)
            )
        )
    }
}



