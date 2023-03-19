import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class Task(
    val title: String,

    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)

class TaskListViewModel : ViewModel() {
    private val _taskList = mutableStateListOf<Task>()
    var timeLeftInMillis by mutableStateOf(DEFAULT_TIME)
    var isRunning by mutableStateOf(false)

    val taskList: List<Task>
        get() = _taskList

    fun addTask(task: Task) {
        _taskList.add(task)
    }

    fun removeTask(task: Task) {
        _taskList.remove(task)
    }
}