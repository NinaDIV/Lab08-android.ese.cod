package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch
import com.example.lab08.ui.theme.Lab08Theme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08Theme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()


                val taskDao = db.taskDao()
                val viewModel = TaskViewModel(taskDao)


                TaskScreen(viewModel)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var newTaskDescription by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var editedDescription by remember { mutableStateOf("") }

    // Filtros
    var showCompleted by remember { mutableStateOf(true) }
    var showPending by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(SortOption.BY_NAME) }

    // Filtrar y ordenar tareas
    val filteredTasks = tasks.filter { task ->
        (showCompleted && task.isCompleted) || (showPending && !task.isCompleted)
    }.filter { task ->
        task.description.contains(searchQuery, ignoreCase = true)
    }.let { filtered ->
        when (sortOption) {
            SortOption.BY_NAME -> filtered.sortedBy { it.description }
            SortOption.BY_DATE -> filtered.sortedBy { it.id } //este no aplica
            SortOption.BY_STATUS -> filtered.sortedBy { it.isCompleted }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newTaskDescription.isNotEmpty()) {
                        viewModel.addTask(newTaskDescription)
                        newTaskDescription = ""
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(18.dp)
            ) {
                item {
                    Text(
                        text = "Gestión de Tareas",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Campo para agregar nueva tarea
                item {
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text("Nueva tarea") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                // Barra de búsqueda
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar tareas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                // Filtros
                item {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(), // Asegúrate de que la fila ocupe todo el ancho disponible
                        horizontalArrangement = Arrangement.Center, // Centrar el contenido
                        verticalAlignment = Alignment.CenterVertically // Alinear verticalmente al centro
                    ) {
                        Text("Completadas")
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(
                            checked = showCompleted,
                            onCheckedChange = { showCompleted = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Gray.copy(alpha = 0.8f), // Color del pulgar cuando está activado
                                uncheckedThumbColor = Color.Gray.copy(alpha = 0.5f), // Color del pulgar cuando está desactivado
                                checkedTrackColor = Color.LightGray.copy(alpha = 0.5f), // Color de la pista cuando está activado
                                uncheckedTrackColor = Color.LightGray.copy(alpha = 0.2f) // Color de la pista cuando está desactivado
                            ),
                            modifier = Modifier.size(30.dp) // Tamaño del switch
                        )

                        Spacer(modifier = Modifier.width(28.dp))
                        Text("Pendientes")
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(
                            checked = showPending,
                            onCheckedChange = { showPending = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Gray.copy(alpha = 0.8f),
                                uncheckedThumbColor = Color.Gray.copy(alpha = 0.5f),
                                checkedTrackColor = Color.LightGray.copy(alpha = 0.5f),
                                uncheckedTrackColor = Color.LightGray.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.size(30.dp) // Tamaño del switch
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }



                // Menú para ordenar tareas
                item {

                    ExposedDropdownMenuBox(
                        expanded = false, // Cambiar a una variable para manejar el estado de apertura
                        onExpandedChange = { /* Cambiar estado aquí */ }

                    ) {
                        Text("Ordenar por: ${sortOption.label}")
                        // Aquí puedes mostrar el valor actual
                        // Aquí puedes implementar el menú para elegir el método de ordenación
                    }
                }

                // Lista de tareas filtradas y ordenadas
                filteredTasks.forEach { task ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .shadow(4.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                // Si está en modo edición
                                if (isEditing && taskToEdit?.id == task.id) {
                                    OutlinedTextField(
                                        value = editedDescription,
                                        onValueChange = { editedDescription = it },
                                        label = { Text("Editar tarea") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    IconButton(
                                        onClick = {
                                            viewModel.editTask(task.copy(description = editedDescription))
                                            isEditing = false
                                            editedDescription = ""
                                            taskToEdit = null
                                        }
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                                    }
                                } else {
                                    Text(
                                        text = task.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = Int.MAX_VALUE,
                                        overflow = TextOverflow.Clip,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                isEditing = true
                                                taskToEdit = task
                                                editedDescription = task.description
                                            },
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteTask(task) },
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                        }

                                        IconButton(
                                            onClick = { viewModel.toggleTaskCompletion(task) },
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = if (task.isCompleted) "Completada" else "Pendiente",
                                                tint = if (task.isCompleted) Color.Green else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Botón para eliminar todas las tareas
                item {
                    Button(
                        onClick = { coroutineScope.launch { viewModel.deleteAllTasks() } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar todas las tareas")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar todas las tareas")
                    }
                }
            }
        }
    )
}

// Enum para opciones de ordenación
enum class SortOption(val label: String) {
    BY_NAME("Nombre"),
    BY_DATE("Fecha"),
    BY_STATUS("Estado")
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    // Creación de un ViewModel simulado para la vista previa
    val viewModel = TaskViewModel(MockTaskDao())
    TaskScreen(viewModel)
}

// Implementación de un DAO simulado para las pruebas de vista previa
class MockTaskDao : TaskDao {
    private val tasksList = mutableListOf(
        Task(id = 1, description = "Tarea 1", isCompleted = false),
        Task(id = 2, description = "Tarea 2", isCompleted = true),
        Task(id = 3, description = "Tarea 3", isCompleted = false)
    )

    override suspend fun getAllTasks(): List<Task> {
        return tasksList
    }

    override suspend fun insertTask(task: Task) {
        tasksList.add(task)
    }

    override suspend fun updateTask(task: Task) {
        tasksList.replaceAll { if (it.id == task.id) task else it }
    }

    override suspend fun deleteAllTasks() {
        tasksList.clear()
    }

    override suspend fun deleteTask(taskId: Int) {
        tasksList.removeIf { it.id == taskId }
    }
}
