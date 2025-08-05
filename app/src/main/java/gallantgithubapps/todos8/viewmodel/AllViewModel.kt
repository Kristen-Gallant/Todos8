package gallantgithubapps.todos8.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gallantgithubapps.todos8.models.RegisterRequest
import gallantgithubapps.todos8.models.TodoItem
import gallantgithubapps.todos8.mynetwork.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class AllViewModel(application: Application) : AndroidViewModel(application) {
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos


    val passTodoObject = mutableStateOf<TodoItem?>(null)

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            getItems()
        }
    }





    suspend fun login(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitObject.apiService.loginUser(username, password)
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {

                    true
                } else {

                    false
                }
            } catch (e: Exception) {

                false
            }
        }
    }

    fun getItems() {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.apiService.getTodos()
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    _todos.value = response.body()!!
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }


    suspend fun createTodo(newTodo: TodoItem): Boolean {
        return try {
            val response = RetrofitObject.apiService.addTodo(newTodo)
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    _todos.value += response.body()!!
                }
                true
            } else {

                false
            }
        } catch (e: Exception) {
            false
        }
    }




    suspend fun deleteTodoItem(id: Int): Boolean {
        return try {
            val response = RetrofitObject.apiService.deleteTodo(id)

            if (response.isSuccessful) {
                _todos.value = _todos.value.filter { it.id != id }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }


    fun markTodoAsDone(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.apiService.updateTodoStatus(
                    id,
                    mapOf("isDone" to true)
                )

                if (response.isSuccessful && response.body() != null) {
                    val updatedTodo = response.body()!!
                    _todos.value = _todos.value.map {
                        if (it.id == id) updatedTodo else it
                    }
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }



    suspend fun updateTodoItem(id: Int, updatedTodo: TodoItem): Boolean {
        return try {
            val response = RetrofitObject.apiService.updateTodo(id, updatedTodo)

            if (response.isSuccessful && response.body() != null) {
                val updatedData = response.body()!!
                _todos.value = _todos.value.map {
                    if (it.id == id) updatedData else it
                }
                passTodoObject.value = updatedTodo
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }



    suspend fun register(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitObject.apiService.registerUser(
                    RegisterRequest(username, password)
                )
                if (response.isSuccessful) {
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }


    fun setLoggedIn(context: Context) {
        val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        sharedPref.edit {
            putBoolean("logged_in", true)
        }
    }





}