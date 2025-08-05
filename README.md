# Todos 8 – Android Todo List App


# Todos 8 is a modern, minimalistic, and high-performing Todo List application built with Jetpack Compose and powered by MockAPI for backend operations. This app is designed to demonstrate seamless integration of CRUD (Create, Read, Update, Delete, Patch) operations using Retrofit and Moshi, all managed under the MVVM (Model-View-ViewModel) architecture for clean, scalable, and maintainable code.

# Features
* ✅ Add, View, Edit, and Delete Tasks with real-time UI updates

* 🔄 Patch existing todos for quick status updates without full replacements

* 📡 MockAPI integration for fast and easy backend testing

* 🎨 Jetpack Compose UI with Column, Row, and VStack for a sleek, intuitive design

* 📂 MVVM Architecture ensuring separation of concerns and scalability

* 🌐 Retrofit + Moshi for efficient networking and JSON parsing

* 🧭 Navigation Component with NavController and NavHost for smooth screen transitions

* 📱 Modern, clean, and lightweight design for a great user experience

# API Endpoints (MockAPI)
* GET /todos – Fetch all todos

* POST /todos – Add a new todo

* PUT /todos/{id} – Update an existing todo

* PATCH /todos/{id} – Partially update a todo (e.g., mark as done)

* DELETE /todos/{id} – Remove a todo permanently

* ## Demo Video

# Clone the repository
* git clone https://github.com/kristen-gallant/todos8.git
* Open in Android Studio
* Sync Gradle to download dependencies
* Replace the MockAPI base URL in RetrofitInstance with your own endpoint if needed
* Run the app on an emulator or physical device



# How It Works
* Todos 8 is designed to give users a smooth and intuitive experience for managing daily tasks. Below is a quick overview of how the app works:

# Register an Account
* When opening the app for the first time, users are prompted to create an account.

* Basic details such as username, email, and password are collected.

* Registration data is sent securely to the MockAPI backend for storage.


# Login to Access Todos
* Once registered, users can log in using their credentials.

* Successful login grants access to their personalized todo list.

* The session is maintained while the app is open, ensuring seamless navigation.



# View All Todos
* The Home Screen displays all todos fetched from MockAPI using a GET request.

* Todos are listed with key details such as:

* Title

* Description

* Due Date

* Completion Status


# Edit Existing Todo
* Users can select any todo item to update its details.

* Editing allows changes to:

* Title

* Description

* Due Date

* Completion Status

# Delete Todo
* Unwanted tasks can be removed permanently with a single tap.

* A DELETE request is made to the backend, removing the task and updating the list in real time.


<img src="https://raw.githubusercontent.com/Kristen-Gallant/Todos8/master/homePage.png" alt="Alt Text" width="300" /> <img src="https://raw.githubusercontent.com/Kristen-Gallant/Todos8/master/onBoarding.png" alt="Alt Text" width="300" />

<img src="https://raw.githubusercontent.com/Kristen-Gallant/Todos8/master/splashScreen.png" alt="Alt Text" width="300" /> <img src="https://raw.githubusercontent.com/Kristen-Gallant/Todos8/master/updateTodo.png" alt="Alt Text" width="300" />



<a href="https://youtu.be/zogbelBjTe8">
  <img src="https://img.youtube.com/vi/zogbelBjTe8/maxresdefault.jpg" alt="Video Title" width="500" />  
</a>





<a href="https://youtube.com/shorts/BA5_XK6ZoRQ?feature=share">
  <img src="https://img.youtube.com/vi/BA5_XK6ZoRQ/maxresdefault.jpg" alt="Video Title" width="500" />
</a>









