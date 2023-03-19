// MyTask.kt
data class MyTask(
    val id: Int,
    val title: String,
    val description: String,
    val deadline: Long,
    val isCompleted: Boolean = false,
    val notes: String = ""
)