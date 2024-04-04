package com.esmasen.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoApp() {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TodoInput(
            taskText = taskText,
            onTaskTextChanged = { taskText = it },
            onAddTask = {
                if (taskText.isNotBlank()) {
                    tasks.add(taskText)
                    taskText = ""
                    keyboardController?.hide()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TodoList(
            tasks = tasks,
            onDeleteTask = { tasks.remove(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoInput(
    taskText: String,
    onTaskTextChanged: (String) -> Unit,
    onAddTask: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskText,
            onValueChange = { onTaskTextChanged(it) },
            label = { Text("Enter a task") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddTask()
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                onAddTask()
                keyboardController?.hide()
            }
        ) {
            Text("Add")
        }
    }
}

@Composable
fun TodoList(
    tasks: List<String>,
    onDeleteTask: (String) -> Unit
) {
    Column {
        tasks.forEach { task ->
            TodoItem(
                task = task,
                onDeleteTask = { onDeleteTask(task) }
            )
        }
    }
}

@Composable
fun TodoItem(
    task: String,
    onDeleteTask: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = task, modifier = Modifier.weight(1f))

        IconButton(onClick = onDeleteTask) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}
