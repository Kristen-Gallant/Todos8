package gallantgithubapps.todos8


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gallantgithubapps.todos8.addtodolistitem.AddTodoScreen
import gallantgithubapps.todos8.loginregisterscreen.LoginRegisterScreen
import gallantgithubapps.todos8.loginregisterscreen.RegisterScreen
import gallantgithubapps.todos8.models.TodoItem
import gallantgithubapps.todos8.myutilcodes.formatDateRelative
import gallantgithubapps.todos8.onboarding.OnboardingFlowScreen
import gallantgithubapps.todos8.previewtask.TodoScreen
import gallantgithubapps.todos8.splashscreen.SplashScreen
import gallantgithubapps.todos8.ui.theme.Todos8Theme
import gallantgithubapps.todos8.updatetodo.UpdateTodoScreen
import gallantgithubapps.todos8.viewmodel.AllViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Todos8Theme {
                val navController = rememberNavController()
                val viewModel : AllViewModel = viewModel()

                NavHost(navController = navController , startDestination = "splash"){
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("onBoarding") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                    composable("homePage") {

                        Scaffold(
                            modifier = Modifier.fillMaxSize() ,
                            floatingActionButton = {
                                GradientFAB(navController)
                            }
                        ) { innerPadding ->

                            HomePage(
                                modifier = Modifier.padding(innerPadding) ,
                                navController,
                                viewModel = viewModel
                            )
                        }
                    }

                    composable("previewPage") {
                        viewModel.passTodoObject.value?.let { here ->
                                TodoScreen(
                                    todoItem = here,
                                    onEditClick = {
                                        navController.navigate("updateTodoItem")
                                    },

                                    onClickBackButton = {
                                        navController.popBackStack()
                                    },
                                    allViewModel = viewModel,
                                    navController = navController
                                )
                            }
                    }

                    composable("addTodoListItem") {
                        AddTodoScreen({ todoName, todoDescription, selectedDate ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val success = viewModel.createTodo( TodoItem(
                                    title = todoName,
                                    description = todoDescription,
                                    isDone = false,
                                    date = selectedDate
                                ))
                                if (success) {
                                  withContext(Dispatchers.Main){
                                      navController.popBackStack()
                                  }
                                }

                            }
                        })
                    }

                    composable("onBoarding") {
                        Scaffold {paddingValues ->
                            OnboardingFlowScreen(modifier = Modifier.padding(paddingValues), navController = navController , {
                                navController.navigate("loginRegisterScreen")

                            })
                        }
                    }

                    composable("loginRegisterScreen") {
                        val coroutineScope = rememberCoroutineScope()
                        val snackbarHostState = remember { SnackbarHostState() }
                        val context = LocalContext.current

                        Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) { paddingValues ->

                            LoginRegisterScreen(
                                navController = navController,
                                paddingValues = paddingValues,
                                onLoginClick = { email, password ->
                                    coroutineScope.launch {
                                        val success = viewModel.login(email, password)
                                        if (success) {
                                            viewModel. setLoggedIn(context)
                                            navController.navigate("homePage") {
                                                popUpTo("loginRegisterScreen") { inclusive = true }
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar("Wrong credentials")
                                        }
                                    }
                                }
                            )
                        }
                    }


                    composable("registerScreen") {
                        val coroutineScope = rememberCoroutineScope()
                        val context = LocalContext.current

                        RegisterScreen(
                            navController = navController,
                            onRegisterClick = { email, password, confirmPassword ->
                                coroutineScope.launch {
                                    if (password == confirmPassword) {
                                        val success = viewModel.register(email, password)
                                        if (success) {
                                            viewModel. setLoggedIn(context)
                                            navController.navigate("homePage") {
                                                popUpTo("registerScreen") { inclusive = true }
                                            }
                                        } else {

                                        }
                                    } else {

                                    }
                                }
                            }
                        )
                    }

                    composable("updateTodoItem") {
                        val coroutineScope = rememberCoroutineScope()

                        UpdateTodoScreen(
                            todoItem = viewModel.passTodoObject.value ?: TodoItem(0, "", "", false, ""),
                            onUpdate = { updatedItem: TodoItem ->
                                updatedItem.id?.let { id ->
                                    coroutineScope.launch {
                                        val success = viewModel.updateTodoItem(id, updatedItem)
                                        if (success) {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage( modifier: Modifier = Modifier , navController: NavController , viewModel: AllViewModel) {

    val todos = viewModel.todos.collectAsState().value

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = LocalDate.now()

    val groupedTodos = todos.groupBy { it.date }
        .toSortedMap { date1, date2 ->
            val myDateOne = LocalDate.parse(date1, formatter)
            val myDateTwo = LocalDate.parse(date2, formatter)

            when {
                myDateOne.isBefore(today) && !myDateTwo.isBefore(today) -> 1
                !myDateOne.isBefore(today) && myDateTwo.isBefore(today) -> -1
                else -> myDateOne.compareTo(myDateTwo)
            }
        }

    val doneCount = todos.count { it.isDone }

    val undoneCount = todos.count { !it.isDone }

    val upcomingCount = todos.count {
        !it.isDone && LocalDate.parse(it.date, formatter).isAfter(today)
    }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.wrapContentHeight()) {
            DateCard(
                done = doneCount,
                unDone = undoneCount,
                upcoming = upcomingCount
            )
        }
        Column() {
            for (i in groupedTodos){
                TodoListScreen(day = i.key , navController = navController , viewModel = viewModel , todoList = i.value)
            }
        }
    }


}



@Composable
fun GradientFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = {
            navController.navigate("addTodoListItem")
        },
        icon = {
            Icon(Icons.Outlined.Add, contentDescription = "Add Record", tint = Color.White)
        },
        text = {
            Text("Add Todo", color = Color.White)
        },
        expanded = true,
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        containerColor = Color(0xFF002171),
        contentColor = Color.White
    )
}

@Composable
fun GradientFABB(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = {
            navController.navigate("addTodoListItem")
        },
        icon = {
            Icon(Icons.Outlined.Add, contentDescription = "Add Record", tint = Color.White)
        },
        text = {
            Text("Add Todo", color = Color.White)
        },
        expanded = true,
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0D47A1),
                        Color(0xFF002171)
                    )
                ),
                shape = MaterialTheme.shapes.extraLarge
            ),
        containerColor = Color.Transparent
    )
}



@Composable
fun TodoListScreen(modifier: Modifier = Modifier , day : String , navController: NavController , viewModel: AllViewModel , todoList : List<TodoItem>) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.2.dp)
    ) {
        Text(formatDateRelative(day) , fontWeight = FontWeight.Bold , modifier = Modifier.padding(horizontal = 17.dp ).padding(top = 15.dp) , fontSize = 20.sp)

        Column (
            modifier = Modifier
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            todoList.forEach {  data ->
                TodoCard(
                    todo = data,
                    onCheckedChange = {
                        data.id?.let { it1 -> viewModel.markTodoAsDone(it1) }
                    },
                    navController,
                    viewModel
                )
            }
        }
    }
}


@Composable
fun TodoCard(
    todo: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    navController: NavController,
    allViewModel: AllViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                allViewModel.passTodoObject.value = todo
                navController.navigate("previewPage")
            }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { onCheckedChange(!todo.isDone) }) {
                Icon(
                    imageVector = if (todo.isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Mark as done",
                    tint = if (todo.isDone) Color(0xFF4CAF50) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun DateCard(done : Int , unDone : Int , upcoming : Int) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    val formattedDate = currentDate.format(formatter)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
            Row {
                TaskStatItem(
                    icon = Icons.Default.Check,
                    label = "Done",
                    count = done,
                    subLabel = "tasks",
                    color = Color.Green
                )

                TaskStatItem(
                    icon = Icons.Default.LockClock,
                    label = "Pending",
                    count = unDone,
                    subLabel = "tasks",
                    color = Color.Red
                )

                TaskStatItem(
                    icon = Icons.Default.Upload,
                    label = "Upcoming",
                    count = upcoming,
                    subLabel = "tasks",
                    color = Color.Green
                )
            }
        }
    }
}



@Composable
fun TaskStatItem(
    icon: ImageVector,
    label: String,
    count: Int,
    subLabel: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subLabel,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


