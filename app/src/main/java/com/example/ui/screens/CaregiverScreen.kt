package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReminderStatus
import com.example.state.AppViewModel
import com.example.ui.theme.*

@Composable
fun CaregiverScreen(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()
    var noteText by remember { mutableStateOf("") }
    var newMemberName by remember { mutableStateOf("") }
    var newMemberRelation by remember { mutableStateOf("") }
    var newRemTitle by remember { mutableStateOf("") }
    var newRemTime by remember { mutableStateOf("02:00 PM") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.isHighContrast) HighContrastBg else CanvasCream)
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Rose100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("❤️", fontSize = 24.sp)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(state.currentUser?.name ?: "Rahul Borah", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(6.dp), color = Rose50) {
                                Text("Caregiver", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Rose700, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                            }
                        }
                        Text("Son / Primary Family Caregiver · Monitored Elder: Pratima Devi (Aita)", fontSize = 11.sp, color = TextMuted)
                    }
                }

                // Badges Row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Emerald50, border = BorderStroke(1.dp, Emerald100)) {
                        Text("✓ Elder Status: Stable", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = Amber50, border = BorderStroke(1.dp, Amber200)) {
                        Text("🔥 4-Day Cognitive Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Amber600, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
        }

        // Stats Row (Adherence, Agitation, Geofence)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            CaregiverStatBox("92%", "Adherence", Emerald700, Modifier.weight(1f))
            CaregiverStatBox("Normal", "Agitation Risk", Sky700, Modifier.weight(1f))
            CaregiverStatBox("Home", "Safe Geofence", Amber600, Modifier.weight(1f))
        }

        // Subtabs (Overview, Reminders, Family, Notes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PatientSubTab("Overview", state.caregiverActiveTab == "overview") { viewModel.selectCaregiverTab("overview") }
            PatientSubTab("Reminders", state.caregiverActiveTab == "reminders") { viewModel.selectCaregiverTab("reminders") }
            PatientSubTab("Family", state.caregiverActiveTab == "family") { viewModel.selectCaregiverTab("family") }
            PatientSubTab("Notes", state.caregiverActiveTab == "notes") { viewModel.selectCaregiverTab("notes") }
        }

        when (state.caregiverActiveTab) {
            "overview" -> {
                // Overview Tab: Cognitive Performance Summary & Alerts
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Weekly Cognitive Activity Observations", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                        Text(
                            text = "\"Memory activity performance has remained steady over the last 7 days. Enthusiastic recall of Bihu Dhol, Gamosa and tea garden folklore observed. Routine hydration consistently completed.\"",
                            fontSize = 12.sp,
                            color = TextDark,
                            lineHeight = 17.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF5F3EF))
                                .padding(10.dp)
                        )
                    }
                }

                // Recent Observations Feed
                Text("Recent Shared Notes with Healthcare Team", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.caregiverNotes.forEach { note ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(note.author, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Rose700)
                                    Text(note.date, fontSize = 10.sp, color = TextMuted)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.text, fontSize = 12.sp, color = TextDark)
                            }
                        }
                    }
                }
            }

            "reminders" -> {
                // Manage Reminders Tab
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Schedule New Routine Reminder", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)

                        OutlinedTextField(
                            value = newRemTitle,
                            onValueChange = { newRemTitle = it },
                            placeholder = { Text("e.g. Afternoon Herbal Tea", fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newRemTime,
                                onValueChange = { newRemTime = it },
                                placeholder = { Text("02:00 PM", fontSize = 12.sp) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (newRemTitle.isNotBlank()) {
                                        viewModel.addReminder(newRemTitle.trim(), newRemTime.trim())
                                        newRemTitle = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Rose700)
                            ) {
                                Text("+ Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text("Active Daily Reminders", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.reminders.forEach { rem ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(if (rem.status == ReminderStatus.ACKNOWLEDGED) "✅" else "⏰", fontSize = 18.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(rem.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                    Text("${rem.time} · Status: ${rem.status}", fontSize = 10.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }

            "family" -> {
                // Family Directory Tab
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Add Family Member (for Face & Name Recall Game)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newMemberName,
                                onValueChange = { newMemberName = it },
                                placeholder = { Text("Name (e.g. Rahul)", fontSize = 12.sp) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = newMemberRelation,
                                onValueChange = { newMemberRelation = it },
                                placeholder = { Text("Relation (e.g. Son)", fontSize = 12.sp) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Button(
                            onClick = {
                                if (newMemberName.isNotBlank() && newMemberRelation.isNotBlank()) {
                                    viewModel.addFamilyMember(newMemberName.trim(), newMemberRelation.trim())
                                    newMemberName = ""
                                    newMemberRelation = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose700),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Add to Family Directory", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text("Registered Family Members (${state.familyMembers.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.familyMembers.forEach { member ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Rose100), contentAlignment = Alignment.Center) {
                                        Text(member.name.take(1), fontWeight = FontWeight.Bold, color = Rose700, fontSize = 16.sp)
                                    }
                                    Column {
                                        Text(member.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                        Text(member.relation, fontSize = 11.sp, color = TextMuted)
                                    }
                                }

                                IconButton(onClick = { viewModel.removeFamilyMember(member.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            "notes" -> {
                // Notes Tab
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Record Observation for Doctor", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)

                        OutlinedTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            placeholder = { Text("e.g. Aita enjoyed afternoon tea and smiled at Bihu songs...", fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(80.dp),
                            maxLines = 3
                        )

                        Button(
                            onClick = {
                                if (noteText.isNotBlank()) {
                                    viewModel.addCaregiverNote(noteText.trim())
                                    noteText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose700),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Post Observation Note", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text("Communication Log", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.caregiverNotes.forEach { note ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(note.author, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Rose700)
                                    Text(note.date, fontSize = 10.sp, color = TextMuted)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.text, fontSize = 12.sp, color = TextDark)
                            }
                        }
                    }
                }
            }
        }

        // Emergency Call Button
        Button(
            onClick = {
                viewModel.soundHelper.playChime("gentle")
                viewModel.showToast("Contacting Dr. Bhaskar Hazarika (GNRC Guwahati)...")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Rose700),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contact Assigned Neurologist (GNRC)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
fun CaregiverStatBox(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
            Text(label, fontSize = 10.sp, color = TextMuted)
        }
    }
}
