package com.example.energymonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.energymonitor.ui.navigation.AppNavigation
import com.example.energymonitor.ui.theme.EnergymonitorTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            EnergymonitorTheme {
                Surface(color = androidx.compose.material3.MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}
