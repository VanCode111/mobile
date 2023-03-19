package com.example.myapplication

import TaskListViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun App() {
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
        NavGraph(navHostController = navController, appState = TaskListViewModel)

    }
    Box() {
        BottomSheet(sheetState = sheetState)
    }

}

@Composable
fun BottomBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar () {
        NavigationBarItem (
            label = { Text("Профиль") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
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
                    Icons.Filled.DateRange,
                contentDescription = "Text")
            },
            onClick = {
                navController.navigate("pomodoro")
            },
            selected = currentRoute == "pomodoro"

        )
        NavigationBarItem (
            label = { Text("Text") },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                contentDescription = "Text")
            },
            onClick = {selectedItem = 1},
            selected = false
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet (sheetState: ModalBottomSheetState) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
    }

    ModalBottomSheetLayout(

        sheetState = sheetState,
        sheetContent = { Column(modifier = Modifier.padding(10.dp),) {
            OutlinedTextField(

                value = text,
                onValueChange = { text = it },
                label = { Text("Label") }
            )
            Button(onClick = {  }) {
                Text (text = "Создать")
            }
        }}
    ) {}


}