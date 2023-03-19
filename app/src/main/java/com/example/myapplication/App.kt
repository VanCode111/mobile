package com.example.myapplication

import TaskListViewModel
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.tasks.TasksViewModel
import com.example.myapplication.data.tasks.Task
import com.example.myapplication.data.tasks.TasksViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun App() {
    val context = LocalContext.current
    val taskViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(context.applicationContext as Application))
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var navController = rememberNavController()
    val TaskListViewModel: TaskListViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    var showSheet = remember { mutableStateOf(false) }

    LaunchedEffect(showSheet.value) {
        if (showSheet.value) {
            sheetState.show()
            showSheet.value = false
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {BottomBar(navController = navController)},
        floatingActionButton = {
            if (currentRoute == "profile") {
                FloatingActionButton(onClick = { showSheet.value = true }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) {
        NavGraph(navHostController = navController, appState = TaskListViewModel, taskViewModel = taskViewModel)

    }
    Box() {
        BottomSheet(sheetState = sheetState, appState = TaskListViewModel, taskViewModel = taskViewModel)
    }

}

@Composable
fun BottomBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar () {
        NavigationBarItem (
            label = { Text("Задачи") },
            icon = {
                Icon(
                    Icons.Filled.Check,
                contentDescription = "Text")
            },
            onClick = {
                navController.navigate("profile")
            },
            selected = currentRoute == "profile"
        )
        NavigationBarItem (
            label = { Text("Фокус") },
            icon = {
                Icon(
                    Icons.Filled.PlayArrow,
                contentDescription = "Text")
            },
            onClick = {
                navController.navigate("pomodoro")
            },
            selected = currentRoute == "pomodoro"

        )
        NavigationBarItem (
            label = { Text("Календарь") },
            icon = {
                Icon(
                    Icons.Filled.DateRange,
                contentDescription = "Text")
            },
            onClick = {navController.navigate("calendar")},
            selected = currentRoute == "calendar"
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet (sheetState: ModalBottomSheetState, appState: TaskListViewModel = viewModel(),  taskViewModel: TasksViewModel) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    ModalBottomSheetLayout(

        sheetState = sheetState,
        sheetContent = { Column(modifier = Modifier.padding(10.dp),) {
            OutlinedTextField(
                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                label = { Text("Название задачи") }
            )

            Button( modifier = Modifier.fillMaxWidth(),onClick = { taskViewModel.addTask(Task(title = text.text, createdDate = LocalDate.now().format(
                formatter
            )))}) {
                Text (text = "Создать")
            }
        }}
    ) {}


}