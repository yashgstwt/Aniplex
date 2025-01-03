package com.example.aniplex.UILayer

import com.example.aniplex.R
import com.example.aniplex.ViewModal.AniplexViewModal
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.metrics.PlaybackStateEvent.STATE_ENDED
import android.media.metrics.PlaybackStateEvent.STATE_NOT_STARTED
import android.media.metrics.PlaybackStateEvent.STATE_STOPPED
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.aniplex.ViewModal.GetStreamingData
import com.example.aniplex.ui.theme.Vibrant
import com.example.aniplex.ui.theme.VibrantDark
import com.example.aniplex.ui.theme.black
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

const val PLAYER_SEEK_BACK_INCREMENT = 10 * 1000L // 10 seconds
const val PLAYER_SEEK_FORWARD_INCREMENT = 15 * 1000L // 15 seconds
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun CustomVideoPlayer(
    modifier: Modifier = Modifier,
    viewModal: AniplexViewModal,
    navController: NavHostController,
) {
    var isVideoVisible by remember { mutableStateOf(true) }
    var currentEpPlayingID by remember { mutableStateOf(viewModal.AnimeEpisodesIDs[viewModal.currentEpisode].id) }
    var URL by remember { mutableStateOf(viewModal.playQuality[0].url) }
    var currentPosition by remember { mutableLongStateOf(0L) } // holds the current position of video
    var shouldSeek by remember { mutableStateOf(false) }

    LaunchedEffect (currentEpPlayingID){
        viewModal.getStreamingLink(currentEpPlayingID, viewModal.playbackServer)
    }

    val systemUiController: SystemUiController = rememberSystemUiController()
    val context = LocalContext.current
    val activity = context as? Activity
    var height by remember { mutableFloatStateOf(.27f) }

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
                URL = viewModal.playQuality[0].url
            }
        }
    }


    LaunchedEffect(URL) {
        Log.d("epsodeee", " URL : $URL")
        if (URL.isNotEmpty()) {
            val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
            val uri = Uri.Builder().encodedPath(URL).build() // streaming url
            val hlsMediaItem = MediaItem.Builder().setUri(uri).build()
            val mediaSource =
                HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem)
            viewModal.exoPlayer.setMediaSource(mediaSource)
            viewModal.exoPlayer.prepare()
            viewModal.exoPlayer.playWhenReady = true
            if (shouldSeek) {
                Log.d("epsodeee", " INSIDE URL   current position : ${viewModal.exoPlayer.currentPosition} , currentPositionVar : ${currentPosition}")
                viewModal.exoPlayer.seekTo(currentPosition)
                shouldSeek = false
            }
        }
    }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val observer = LifecycleEventObserver { owner, event ->
        if (event ==  Lifecycle.Event.ON_PAUSE ) {
            viewModal.exoPlayer.pause()
        }
    }
    val lifecycle = lifecycleOwner.value.lifecycle
    lifecycle.addObserver(observer)
    var shouldShowControles by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(viewModal.exoPlayer.isPlaying) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    var bufferedPercentage by remember { mutableIntStateOf(0) }
    var playbackState by remember { mutableStateOf(viewModal.exoPlayer.playbackState) }
    val brush: List<Color> = listOf(VibrantDark, black)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(brush))

    ) {

        Box(
            modifier = modifier.statusBarsPadding()
                .fillMaxWidth()
                .fillMaxHeight(height),
            contentAlignment = Alignment.BottomCenter

        ) {
            val listener = object : Player.Listener {

                override fun onEvents(
                    player: Player,
                    events: Player.Events,
                ) {

                    //Log.d("logee" , "onEvents called ${player.currentPosition}")
                    super.onEvents(player, events)
                    totalDuration = player.duration.coerceAtLeast(0L)
                    bufferedPercentage = player.bufferedPercentage
                    isPlaying = player.isPlaying
                    playbackState = player.playbackState
                }

            }

            if (isPlaying) {
                LaunchedEffect(Unit) {
                    while(true) {
                        viewModal.currentVideoTime.update {
                            viewModal.exoPlayer.getCurrentPosition().coerceAtLeast(0L)
                        }
                        delay(1.seconds )
                    }
                }
            }

            viewModal.exoPlayer.addListener(listener)

            BackHandler {
                if(viewModal.isLandscape.value){
                    viewModal.updateOrientation()
                } else {
                    navController.popBackStack()
                    isVideoVisible = false // Hide the video view
                    viewModal.exoPlayer.pause()
                    viewModal.playQuality[0].url = "default null "
                    viewModal.updateCurrentEpisode(0)
                    viewModal.exoPlayer.removeListener(listener)
                    lifecycle.removeObserver(observer)
                }
            }


            if (isVideoVisible) {

            AndroidView(
                modifier = Modifier
                    .clickable {
                        shouldShowControles = shouldShowControles.not()
                        systemUiController.isNavigationBarVisible =
                            systemUiController.isNavigationBarVisible.not()
                        systemUiController.isSystemBarsVisible =
                            systemUiController.isSystemBarsVisible.not()
                        systemUiController.isSystemBarsVisible =
                            systemUiController.isSystemBarsVisible.not()
                    }
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.BottomCenter),
                factory = {
                    PlayerView(context).apply {
                        player = viewModal.exoPlayer
                        useController = false
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        keepScreenOn = true
                    }
                },
            )
            }

            PlayerControles(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = .3f))
                   ,
                isVisible = { shouldShowControles },
                isPlaying = {
                    isPlaying
                },
                title = { " Episode : ${viewModal.currentEpisode} " },
                playbackState = { playbackState },
                onBackwardClick = { viewModal.exoPlayer.seekBack() },
                onForwardClick = { viewModal.exoPlayer.seekForward() },
                onPauseToggle = {
                    when {
                        viewModal.exoPlayer.isPlaying -> {
                            viewModal.exoPlayer.pause()
                        }

                        viewModal.exoPlayer.isPlaying.not() &&
                                playbackState == STATE_ENDED -> {
                            viewModal.exoPlayer.seekTo(0)
                            viewModal.exoPlayer.playWhenReady = true
                        }

                        else -> {
                            viewModal.exoPlayer.play()
                        }
                    }
                    isPlaying = isPlaying.not()
                },
                totalDuration = { totalDuration },
                bufferedPercentage = { bufferedPercentage },
                viewModel = viewModal,
                onSeekChanged = { timeMs: Float ->
                    viewModal.exoPlayer.seekTo(timeMs.toLong())
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
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .height(height = 55.dp)
                        .background(Vibrant.copy(.5f))
                        .clickable {
                            Log.d("epsodeee", " BEFORE  current position : ${viewModal.exoPlayer.currentPosition} , currentPositionVar : ${currentPosition}")
                            currentPosition = viewModal.exoPlayer.currentPosition
                            Log.d("epsodeee", " AFTER  current position : ${viewModal.exoPlayer.currentPosition} , currentPositionVar : ${currentPosition}")
                            URL = ep.url
                            viewModal.exoPlayer.playWhenReady = true
                            shouldSeek = true
                            Log.d("epsodeee",shouldSeek.toString())
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        ep.quality,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        fontSize = 15.sp,
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
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Vibrant.copy(.5f),
                        Color.Transparent
                    )
                )
            )
            , contentPadding = PaddingValues(10.dp)
        ) {
            items(viewModal.AnimeEpisodesIDs , key = {it.id}){
                    ep->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 70.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )

                        .clickable {
                            Log.d("epsodeee", "ep id : ${ep.number}  currentEp : ${viewModal.currentEpisode}")
                            viewModal.updateCurrentEpisode(ep.number)
                            currentEpPlayingID = ep.id
                            viewModal.exoPlayer.seekTo(0)
                            viewModal.exoPlayer.playWhenReady = true
                        }
                        .background(
                            if (ep.number  == viewModal.currentEpisode) VibrantDark.copy(
                                .5f
                            ) else Vibrant
                        ),
                    contentAlignment = Alignment.Center,
                ) {

                    Text("Episode "+ ep.number.toString(), modifier = Modifier.padding(start = 5.dp , end = 5.dp), fontSize = 10.sp, color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerControles(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    title: () -> String,
    onBackwardClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    totalDuration: () -> Long,
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
                onBackwardClick = { onBackwardClick() },
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
    onBackwardClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    playbackState: () -> Int,
){

    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
//        .also {
//        //Log.d("video1" , "isplaying : ${isPlaying()}")
//    }
    val playerState = remember(playbackState()) { playbackState() }
//        .also {
//        // Log.d("video1" , " playbackState : ${playbackState()}")
//    }

    Row (modifier = modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceEvenly){
        IconButton(modifier = Modifier.size(40.dp), onClick = {onBackwardClick()}) {
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
    Row (modifier = modifier.fillMaxWidth().statusBarsPadding(), horizontalArrangement = Arrangement.Center){
        Text(videoTitle, fontSize = 15.sp , textAlign = TextAlign.Center , color = Color.White)
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun BottomControles(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    bufferedPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    viewModel: AniplexViewModal,
){


    val duration = remember(totalDuration()) { totalDuration() }

    val videoTime by viewModel.currentVideoTime.collectAsState(initial = 0f)


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
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxWidth(),
                value = videoTime.toFloat(),
                onValueChange = {
                    onSeekChanged(it)
                   viewModel.updateCurrentVideoTime(it.toLong())
                                },
                valueRange = 0f..duration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    activeTickColor = Color.White,
                    inactiveTickColor = Color.White
                )
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



