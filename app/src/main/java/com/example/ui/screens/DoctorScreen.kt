package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.AppViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DoctorScreen(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()
    var newLogText by remember { mutableStateOf("") }
    var chatInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.isHighContrast) HighContrastBg else CanvasCream)
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Doctor Portal Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Sky100),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🩺", fontSize = 24.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(state.currentUser?.name ?: "Dr. Bhaskar Hazarika", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(6.dp), color = Sky50) {
                            Text("Provider", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Sky700, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                    Text(state.currentUser?.specialty ?: "Geriatric Neurologist · GNRC Guwahati", fontSize = 11.sp, color = TextMuted)
                    Text("Connected to Firestore: ai-studio-smritinercogniti-...", fontSize = 9.sp, color = Emerald700, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Active Patient Selector
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Assigned Monitored Patient", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Sky50)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("👵", fontSize = 24.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Pratima Devi (Aita)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                        Text("Age 74 · Assam (NER) · Mild Cognitive Impairment", fontSize = 10.sp, color = TextMuted)
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = Emerald100) {
                        Text("Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald800, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
        }

        // Subtabs: Progress Logs vs Direct Chat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PatientSubTab("Clinical Logs (${state.progressLogs.size})", state.doctorActiveTab == "logs") {
                viewModel.selectDoctorTab("logs")
            }
            PatientSubTab("Direct Patient Chat", state.doctorActiveTab == "chat") {
                viewModel.selectDoctorTab("chat")
            }
        }

        if (state.doctorActiveTab == "logs") {
            // New Observation Input
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Log Clinical Observation (Firestore)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newLogText,
                        onValueChange = { newLogText = it },
                        placeholder = { Text("Log observation for caregiver & patient (e.g. Cognitive recall sharp)...", fontSize = 12.sp) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = {
                                if (newLogText.isNotBlank()) {
                                    viewModel.addProgressLog(newLogText.trim(), 95.0)
                                    newLogText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Sky700),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save to Firestore", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Existing Logs Feed
            Text("Patient Longitudinal Progress Logs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.progressLogs.forEach { log ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Observation", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Sky700)
                                val sdf = SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault())
                                Text(sdf.format(Date(log.timestamp)), fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(log.logText, fontSize = 12.sp, color = TextDark, lineHeight = 16.sp)
                            if (log.score != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Score: ${log.score.toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald700)
                            }
                        }
                    }
                }
            }
        } else {
            // DIRECT CHAT TAB
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth().height(420.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    Text("Chat with Pratima Devi (Aita)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Sky700)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(state.doctorChatMessages) { msg ->
                            val isMe = msg.senderId == state.currentUser?.uid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isMe) Sky700 else Color(0xFFF0F9FF),
                                    modifier = Modifier.widthIn(max = 260.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        color = if (isMe) Color.White else TextDark,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Write to Aita...", fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    viewModel.sendDoctorChatMessage(chatInput.trim())
                                    chatInput = ""
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Sky700)
                        ) {
                            Text("Send", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
