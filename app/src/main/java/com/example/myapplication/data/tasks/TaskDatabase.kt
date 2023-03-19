package com.example.myapplication.data.tasks

import MyTask
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
data class Task @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "createdDate") var createdDate: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: Int): Task

    @Query("SELECT * FROM task WHERE createdDate = :date")
    fun getTaskByDate(date: String): List<Task>

    @Insert
    fun insert(vararg task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun updateTask(task: Task)

    @Transaction fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        val task = getTaskById(taskId)
        task?.let {task: Task ->
            task.isCompleted = isCompleted
            updateTask(task)
        }
    }
}


@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        fun getInstance (context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

}