package com.example.aniplex.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aniplex.UILayer.CustomVideoPlayer
import com.example.aniplex.UILayer.DetailScreen
import com.example.aniplex.UILayer.HomeScreen
import com.example.aniplex.UILayer.SearchScreen
import com.example.aniplex.UILayer.VideoPlayer
import com.example.aniplex.ViewModal.AniplexViewModal

@Composable
fun Navigation( viewModal: AniplexViewModal){

    val navController =  rememberNavController()

    NavHost(navController, startDestination = NavigationRoutes.HOME_SCREEN.toString()) {
        composable(route = NavigationRoutes.HOME_SCREEN.toString()) {
            HomeScreen(viewModal,navController)
        }

        composable(route = NavigationRoutes.DETAIL_SCREEN.toString()+"/{id}?isFavScreen={isFavScreen}" , arguments = listOf(
            navArgument("id"){
                type = NavType.StringType
            },
            navArgument("isFavScreen"){
                type = NavType.BoolType
            }
        )) {
            var animeId = it.arguments?.getString("id")
            var isFavScreen = it.arguments?.getBoolean("isFavScreen")
            DetailScreen(viewModal,navController, animeId , isFavScreen)
        }

        composable(route= NavigationRoutes.VIDEOPLAYER_SCREEN.toString()){
            CustomVideoPlayer(modifier = Modifier,viewModal)
        }
        composable(route = NavigationRoutes.SEARCH_SCREEN.toString() ) {
            SearchScreen(viewModal,navController)
        }
    }
}