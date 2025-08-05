package gallantgithubapps.todos8.mynetwork

import gallantgithubapps.todos8.models.RegisterRequest
import gallantgithubapps.todos8.models.RegisterResponse
import gallantgithubapps.todos8.models.TodoItem
import gallantgithubapps.todos8.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApi {

    @GET("users")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<List<UserResponse>>


    @POST("users")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>


    @GET("todos")
    suspend fun getTodos() : Response<List<TodoItem>>


    @POST("todos")
    suspend fun addTodo(
        @Body todo: TodoItem
    ): Response<TodoItem>


    @DELETE("todos/{id}")
    suspend fun deleteTodo(
        @Path("id") id: Int
    ): Response<Unit>


    @PATCH("todos/{id}")
    suspend fun updateTodoStatus(
        @Path("id") id: Int,
        @Body updateData: Map<String, Boolean>
    ): Response<TodoItem>


    @PATCH("todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: Int,
        @Body updatedTodo: TodoItem
    ): Response<TodoItem>
}