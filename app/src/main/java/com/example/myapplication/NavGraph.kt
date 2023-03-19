package com.example.myapplication

import Pomodoro
import TaskListViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph (navHostController: NavHostController, appState: TaskListViewModel = viewModel()) {
    NavHost (navController = navHostController, startDestination = "profile") {
        composable("profile") {
            Tasks(appState = appState)
        }
        composable("pomodoro") {
            Pomodoro(appState = appState)
        }
    }

}