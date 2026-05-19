package com.example.aplikasilogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplikasilogin.data.local.database.AppDatabase
import com.example.aplikasilogin.data.repository.UserRepository
import com.example.aplikasilogin.ui.screen.LoginScreen
import com.example.aplikasilogin.viewmodel.LoginViewModel
import com.example.aplikasilogin.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        val repository = UserRepository(database.userDao())
        val factory = LoginViewModelFactory(repository)
        setContent {
            val viewModel: LoginViewModel = viewModel(factory = factory)
            androidx.compose.runtime.LaunchedEffect(Unit) {
                viewModel.insertDummyUser()
            }
            LoginScreen(viewModel)
        }
    }
}