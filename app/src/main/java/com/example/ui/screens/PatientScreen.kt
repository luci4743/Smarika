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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.Translations
import com.example.data.model.CognitiveDomain
import com.example.data.model.ReminderStatus
import com.example.state.AppViewModel
import com.example.ui.theme.*

@Composable
fun PatientScreen(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()

    // If an active game is currently selected, display the GameRunnerScreen
    if (state.activeGame != null) {
        GameRunnerScreen(
            domain = state.activeGame!!,
            viewModel = viewModel,
            onExit = { viewModel.exitGame() }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.isHighContrast) HighContrastBg else CanvasCream)
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Greeting Banner with Sunrise Gradient
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = if (state.isHighContrast) HighContrastSurface else Emerald900,
            border = if (state.isHighContrast) BorderStroke(2.dp, HighContrastYellow) else null,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (state.isHighContrast) Brush.linearGradient(listOf(HighContrastSurface, HighContrastBg))
                        else Brush.linearGradient(listOf(Emerald800, Teal800, Emerald950))
                    )
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Top Row: Avatar, Region Badge & Listen Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Emerald700),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👵", fontSize = 24.sp)
                            }
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Amber400.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "🌿 Guwahati, Assam",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber200,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "Today's Mindful Day",
                                    fontSize = 11.sp,
                                    color = Emerald100
                                )
                            }
                        }

                        // Listen Greeting Button
                        Button(
                            onClick = {
                                viewModel.soundHelper.playChime("gentle")
                                val greeting = Translations.get("greeting_morning", state.currentLanguage)
                                viewModel.speak("$greeting, Aita Pratima Devi! Welcome to a peaceful day of memory care and joyful activities.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = TextDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Hear Greeting", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    // Greeting Title
                    val greeting = Translations.get("greeting_morning", state.currentLanguage)
                    Text(
                        text = "$greeting, ${state.currentUser?.name ?: "Aita"}!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = if (state.isHighContrast) HighContrastYellow else Color.White
                    )

                    Text(
                        text = "Gentle sunrise, fresh breeze & mindful moments for your day.",
                        fontSize = 12.sp,
                        color = Emerald100
                    )

                    // Streak Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "🔥", fontSize = 14.sp)
                            Text(
                                text = "${state.streakDays}-Day Mindful Streak!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber200
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Amber400),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", fontSize = 9.sp, fontWeight = FontWeight.Black, color = TextDark)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sub-Navigation Tabs Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (state.isHighContrast) HighContrastSurface else Color.White)
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PatientSubTab("Routine", state.patientActiveTab == "home") { viewModel.selectPatientTab("home") }
            PatientSubTab("Games", state.patientActiveTab == "games") { viewModel.selectPatientTab("games") }
            PatientSubTab("Smriti AI", state.patientActiveTab == "ai") { viewModel.selectPatientTab("ai") }
            PatientSubTab("Doctor", state.patientActiveTab == "doctor") { viewModel.selectPatientTab("doctor") }
            PatientSubTab("Streak", state.patientActiveTab == "progress") { viewModel.selectPatientTab("progress") }
        }

        // Viewport Switch based on patientActiveTab
        when (state.patientActiveTab) {
            "home" -> {
                // DAILY ROUTINE VIEW
                // 1. Recommended Cognitive Game Card
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
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Emerald100),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🧠", fontSize = 24.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Memory Match", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(shape = RoundedCornerShape(6.dp), color = Emerald50) {
                                        Text("⭐ Recommended", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Emerald700, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                Text(text = "Match familiar Assam & NER heritage items · 3-5 mins", fontSize = 11.sp, color = TextMuted)
                            }
                        }

                        // Preview chips
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(shape = RoundedCornerShape(8.dp), color = CanvasCream, border = BorderStroke(1.dp, BorderSubtle)) {
                                Text("🥁 Bihu Dhol", fontSize = 10.sp, color = TextDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = CanvasCream, border = BorderStroke(1.dp, BorderSubtle)) {
                                Text("👒 Japi", fontSize = 10.sp, color = TextDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = CanvasCream, border = BorderStroke(1.dp, BorderSubtle)) {
                                Text("🍵 Assam Chai", fontSize = 10.sp, color = TextDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }

                        Button(
                            onClick = { viewModel.startPlayingGame(CognitiveDomain.MEMORY) },
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Amber200)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PLAY Memory Journey →", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                // 2. Fresh Water Hydration Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Sky100),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💧", fontSize = 24.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Drink Fresh Water", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                                Text("${state.waterGlassesCount}/6 Glasses", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Sky700)
                            }
                            Text("1 glass of lukewarm water · 11:30 AM", fontSize = 11.sp, color = TextMuted)
                        }

                        Button(
                            onClick = { viewModel.addWaterGlass() },
                            colors = ButtonDefaults.buttonColors(containerColor = Sky600),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("+ Drink 💧", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                // 3. Daily Reminders List
                Text("Today's Gentle Schedule", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.reminders.forEach { reminder ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, if (reminder.status == ReminderStatus.ACKNOWLEDGED) Emerald600.copy(alpha = 0.4f) else BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = if (reminder.status == ReminderStatus.ACKNOWLEDGED) "✅" else "⏰",
                                    fontSize = 20.sp
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reminder.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (reminder.status == ReminderStatus.ACKNOWLEDGED) Emerald800 else TextDark
                                    )
                                    Text(text = "${reminder.time} · ${reminder.doseInfo ?: "Scheduled"}", fontSize = 10.sp, color = TextMuted)
                                }

                                if (reminder.status != ReminderStatus.ACKNOWLEDGED) {
                                    Button(
                                        onClick = { viewModel.acknowledgeReminder(reminder.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Done ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Text("DONE ✓", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Emerald700)
                                }
                            }
                        }
                    }
                }
            }

            "games" -> {
                // COGNITIVE GAMES CATALOG
                Text("10 Culturally Resonant Cognitive Games", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CognitiveDomain.entries.forEach { dom ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.startPlayingGame(dom) }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Emerald50),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dom.emoji, fontSize = 24.sp)
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(dom.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                                    Text(dom.description, fontSize = 11.sp, color = TextMuted)
                                }

                                Button(
                                    onClick = { viewModel.startPlayingGame(dom) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("PLAY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            "ai" -> {
                // SMRITI AI VOICE COMPANION
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth().height(420.dp)
                ) {
                    SmritiAiView(viewModel = viewModel)
                }
            }

            "doctor" -> {
                // DOCTOR CONSULT DIRECT CHAT
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth().height(420.dp)
                ) {
                    DoctorConsultView(viewModel = viewModel)
                }
            }

            "progress" -> {
                // VITALITY & STREAK BADGES
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Your Loving Memory Milestones", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        BadgeCard("Morning Ritual", "🍵", "Warm tea & morning hydrations", Modifier.weight(1f)) {
                            viewModel.speak("Morning Ritual Badge. You greeted the morning with hydration and warm Assam tea.")
                        }
                        BadgeCard("Cultural Heart", "🧣", "Gamosa & Bihu folk symbols", Modifier.weight(1f)) {
                            viewModel.speak("Cultural Heart Badge. You matched traditional Gamosa and festive symbols with joy.")
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        BadgeCard("River Calm", "🏞️", "Tranquil breath of Brahmaputra", Modifier.weight(1f)) {
                            viewModel.speak("River Calm Badge. Peaceful breath like the gentle waters of Majuli island.")
                        }
                        BadgeCard("Kaziranga Star", "🦏", "Wild wonders & sharp recognition", Modifier.weight(1f)) {
                            viewModel.speak("Kaziranga Star Badge. Recognized iconic regional symbols with pride.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PatientSubTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Emerald700 else Color.Transparent,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextDark,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun BadgeCard(
    title: String,
    emoji: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
            Text(desc, fontSize = 9.sp, color = TextMuted, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SmritiAiView(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🤖", fontSize = 20.sp)
            Column {
                Text("Smriti AI Empathy Companion", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Emerald800)
                Text("Always here to talk, share memories & bring calm", fontSize = 10.sp, color = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message stream
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.aiCompanionMessages) { msg ->
                val isMe = msg.senderId == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isMe) Emerald700 else Color(0xFFF5F3EF),
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
            if (state.isAiThinking) {
                item {
                    Text("Smriti AI is thinking peacefully...", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(6.dp))
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Tell me anything...", fontSize = 12.sp) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendAiChatMessage(input.trim())
                        input = ""
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
                Text("Send", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DoctorConsultView(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🩺", fontSize = 20.sp)
            Column {
                Text("Direct Message with Dr. Bhaskar Hazarika", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Sky700)
                Text("Synced live with Firestore clinical channel", fontSize = 10.sp, color = TextMuted)
            }
        }

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
                        shape = RoundedCornerShape(14.dp),
                        color = if (isMe) Sky700 else Color(0xFFF0F9FF),
                        modifier = Modifier.widthIn(max = 260.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.text,
                                color = if (isMe) Color.White else TextDark,
                                fontSize = 12.sp
                            )
                        }
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
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Message Dr. Bhaskar...", fontSize = 12.sp) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendDoctorChatMessage(input.trim())
                        input = ""
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Sky700)
            ) {
                Text("Send", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
