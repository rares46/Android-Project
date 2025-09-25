package com.example.myapplication

class TodoRepository(private val api: TodoApiService) {
    suspend fun fetchTodo(id: Int): Todo {
        return api.getTodo(id)
    }

    suspend fun updateTodo(todo: Todo): Todo {
        return api.updateTodo(todo.id, todo)
    }
}
