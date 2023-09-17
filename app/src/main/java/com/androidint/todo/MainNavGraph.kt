package com.androidint.todo

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androidint.todo.screens.mainPage.MainPageCompose
import com.androidint.todo.screens.mainPage.MainPageViewModel
import com.androidint.todo.utils.Screens

@Composable
fun MainNavGraph (navHostController : NavHostController) {

    NavHost(navController = navHostController, startDestination = Screens.main_page ){

        composable(route = Screens.main_page){
            val viewModel = hiltViewModel<MainPageViewModel>()
            MainPageCompose(
                navHostController,
                viewModel
            )
        }
    }
}