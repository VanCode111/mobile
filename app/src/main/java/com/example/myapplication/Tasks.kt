package com.example.myapplication

import Task
import TaskListViewModel
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem (task: Task) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)) {
        Row() {
            Checkbox(
                checked = task.isChecked.value,
                onCheckedChange = { task.isChecked.value = it }
            )
            Text(text = task.title)
        }
    }

}

@Composable
fun Tasks (appState: TaskListViewModel = viewModel()){
    val taskList = appState.taskList

    LazyColumn {
        itemsIndexed(taskList) {index, task ->
            TaskItem(task)
        }
    }
}

