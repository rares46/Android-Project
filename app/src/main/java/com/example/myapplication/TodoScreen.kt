package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.collectAsState

@Composable
fun TodoScreen(modifier: Modifier = Modifier, viewModel: TodoViewModel) {
    val todoState by viewModel.todo.collectAsState(initial = null)
    var inputId by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TextField for ID input
        OutlinedTextField(
            value = inputId,
            onValueChange = {
                inputId = it
                errorMessage = null // clear error while typing
            },
            label = { Text("Enter Todo ID (1-200)") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        // Error message if ID not valid
        errorMessage?.let { msg ->
            Text(text = msg, color = MaterialTheme.colorScheme.error)
        }

        // Button to fetch todo
        Button(
            onClick = {
                val id = inputId.text.toIntOrNull()
                if (id == null || id !in 1..200) {
                    errorMessage = "Please enter a number between 1 and 200"
                } else {
                    errorMessage = null
                    viewModel.loadTodo(id)
                }
            },
            enabled = inputId.text.isNotBlank()
        ) {
            Text("Load Todo")
        }

        // Display Todo info if available
        todoState?.let { todo ->
            Text(
                text = "Title: ${todo.title}",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = todo.completed,
                    onCheckedChange = { viewModel.toggleCompleted() }
                )
                Text(
                    text = if (todo.completed) "Completed ✅" else "Not completed ❌"
                )
            }
        }
    }
}