package com.example.energymonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.energymonitor.data.model.Consumption
import com.example.energymonitor.data.repository.FirebaseRepository

@Composable
fun ConsumptionHistoryScreen(navController: NavController) {
    val consumptions = remember { mutableStateListOf<Consumption>() }
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        FirebaseRepository.getConsumptions { fetchedConsumptions ->
            consumptions.clear()
            consumptions.addAll(fetchedConsumptions)
        }
    }

    if (showSnackbar.value) {
        Snackbar {
            Text(snackbarMessage.value)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Histórico de Consumo", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(consumptions) { consumption ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Data: ${consumption.date}")
                        Text("Consumo: ${consumption.usage} kWh")
                    }
                    Row {
                        IconButton(onClick = {
                            navController.navigate("edit_consumption_screen/${consumption.id}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Consumo",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = {
                            consumption.id?.let { id ->
                                FirebaseRepository.deleteConsumption(id) { success ->
                                    if (success) {
                                        snackbarMessage.value = "Consumo excluído com sucesso!"
                                        consumptions.remove(consumption)
                                    } else {
                                        snackbarMessage.value = "Erro ao excluir o consumo."
                                    }
                                    showSnackbar.value = true
                                }
                            } ?: run {
                                snackbarMessage.value = "ID inválido para exclusão."
                                showSnackbar.value = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir Consumo",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
