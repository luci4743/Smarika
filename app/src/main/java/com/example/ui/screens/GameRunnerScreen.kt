package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.Translations
import com.example.data.model.CognitiveDomain
import com.example.data.model.CulturalData
import com.example.data.model.CulturalItem
import com.example.state.AppViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MemoryCard(
    val id: String,
    val matchId: String,
    val symbol: String,
    val label: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun GameRunnerScreen(
    domain: CognitiveDomain,
    viewModel: AppViewModel,
    onExit: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var stage by remember { mutableStateOf("INSTRUCTIONS") } // "INSTRUCTIONS", "PLAYING", "COMPLETE"
    var currentRound by remember { mutableStateOf(1) }
    val totalRounds = 3
    var mistakes by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(100) }
    var encouragingMsg by remember { mutableStateOf("") }
    val startTime = remember { System.currentTimeMillis() }

    // Memory Game State
    var memoryCards by remember { mutableStateOf(emptyList<MemoryCard>()) }
    var selectedCards by remember { mutableStateOf(listOf<Int>()) }

    // Sequence Recall State
    var targetSequence by remember { mutableStateOf(emptyList<CulturalItem>()) }
    var userSequence by remember { mutableStateOf(emptyList<CulturalItem>()) }
    var isShowingSequence by remember { mutableStateOf(true) }

    // Object Recognition State
    var targetObject by remember { mutableStateOf<CulturalItem?>(null) }
    var objectChoices by remember { mutableStateOf(emptyList<CulturalItem>()) }

    // Pattern Matching State
    var patternItems by remember { mutableStateOf(emptyList<CulturalItem>()) }
    var patternOptions by remember { mutableStateOf(emptyList<CulturalItem>()) }
    var correctPatternChoice by remember { mutableStateOf<CulturalItem?>(null) }

    // Attention Grid State
    var attentionGrid by remember { mutableStateOf(emptyList<Pair<String, Boolean>>()) }
    var tappedAttention by remember { mutableStateOf(setOf<Int>()) }

    // Story Question State
    var storyQuestionIndex by remember { mutableStateOf(0) }

    fun initRound(roundNum: Int) {
        encouragingMsg = ""
        when (domain) {
            CognitiveDomain.MEMORY -> {
                val pool = CulturalData.items.shuffled().take(3)
                val cards = mutableListOf<MemoryCard>()
                pool.forEachIndexed { i, item ->
                    cards.add(MemoryCard("c_${i}_a", item.id, item.symbol, item.label))
                    cards.add(MemoryCard("c_${i}_b", item.id, item.symbol, item.label))
                }
                memoryCards = cards.shuffled()
                selectedCards = emptyList()
            }
            CognitiveDomain.SEQUENCE_RECALL -> {
                val seq = CulturalData.items.shuffled().take(3)
                targetSequence = seq
                userSequence = emptyList()
                isShowingSequence = true
                scope.launch {
                    delay(3000)
                    isShowingSequence = false
                }
            }
            CognitiveDomain.OBJECT_RECOGNITION, CognitiveDomain.CULTURAL_MEMORY -> {
                val target = CulturalData.items[(roundNum - 1) % CulturalData.items.size]
                val others = CulturalData.items.filter { it.id != target.id }.shuffled().take(2)
                targetObject = target
                objectChoices = (others + target).shuffled()
            }
            CognitiveDomain.PATTERN_MATCHING -> {
                val p1 = CulturalData.items[0]
                val p2 = CulturalData.items[1]
                patternItems = listOf(p1, p2, p1, p2)
                correctPatternChoice = p1
                patternOptions = listOf(p1, p2, CulturalData.items[2]).shuffled()
            }
            CognitiveDomain.ATTENTION -> {
                tappedAttention = emptySet()
                val targetEmoji = "🍵"
                val distractors = listOf("👒", "🧣", "🥁", "🌸")
                val grid = mutableListOf<Pair<String, Boolean>>()
                for (i in 0 until 9) {
                    val isTarget = (i % 3 == 0) || (i == 4)
                    grid.add(Pair(if (isTarget) targetEmoji else distractors.random(), isTarget))
                }
                attentionGrid = grid
            }
            CognitiveDomain.STORY_RECALL -> {
                storyQuestionIndex = (roundNum - 1) % CulturalData.bihuStory.questions.size
            }
            else -> {}
        }
    }

    fun handleRoundSuccess() {
        viewModel.soundHelper.playChime("match")
        viewModel.soundHelper.vibrate("success")
        encouragingMsg = "Wonderful! Well done!"

        if (currentRound < totalRounds) {
            scope.launch {
                delay(1200)
                currentRound++
                initRound(currentRound)
            }
        } else {
            stage = "COMPLETE"
            val totalSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt().coerceAtLeast(20)
            val accuracy = (score - mistakes * 5).coerceIn(65, 100)
            viewModel.recordGameSession(
                domain = domain,
                score = accuracy,
                accuracy = accuracy,
                mistakes = mistakes,
                durationSeconds = totalSeconds,
                completed = true
            )
        }
    }

    fun handleMistake() {
        mistakes++
        viewModel.soundHelper.playChime("gentle")
        viewModel.soundHelper.vibrate("medium")
        encouragingMsg = "Let's try together!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.isHighContrast) HighContrastBg else CanvasCream)
            .padding(16.dp)
    ) {
        // Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onExit() },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (state.isHighContrast) HighContrastSurface else Color.White)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Exit", tint = if (state.isHighContrast) HighContrastYellow else TextDark)
            }

            Text(
                text = domain.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = if (state.isHighContrast) HighContrastYellow else TextDark
            )

            IconButton(
                onClick = { viewModel.speak("${domain.title}. ${domain.description}") },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Emerald50)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Audio Guidance", tint = Emerald700)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (stage == "INSTRUCTIONS") {
            // Instructions Stage
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Emerald100),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = domain.emoji, fontSize = 42.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = domain.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isHighContrast) HighContrastYellow else TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = domain.description,
                    fontSize = 14.sp,
                    color = if (state.isHighContrast) Color.LightGray else TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.speak(domain.description)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Emerald700)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hear Guidance", color = TextDark, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        stage = "PLAYING"
                        currentRound = 1
                        initRound(1)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(52.dp)
                ) {
                    Text("START ACTIVITY →", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        } else if (stage == "PLAYING") {
            // Playing Stage
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Round & Encouragement Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Round $currentRound of $totalRounds",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = encouragingMsg.ifBlank { "Take your time" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald700
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (domain) {
                    CognitiveDomain.MEMORY -> {
                        // Memory Match Card Grid
                        Text(
                            text = "Tap cards to discover matching cultural pairs:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        ) {
                            itemsIndexed(memoryCards) { idx, card ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (card.isFlipped || card.isMatched) {
                                        if (state.isHighContrast) HighContrastYellow else Emerald50
                                    } else {
                                        if (state.isHighContrast) HighContrastSurface else Color.White
                                    },
                                    border = BorderStroke(
                                        2.dp,
                                        if (card.isFlipped || card.isMatched) Emerald600 else BorderSubtle
                                    ),
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clickable {
                                            if (card.isFlipped || card.isMatched || selectedCards.size >= 2) return@clickable
                                            viewModel.soundHelper.playChime("flip")
                                            val updated = memoryCards.mapIndexed { i, c ->
                                                if (i == idx) c.copy(isFlipped = true) else c
                                            }
                                            memoryCards = updated
                                            val newSelected = selectedCards + idx
                                            selectedCards = newSelected

                                            if (newSelected.size == 2) {
                                                val c1 = updated[newSelected[0]]
                                                val c2 = updated[newSelected[1]]
                                                if (c1.matchId == c2.matchId) {
                                                    // Match!
                                                    val matchedList = updated.mapIndexed { i, c ->
                                                        if (i == newSelected[0] || i == newSelected[1]) c.copy(isMatched = true) else c
                                                    }
                                                    memoryCards = matchedList
                                                    selectedCards = emptyList()
                                                    if (matchedList.all { it.isMatched }) {
                                                        handleRoundSuccess()
                                                    } else {
                                                        viewModel.soundHelper.playChime("match")
                                                    }
                                                } else {
                                                    handleMistake()
                                                    scope.launch {
                                                        delay(1000)
                                                        memoryCards = memoryCards.mapIndexed { i, c ->
                                                            if (i == newSelected[0] || i == newSelected[1]) c.copy(isFlipped = false) else c
                                                        }
                                                        selectedCards = emptyList()
                                                    }
                                                }
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (card.isFlipped || card.isMatched) {
                                            Text(text = card.symbol, fontSize = 34.sp)
                                        } else {
                                            Text(text = "✨", fontSize = 24.sp, color = BorderSubtle)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CognitiveDomain.SEQUENCE_RECALL -> {
                        // Sequence Recall
                        if (isShowingSequence) {
                            Text(text = "Remember this sequence:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Emerald50)
                                    .padding(20.dp)
                            ) {
                                targetSequence.forEach { item ->
                                    Text(text = item.symbol, fontSize = 40.sp)
                                }
                            }
                        } else {
                            Text(text = "Tap items in the same sequence:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Selected so far
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF5F3EF))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (userSequence.isEmpty()) {
                                    Text(text = "Tap cards below in order...", fontSize = 12.sp, color = TextMuted)
                                } else {
                                    userSequence.forEach { item ->
                                        Text(text = item.symbol, fontSize = 30.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Choices
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                targetSequence.shuffled().forEach { item ->
                                    Button(
                                        onClick = {
                                            val next = userSequence + item
                                            userSequence = next
                                            val currentIdx = next.size - 1
                                            if (targetSequence[currentIdx].id != item.id) {
                                                handleMistake()
                                                userSequence = emptyList()
                                            } else if (next.size == targetSequence.size) {
                                                handleRoundSuccess()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        border = BorderStroke(1.5.dp, Emerald700.copy(alpha = 0.3f)),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.fillMaxWidth().height(56.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(text = item.symbol, fontSize = 28.sp)
                                            Text(text = item.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CognitiveDomain.OBJECT_RECOGNITION, CognitiveDomain.CULTURAL_MEMORY -> {
                        // Object Recognition
                        val target = targetObject
                        if (target != null) {
                            Text(
                                text = "Which one is ${target.label}?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                objectChoices.forEach { opt ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.5.dp, BorderSubtle),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (opt.id == target.id) {
                                                    handleRoundSuccess()
                                                } else {
                                                    handleMistake()
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                                        ) {
                                            Text(text = opt.symbol, fontSize = 36.sp)
                                            Column {
                                                Text(text = opt.label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                                Text(text = opt.nerContext, fontSize = 11.sp, color = TextMuted)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CognitiveDomain.PATTERN_MATCHING -> {
                        // Pattern Matching
                        Text(text = "What item fits next in the motif?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Emerald50)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            patternItems.forEach {
                                Text(text = it.symbol, fontSize = 32.sp)
                            }
                            Text(text = "➔ ❓", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Emerald700)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            patternOptions.forEach { opt ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.5.dp, BorderSubtle),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(90.dp)
                                        .clickable {
                                            if (opt.id == correctPatternChoice?.id) {
                                                handleRoundSuccess()
                                            } else {
                                                handleMistake()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = opt.symbol, fontSize = 36.sp)
                                    }
                                }
                            }
                        }
                    }

                    CognitiveDomain.ATTENTION -> {
                        // Attention Game: Tap all tea cups
                        Text(text = "Tap every cup of Assam tea 🍵", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            itemsIndexed(attentionGrid) { idx, item ->
                                val isTapped = tappedAttention.contains(idx)
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isTapped) Color(0xFFE2E8F0) else Color.White,
                                    border = BorderStroke(1.5.dp, if (isTapped) Color.Transparent else BorderSubtle),
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clickable {
                                            if (isTapped) return@clickable
                                            tappedAttention = tappedAttention + idx
                                            if (item.second) {
                                                viewModel.soundHelper.playChime("tap")
                                                val allTargets = attentionGrid.mapIndexedNotNull { i, pair -> if (pair.second) i else null }
                                                if (allTargets.all { tappedAttention.contains(it) }) {
                                                    handleRoundSuccess()
                                                }
                                            } else {
                                                handleMistake()
                                            }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = item.first,
                                            fontSize = 32.sp,
                                            color = if (isTapped) Color.Gray else Color.Unspecified
                                        )
                                    }
                                }
                            }
                        }
                    }

                    CognitiveDomain.STORY_RECALL -> {
                        // Story Recall
                        val story = CulturalData.bihuStory
                        val q = story.questions[storyQuestionIndex]

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = story.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Emerald800)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = story.text, fontSize = 12.sp, color = TextDark, lineHeight = 17.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = q.question, fontSize = 15.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            q.options.forEachIndexed { optIdx, optText ->
                                Button(
                                    onClick = {
                                        if (optIdx == q.correctIndex) {
                                            handleRoundSuccess()
                                        } else {
                                            handleMistake()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().height(50.dp)
                                ) {
                                    Text(text = optText, color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }

                    else -> {
                        // Generic gentle option choices
                        Text(text = "What feels right for you right now?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            listOf("Brahmaputra Valley, Assam", "Khasi Hills, Meghalaya", "Imphal Valley, Manipur").forEach { choice ->
                                Button(
                                    onClick = { handleRoundSuccess() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().height(50.dp)
                                ) {
                                    Text(text = choice, color = TextDark, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Stage COMPLETE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Amber200),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⭐", fontSize = 48.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Wonderful! You did great!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Emerald800
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Activity safely recorded in your daily care streak.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onExit() },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald700),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(52.dp)
                ) {
                    Text("Return to Daily Routine", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
