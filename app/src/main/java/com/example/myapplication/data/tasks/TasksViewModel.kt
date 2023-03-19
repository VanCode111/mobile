package com.example.myapplication.data.tasks

import MyTask
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TasksViewModel (application: Application): AndroidViewModel(application){
    private val taskDatabase = AppDatabase.getInstance(application)

    val tasks: LiveData<List<Task>> = taskDatabase.userDao().getAll().asLiveData()
    val tasksByDate = mutableStateOf<List<Task>>(emptyList())

    fun addTask (task: Task) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            taskDatabase.userDao().insert(task)
        }
    }

    fun updateTaskCompletion (taskId: Int, isCompleted: Boolean) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            taskDatabase.userDao().updateTaskCompletion(taskId = taskId, isCompleted = isCompleted)
        }
    }

    fun delete (task: Task) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            taskDatabase.userDao().delete(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTasksByDate (date: LocalDate) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val formatter = DateTimeFormatter.ofPattern("yyy-MM-dd")
            println(date.format(formatter))
            println(Date().time)
            tasksByDate.value = taskDatabase.userDao().getTaskByDate(date.format(formatter))
        }
    }
}

class TasksViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}