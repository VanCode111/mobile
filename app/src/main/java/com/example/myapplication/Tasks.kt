package com.example.myapplication

import TaskListViewModel
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import com.example.myapplication.data.tasks.TasksViewModel

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.example.myapplication.data.tasks.Task
import com.example.myapplication.data.tasks.TasksViewModelFactory
import org.w3c.dom.Text
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskItem (task: Task, taskViewModel: TasksViewModel) {
    val swipeableState = rememberSwipeableState(0)
    val squareSize = 90.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    val isSwiping = swipeableState.progress.from > 0 || swipeableState.progress.to > 0
    println(task)

    Box(modifier = Modifier.swipeable(state = swipeableState, anchors = anchors, orientation = Orientation.Horizontal)) {
        Row(modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(horizontal =  15.dp, vertical = 6.dp)
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }) {

            if(isSwiping) {
                Button(onClick = { taskViewModel.delete(task) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red),modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(end = 5.dp)
                    ) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
                Box(modifier = Modifier,)
            }
            Card(modifier = Modifier
                .fillMaxWidth()

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {check: Boolean -> taskViewModel.updateTaskCompletion(taskId = task.id, isCompleted = check) }
                    )
                    Text(text = task.title)
                }
            }

        }

    }


}

@Composable
fun Tasks (appState: TaskListViewModel = viewModel(), taskViewModel: TasksViewModel){
    val taskList by taskViewModel.tasks.observeAsState(emptyList())

    LazyColumn {
        itemsIndexed(taskList) {index, task ->
            TaskItem(task, taskViewModel = taskViewModel)
        }
    }
}

