package com.example.energymonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.energymonitor.data.repository.FirebaseRepository

@Composable
fun AddConsumptionScreen(navController: NavController) {
    var date by remember { mutableStateOf("") }
    var usage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
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
                        FirebaseRepository.addConsumption(date, usage.toDouble()) { success ->
                            isSaving = false
                            if (success) {
                                navController.popBackStack()
                            } else {
                                errorMessage = "Erro ao salvar o consumo. Tente novamente."
                            }
                        }
                    } else {
                        errorMessage = "Preencha todos os campos!"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
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
