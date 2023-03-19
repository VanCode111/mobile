import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class Task(
    val id: Int,
    val title: String,
    val subtitle: String,
    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)

class TaskListViewModel : ViewModel() {
    private val _taskList = mutableStateListOf<Task>(Task(id = 1, title = "task 1", subtitle = "title"), Task(id = 1, title = "task 1", subtitle = "title"))
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