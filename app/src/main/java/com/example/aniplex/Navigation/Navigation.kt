package com.example.aniplex.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aniplex.UILayer.DetailScreen
import com.example.aniplex.UILayer.HomeScreen
import com.example.aniplex.UILayer.VideoPlayer
import com.example.aniplex.ViewModal.AniplexViewModal

@Composable
fun Navigation( viewModal: AniplexViewModal){

    val navController =  rememberNavController()

    NavHost(navController, startDestination = NavigationRoutes.HOME_SCREEN.toString()) {
        composable(route = NavigationRoutes.HOME_SCREEN.toString()) {
            HomeScreen(viewModal,navController)
        }

        composable(route = NavigationRoutes.DETAIL_SCREEN.toString()+"/{id}" , arguments = listOf(
            navArgument("id"){
                type = NavType.StringType
            }
        )) {
            var animeId = it.arguments?.getString("id")
            DetailScreen(viewModal,navController, animeId)
        }

        composable(route= NavigationRoutes.VIDEOPLAYER_SCREEN.toString()){
            VideoPlayer(viewModal)


        }
    }
}