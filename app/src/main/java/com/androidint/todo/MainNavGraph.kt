package com.androidint.todo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androidint.todo.screens.mainPage.MainPageCompose
import com.androidint.todo.utils.Screens

@Composable
fun MainNavGraph (navHostController : NavHostController) {

    NavHost(navController = navHostController, startDestination = Screens.main_page ){

        composable(route = Screens.main_page){
            MainPageCompose(navHostController = navHostController)
        }
//        composable(route = Screens.splash){
//            val viewModel = hiltViewModel<SignViewModel>()
//            SplashScreen(navController , viewModel)
//        }
    }
}