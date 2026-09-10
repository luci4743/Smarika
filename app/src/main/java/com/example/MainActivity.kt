package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.state.AppViewModel
import com.example.ui.components.CelebrationToast
import com.example.ui.components.SmritiTopBar
import com.example.ui.screens.CaregiverScreen
import com.example.ui.screens.DoctorScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PatientScreen
import com.example.ui.theme.SmritiNERTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()
            var showRoleSelector by remember { mutableStateOf(false) }

            SmritiNERTheme(highContrast = state.isHighContrast) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        if (state.currentUser != null && !showRoleSelector) {
                            SmritiTopBar(
                                state = state,
                                viewModel = viewModel,
                                onOpenRoleSelector = { showRoleSelector = true }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (state.currentUser == null || showRoleSelector) {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = { showRoleSelector = false }
                            )
                        } else {
                            when (state.currentUser?.role) {
                                UserRole.PATIENT -> PatientScreen(viewModel = viewModel)
                                UserRole.DOCTOR -> DoctorScreen(viewModel = viewModel)
                                UserRole.CAREGIVER -> CaregiverScreen(viewModel = viewModel)
                                null -> LoginScreen(
                                    viewModel = viewModel,
                                    onLoginSuccess = { showRoleSelector = false }
                                )
                            }
                        }

                        // Floating celebration toast for immediate tactile feedback
                        CelebrationToast(message = state.toastMessage)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
