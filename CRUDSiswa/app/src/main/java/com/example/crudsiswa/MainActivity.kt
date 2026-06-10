package com.example.crudsiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.crudsiswa.data.AppDatabase
import com.example.crudsiswa.ui.MainScreen
import com.example.crudsiswa.ui.theme.CRUDSiswaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dao = AppDatabase.getDatabase(this).siswaDao()

        setContent {
            CRUDSiswaTheme {
                MainScreen(dao = dao)
            }
        }
    }
}