package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.state.AppViewModel
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onLoginSuccess: () -> Unit
) {
    var authMode by remember { mutableStateOf("demo") } // "demo", "signup", "login"
    var selectedRole by remember { mutableStateOf(UserRole.PATIENT) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var doctorId by remember { mutableStateOf("") }
    var patientId by remember { mutableStateOf("patient-ner-001") }
    var relationship by remember { mutableStateOf("Son") }
    var specialty by remember { mutableStateOf("Geriatric Neurologist") }
    var hospital by remember { mutableStateOf("GNRC Medical Guwahati") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasCream)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Brand Banner
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(Emerald900, Teal800, Emerald950)))
                .border(2.dp, Amber400.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "স্মৃ", color = Amber200, fontSize = 28.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "SmritiNER Care Portal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextDark
        )
        Text(
            text = "Culturally grounded cognitive health for Patients, Doctors & Caregivers",
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card Container
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Auth Mode Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F3EF))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AuthTabButton(
                        title = "⚡ Demo",
                        isSelected = authMode == "demo",
                        modifier = Modifier.weight(1f)
                    ) { authMode = "demo" }

                    AuthTabButton(
                        title = "Sign Up",
                        isSelected = authMode == "signup",
                        modifier = Modifier.weight(1f)
                    ) { authMode = "signup" }

                    AuthTabButton(
                        title = "Sign In",
                        isSelected = authMode == "login",
                        modifier = Modifier.weight(1f)
                    ) { authMode = "login" }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (authMode == "demo") {
                    // One-Click Demo Mode
                    Text(
                        text = "Instant 1-Click Portals",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Experience any dedicated role with sample data and live Firestore connectivity:",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Patient Demo Card
                    DemoRoleCard(
                        title = "Patient Portal",
                        subtitle = "Aita Pratima Devi",
                        description = "Daily memory routines, cultural games (Bihu Dhol, Gamosa), Smriti AI voice companion & doctor chat.",
                        icon = "👵",
                        color = Emerald700,
                        onClick = {
                            viewModel.loginDemoPatient()
                            onLoginSuccess()
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Doctor Demo Card
                    DemoRoleCard(
                        title = "Doctor Portal",
                        subtitle = "Dr. Bhaskar Hazarika, MD",
                        description = "Patient roster, clinical progress logs to Firestore, longitudinal cognitive tracking & direct patient chat.",
                        icon = "🩺",
                        color = Sky700,
                        onClick = {
                            viewModel.loginDemoDoctor()
                            onLoginSuccess()
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Caregiver Demo Card
                    DemoRoleCard(
                        title = "Caregiver Portal",
                        subtitle = "Rahul Borah (Son)",
                        description = "Elder status, 92% adherence vitals, safe geofence, medication reminders & family notes.",
                        icon = "❤️",
                        color = Rose700,
                        onClick = {
                            viewModel.loginDemoCaregiver()
                            onLoginSuccess()
                        }
                    )
                } else {
                    // Custom Sign Up / Sign In Form
                    Text(
                        text = "1. Choose Account Type",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RoleSelectBox(
                            title = "Patient",
                            icon = "👵",
                            isSelected = selectedRole == UserRole.PATIENT,
                            color = Emerald700,
                            modifier = Modifier.weight(1f)
                        ) { selectedRole = UserRole.PATIENT }

                        RoleSelectBox(
                            title = "Doctor",
                            icon = "🩺",
                            isSelected = selectedRole == UserRole.DOCTOR,
                            color = Sky700,
                            modifier = Modifier.weight(1f)
                        ) { selectedRole = UserRole.DOCTOR }

                        RoleSelectBox(
                            title = "Caregiver",
                            icon = "❤️",
                            isSelected = selectedRole == UserRole.CAREGIVER,
                            color = Rose700,
                            modifier = Modifier.weight(1f)
                        ) { selectedRole = UserRole.CAREGIVER }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "2. Profile Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text(if (selectedRole == UserRole.PATIENT) "e.g. Pratima Devi (Aita)" else if (selectedRole == UserRole.DOCTOR) "e.g. Dr. Bhaskar Hazarika" else "e.g. Rahul Borah") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        placeholder = { Text("user@smritiner.care") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (selectedRole == UserRole.PATIENT) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = doctorId,
                            onValueChange = { doctorId = it },
                            label = { Text("Assigned Doctor ID (Optional)") },
                            placeholder = { Text("e.g. doctor-001") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    if (selectedRole == UserRole.DOCTOR) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = specialty,
                            onValueChange = { specialty = it },
                            label = { Text("Clinical Specialty") },
                            placeholder = { Text("Geriatric Neurologist") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    if (selectedRole == UserRole.CAREGIVER) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = relationship,
                            onValueChange = { relationship = it },
                            label = { Text("Relationship to Elder") },
                            placeholder = { Text("Son / Daughter / Spouse") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.loginCustom(
                                role = selectedRole,
                                name = name,
                                email = email,
                                doctorId = doctorId,
                                patientId = patientId,
                                relationship = relationship,
                                specialty = specialty
                            )
                            onLoginSuccess()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (selectedRole) {
                                UserRole.PATIENT -> Emerald700
                                UserRole.DOCTOR -> Sky700
                                UserRole.CAREGIVER -> Rose700
                            }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (authMode == "signup") "Create Account & Enter" else "Sign In & Enter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthTabButton(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Emerald800 else TextMuted,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DemoRoleCard(
    title: String,
    subtitle: String,
    description: String,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.06f),
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
                    Text(text = "Enter →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
                }
                Text(text = subtitle, fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextDark)
                Text(text = description, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
fun RoleSelectBox(
    title: String,
    icon: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.1f) else Color.White,
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) color else BorderSubtle
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else TextDark
            )
        }
    }
}
