package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoViewModel : ViewModel() {

    private val api: TodoApiService = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApiService::class.java)

    private val repository = TodoRepository(api)

    private val _todo = MutableStateFlow<Todo?>(null)
    val todo: StateFlow<Todo?> = _todo

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadTodo(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.fetchTodo(id)
                _todo.value = result
            } catch (e: Exception) {
                _message.value = "Failed to load Todo"
            }
        }
    }

    fun toggleCompleted() {
        _todo.value?.let { current ->
            val updated = current.copy(completed = !current.completed)
            _todo.value = updated

            viewModelScope.launch {
                try {
                    val response = repository.updateTodo(updated)
                    _todo.value = response
                    _message.value = "Todo updated successfully ✅"
                } catch (e: Exception) {
                    _message.value = "Failed to update Todo ❌"
                }
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
