package com.example.myapplication
import com.example.myapplication.data.tasks.TasksViewModel
import Pomodoro
import TaskListViewModel
import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.util.Calendar
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph (navHostController: NavHostController, appState: TaskListViewModel = viewModel(), taskViewModel: TasksViewModel) {
    NavHost (navController = navHostController, startDestination = "profile") {
        composable("profile") {
            Tasks(appState = appState, taskViewModel = taskViewModel)
        }
        composable("pomodoro") {
            Pomodoro(appState = appState)
        }
        composable("calendar") {

            val taskList by taskViewModel.tasksByDate
            println(taskList)
            var selectedDate by remember {
                mutableStateOf(LocalDate.now())
            }

            LaunchedEffect(selectedDate) {
                println(11111)
                taskViewModel.getTasksByDate(selectedDate)
            }

            Column() {
                AndroidView(
                    { CalendarView(it).apply {  setOnDateChangeListener {view, year, month, dayOfMonth ->
                        println(dayOfMonth)
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    } } },

                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxWidth(),
                    update = { views ->

                    }
                )

                LazyColumn {
                    itemsIndexed(taskList) {index, task ->
                        TaskItem(task, taskViewModel = taskViewModel)
                    }
                }

            }


        }


    }

}