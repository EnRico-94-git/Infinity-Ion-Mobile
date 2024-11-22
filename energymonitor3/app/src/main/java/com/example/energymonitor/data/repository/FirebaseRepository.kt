package com.example.energymonitor.data.repository

import android.util.Log
import com.example.energymonitor.data.model.Consumption
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addConsumption(date: String, usage: Double, onComplete: (Boolean) -> Unit) {
        val consumption = Consumption(date = date, usage = usage)
        db.collection("consumptions")
            .add(consumption)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRepository", "Erro ao adicionar consumo: ${exception.message}")
                onComplete(false)
            }
    }

    fun getConsumptions(onComplete: (List<Consumption>) -> Unit) {
        db.collection("consumptions")
            .get()
            .addOnSuccessListener { result ->
                val consumptions = result.map { document ->
                    document.toObject(Consumption::class.java).apply { id = document.id }
                }
                onComplete(consumptions)
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRepository", "Erro ao buscar consumos: ${exception.message}")
                onComplete(emptyList())
            }
    }

    fun getConsumptionById(id: String, onComplete: (Consumption?) -> Unit) {
        if (id.isEmpty()) {
            onComplete(null)
            return
        }

        db.collection("consumptions")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                onComplete(document.toObject(Consumption::class.java)?.apply { this.id = document.id })
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRepository", "Erro ao buscar consumo por ID: ${exception.message}")
                onComplete(null)
            }
    }

    fun updateConsumption(id: String?, date: String, usage: Double, onComplete: (Boolean) -> Unit) {
        if (id.isNullOrEmpty()) {
            Log.e("FirebaseRepository", "ID nulo ou vazio na atualização.")
            onComplete(false)
            return
        }

        db.collection("consumptions")
            .document(id)
            .update("date", date, "usage", usage)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRepository", "Erro ao atualizar consumo: ${exception.message}")
                onComplete(false)
            }
    }

    fun deleteConsumption(id: String?, onComplete: (Boolean) -> Unit) {
        if (id.isNullOrEmpty()) {
            Log.e("FirebaseRepository", "ID nulo ou vazio na exclusão.")
            onComplete(false)
            return
        }

        db.collection("consumptions")
            .document(id)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRepository", "Erro ao deletar consumo: ${exception.message}")
                onComplete(false)
            }
    }
}
