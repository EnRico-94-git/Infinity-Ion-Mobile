package com.example.energymonitor.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.energymonitor.data.repository.FirebaseRepository

@Composable
fun EditConsumptionScreen(navController: NavController, consumptionId: String) {
    var date by remember { mutableStateOf("") }
    var usage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(consumptionId) {
        FirebaseRepository.getConsumptionById(consumptionId) { consumption ->
            if (consumption != null) {
                date = consumption.date
                usage = consumption.usage.toString()
            } else {
                Toast.makeText(context, "Consumo não encontrado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = usage,
            onValueChange = { usage = it },
            label = { Text("Consumo (kWh)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isSaving) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = {
                    if (date.isNotEmpty() && usage.isNotEmpty()) {
                        isSaving = true
                        FirebaseRepository.updateConsumption(consumptionId, date, usage.toDouble()) { success ->
                            isSaving = false
                            if (success) {
                                Toast.makeText(context, "Consumo atualizado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                errorMessage = "Erro ao atualizar o consumo."
                            }
                        }
                    } else {
                        errorMessage = "Preencha todos os campos!"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isDeleting) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = {
                    isDeleting = true
                    FirebaseRepository.deleteConsumption(consumptionId) { success ->
                        isDeleting = false
                        if (success) {
                            Toast.makeText(context, "Consumo excluído", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            errorMessage = "Erro ao excluir o consumo."
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Excluir Consumo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
