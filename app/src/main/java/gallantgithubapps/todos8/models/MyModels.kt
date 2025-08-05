package gallantgithubapps.todos8.models

import okhttp3.internal.concurrent.Task

class MyModels {

}

data class TodoItem(
    val id: Int? = null,
    val title: String,
    val description: String,
    var isDone: Boolean = false,
    var date : String
)


data class UserResponse(
    val id: String,
    val username: String,
    val password: String
)


data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterResponse(
    val id: String,
    val username: String,
    val password: String
)

