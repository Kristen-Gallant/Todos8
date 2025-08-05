package gallantgithubapps.todos8.updatetodo


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gallantgithubapps.todos8.models.TodoItem
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTodoScreen(
    todoItem: TodoItem,
    onUpdate: (TodoItem) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var todoName by remember { mutableStateOf(todoItem.title) }
    var todoDescription by remember { mutableStateOf(todoItem.description) }
    var isDone by remember { mutableStateOf(todoItem.isDone) }
    var selectedDate by remember { mutableStateOf(todoItem.date) }

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val dayFormatted = String.format("%02d", day)
            val monthFormatted = String.format("%02d", month + 1)
            selectedDate = "$dayFormatted/$monthFormatted/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Update To-Do", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = todoName,
                onValueChange = { todoName = it },
                label = { Text("Task Name") },
                leadingIcon = { Icon(Icons.Default.TaskAlt, contentDescription = "Task Icon") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            OutlinedTextField(
                value = todoDescription,
                onValueChange = { todoDescription = it },
                label = { Text("Description") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = "Description Icon") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 10.dp),
                maxLines = 4
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable { datePicker.show() },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = selectedDate,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { isDone = it }
                )
                Text("Mark as Done", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    when {
                        todoName.isBlank() -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Task Name is missing!") }
                        }
                        todoDescription.isBlank() -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Description is missing!") }
                        }
                        else -> {
                            onUpdate(
                                todoItem.copy(
                                    title = todoName,
                                    description = todoDescription,
                                    isDone = isDone,
                                    date = selectedDate
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00796B),
                                Color(0xFF004D40)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Update", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Update Task",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

class UpdateTodoScreen {

}