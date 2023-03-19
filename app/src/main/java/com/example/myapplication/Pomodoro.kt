import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.concurrent.TimeUnit

val DEFAULT_TIME = 1500000L

class PomodoroViewModel : ViewModel() {
    var timeLeftInMillis by mutableStateOf(DEFAULT_TIME)
    var isRunning by mutableStateOf(false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Pomodoro(appState: TaskListViewModel = viewModel()) {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(appState.timeLeftInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(appState.timeLeftInMillis) % 60
    val progress =  1 - (appState.timeLeftInMillis.toFloat() / DEFAULT_TIME)
    val timer = remember {object: CountDownTimer(appState.timeLeftInMillis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            appState.timeLeftInMillis = millisUntilFinished
        }

        override fun onFinish() {
            appState.isRunning = false
        }
    } }

    LaunchedEffect(appState.isRunning) {
        if (appState.isRunning) {
            timer.start()
        }else {
            timer.cancel()
            appState.timeLeftInMillis = DEFAULT_TIME
        }
    }

    DisposableEffect(null) {
        onDispose {
            timer.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.size(200.dp)) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress.toFloat(),
                strokeWidth = 10.dp,

            )
            Text(
                text = "$minutes:${"%02d".format(seconds)}",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { appState.isRunning = true },
                enabled = !appState.isRunning
            ) {
                Text("Старт")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { appState.isRunning = false },
                enabled = appState.isRunning
            ) {
                Text("Стоп")
            }
        }
    }
}
