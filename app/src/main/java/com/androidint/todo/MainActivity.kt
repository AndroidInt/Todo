package com.androidint.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navGraph = rememberNavController()
            val fabVisible = remember { mutableStateOf(true) }
            val snackbarHostState = remember { SnackbarHostState() }
            fabVisible.value = true
            fun showSnackBar(message : String, scope: CoroutineScope){
                scope.launch {
                    snackbarHostState.showSnackbar(message = message,
                        duration = SnackbarDuration.Short )
                }
            }
            TodoTheme {

                // A surface container using the 'background' color from the theme
                Scaffold(

                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },

//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                    ,
                    floatingActionButton = {
                        if (navGraph.currentBackStackEntryAsState().value?.destination?.route == Screens.main_page)
                            FloatingActionButton(onClick = { navGraph.navigate(Screens.add_task)
                            fabVisible.value = !fabVisible.value
                            }) {
                            Text(text = "+")
                            }
                    },
                    content = { paddingValues : PaddingValues ->
                        Surface(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.background,
                            content = {

                            }
                        )
                        MainNavGraph(navHostController = navGraph, showSnackBar = ::showSnackBar)
                    }
                )
            }

        }

    }
}

