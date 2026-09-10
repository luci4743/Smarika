package com.example.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundAndVoiceHelper
import com.example.data.i18n.Translations
import com.example.data.model.AppUser
import com.example.data.model.CaregiverNote
import com.example.data.model.ChatMessage
import com.example.data.model.CognitiveDomain
import com.example.data.model.FamilyMember
import com.example.data.model.GameSession
import com.example.data.model.ProgressLog
import com.example.data.model.Reminder
import com.example.data.model.ReminderStatus
import com.example.data.model.ReminderType
import com.example.data.model.UserRole
import com.example.data.remote.FirestoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class AppUiState(
    val currentUser: AppUser? = null,
    val currentLanguage: String = "en",
    val isHighContrast: Boolean = false,
    val textSizeMultiplier: Float = 1.0f,
    val soundEffects: Boolean = true,
    val voiceInstructions: Boolean = true,
    val isOffline: Boolean = false,
    val waterGlassesCount: Int = 3,
    val streakDays: Int = 4,
    val reminders: List<Reminder> = emptyList(),
    val sessions: List<GameSession> = emptyList(),
    val familyMembers: List<FamilyMember> = emptyList(),
    val patients: List<AppUser> = emptyList(),
    val selectedPatient: AppUser? = null,
    val progressLogs: List<ProgressLog> = emptyList(),
    val doctorChatMessages: List<ChatMessage> = emptyList(),
    val caregiverNotes: List<CaregiverNote> = emptyList(),
    val aiCompanionMessages: List<ChatMessage> = emptyList(),
    val isAiThinking: Boolean = false,
    val activeGame: CognitiveDomain? = null,
    val patientActiveTab: String = "home", // "home", "games", "ai", "doctor", "reminders", "progress"
    val caregiverActiveTab: String = "overview", // "overview", "analytics", "reminders", "family", "notes"
    val doctorActiveTab: String = "logs", // "logs", "chat"
    val toastMessage: String? = null
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val firestoreService = FirestoreService(application)
    val soundHelper = SoundAndVoiceHelper(application)

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        // Automatically start with Patient demo profile so emulator shows full experience right away
        loginDemoPatient()
    }

    private fun createInitialState(): AppUiState {
        val defaultReminders = listOf(
            Reminder(
                id = "rem-001",
                patientId = "patient-ner-001",
                type = ReminderType.MEDICINE,
                title = "Morning Blood Pressure Tablet (Amlodipine)",
                time = "09:00 AM",
                status = ReminderStatus.ACKNOWLEDGED,
                doseInfo = "1 tablet after warm tea",
                icon = "pill"
            ),
            Reminder(
                id = "rem-002",
                patientId = "patient-ner-001",
                type = ReminderType.HYDRATION,
                title = "Fresh Water (পানী খোৱা)",
                time = "11:30 AM",
                status = ReminderStatus.PENDING,
                doseInfo = "1 full glass",
                icon = "droplet"
            ),
            Reminder(
                id = "rem-003",
                patientId = "patient-ner-001",
                type = ReminderType.MEALS,
                title = "Nutritious Family Lunch",
                time = "01:00 PM",
                status = ReminderStatus.PENDING,
                doseInfo = "Steamed rice, lentils & seasonal garden greens",
                icon = "meals"
            ),
            Reminder(
                id = "rem-004",
                patientId = "patient-ner-001",
                type = ReminderType.EXERCISE,
                title = "Afternoon Veranda Walk",
                time = "04:30 PM",
                status = ReminderStatus.PENDING,
                doseInfo = "15 peaceful minutes around the courtyard",
                icon = "walk"
            )
        )

        val defaultNotes = listOf(
            CaregiverNote("n1", "Rahul (Son)", "Aita enjoyed the Bihu Dhol and Gamosa pictures this morning. She hummed a folk song.", "Today, 10:15 AM"),
            CaregiverNote("n2", "Dr. Bhaskar Hazarika", "Consistent adherence to 5-minute cognitive sessions noted. Recommend maintaining calm afternoon schedule.", "Yesterday, 04:30 PM")
        )

        val defaultLogs = listOf(
            ProgressLog("log1", "patient-ner-001", "doctor-001", "Patient completed Memory Match with 92% accuracy. Response latency within normal comfort threshold.", 92.0, "doctor-001", System.currentTimeMillis() - 86400000L),
            ProgressLog("log2", "patient-ner-001", "doctor-001", "Morning routine adherence observed at 100%. Orientation to season (Bohag) is sharp and cheerful.", 95.0, "doctor-001", System.currentTimeMillis() - 3600000L * 5)
        )

        val defaultSessions = listOf(
            GameSession("sess1", "evt1", "patient-ner-001", "memory", CognitiveDomain.MEMORY, 1, 92, 92, 185, 7, 1, 0, 2100, true),
            GameSession("sess2", "evt2", "patient-ner-001", "sequence_recall", CognitiveDomain.SEQUENCE_RECALL, 1, 85, 85, 160, 5, 1, 1, 2400, true),
            GameSession("sess3", "evt3", "patient-ner-001", "memory", CognitiveDomain.MEMORY, 1, 95, 95, 170, 6, 0, 0, 1900, true)
        )

        val defaultChat = listOf(
            ChatMessage("msg1", "doctor-001", "Dr. Bhaskar", "Good morning Aita! How are you feeling today?", System.currentTimeMillis() - 7200000L),
            ChatMessage("msg2", "patient-ner-001", "Aita Pratima", "Good morning Doctor Babu. I drank warm tea and solved the memory cards nicely.", System.currentTimeMillis() - 3600000L),
            ChatMessage("msg3", "doctor-001", "Dr. Bhaskar", "Wonderful to hear! Remember to drink plenty of water before your garden walk.", System.currentTimeMillis() - 1800000L)
        )

        val defaultFamily = listOf(
            FamilyMember("fam1", "Rahul", "Son"),
            FamilyMember("fam2", "Bobi", "Granddaughter"),
            FamilyMember("fam3", "Dr. Bhaskar", "Family Neurologist")
        )

        val defaultAi = listOf(
            ChatMessage("ai1", "model", "Smriti AI", "Hello Aita! I am here to help you remember things, share folk tales, or just chat. How are you feeling today?")
        )

        val demoPatient = AppUser(
            uid = "patient-ner-001",
            email = "aita.hemaprabha@smritiner.care",
            name = "Pratima Devi (Aita)",
            role = UserRole.PATIENT,
            doctorId = "doctor-001"
        )

        return AppUiState(
            currentUser = demoPatient,
            reminders = defaultReminders,
            caregiverNotes = defaultNotes,
            progressLogs = defaultLogs,
            sessions = defaultSessions,
            doctorChatMessages = defaultChat,
            familyMembers = defaultFamily,
            aiCompanionMessages = defaultAi,
            patients = listOf(demoPatient),
            selectedPatient = demoPatient
        )
    }

    fun loginDemoPatient() {
        val user = AppUser(
            uid = "patient-ner-001",
            email = "aita.hemaprabha@smritiner.care",
            name = "Pratima Devi (Aita)",
            role = UserRole.PATIENT,
            doctorId = "doctor-001"
        )
        _uiState.update { it.copy(currentUser = user) }
        viewModelScope.launch {
            firestoreService.saveUser(user)
        }
    }

    fun loginDemoDoctor() {
        val user = AppUser(
            uid = "doctor-001",
            email = "dr.bhaskar@gnrc-guwahati.in",
            name = "Dr. Bhaskar Hazarika",
            role = UserRole.DOCTOR,
            specialty = "Geriatric Neurologist (GNRC Guwahati)"
        )
        _uiState.update { it.copy(currentUser = user) }
        viewModelScope.launch {
            firestoreService.saveUser(user)
        }
    }

    fun loginDemoCaregiver() {
        val user = AppUser(
            uid = "caregiver-001",
            email = "rahul.borah@smritiner.care",
            name = "Rahul Borah (Son)",
            role = UserRole.CAREGIVER,
            patientId = "patient-ner-001",
            relationship = "Son / Primary Family Caregiver"
        )
        _uiState.update { it.copy(currentUser = user) }
        viewModelScope.launch {
            firestoreService.saveUser(user)
        }
    }

    fun switchRole(role: UserRole) {
        when (role) {
            UserRole.PATIENT -> loginDemoPatient()
            UserRole.DOCTOR -> loginDemoDoctor()
            UserRole.CAREGIVER -> loginDemoCaregiver()
        }
        soundHelper.playChime("tap")
        soundHelper.vibrate("light")
    }

    fun loginCustom(role: UserRole, name: String, email: String, doctorId: String?, patientId: String?, relationship: String?, specialty: String?) {
        val uid = "user_${UUID.randomUUID().toString().take(8)}"
        val user = AppUser(
            uid = uid,
            email = email.ifBlank { "user@smritiner.care" },
            name = name.ifBlank { "SmritiNER User" },
            role = role,
            doctorId = doctorId?.ifBlank { null },
            patientId = patientId?.ifBlank { null },
            relationship = relationship?.ifBlank { null },
            specialty = specialty?.ifBlank { null }
        )
        _uiState.update { it.copy(currentUser = user) }
        viewModelScope.launch {
            firestoreService.saveUser(user)
        }
    }

    fun logout() {
        _uiState.update { it.copy(currentUser = null) }
    }

    fun setLanguage(code: String) {
        _uiState.update { it.copy(currentLanguage = code) }
        soundHelper.playChime("tap")
    }

    fun toggleHighContrast() {
        _uiState.update { it.copy(isHighContrast = !it.isHighContrast) }
        soundHelper.playChime("tap")
    }

    fun setTextSize(size: String) {
        val multiplier = when (size) {
            "small" -> 0.85f
            "large" -> 1.15f
            "extra_large" -> 1.3f
            else -> 1.0f
        }
        _uiState.update { it.copy(textSizeMultiplier = multiplier) }
    }

    fun toggleSound() {
        _uiState.update { it.copy(soundEffects = !it.soundEffects) }
    }

    fun toggleVoiceInstructions() {
        _uiState.update { it.copy(voiceInstructions = !it.voiceInstructions) }
    }

    fun toggleOffline() {
        val next = !_uiState.value.isOffline
        _uiState.update { it.copy(isOffline = next) }
        soundHelper.playChime("tap")
        if (!next) {
            // Re-sync with Firestore
            syncWithFirestore()
        }
    }

    fun selectPatientTab(tab: String) {
        _uiState.update { it.copy(patientActiveTab = tab, activeGame = null) }
        soundHelper.playChime("tap")
    }

    fun selectCaregiverTab(tab: String) {
        _uiState.update { it.copy(caregiverActiveTab = tab) }
        soundHelper.playChime("tap")
    }

    fun selectDoctorTab(tab: String) {
        _uiState.update { it.copy(doctorActiveTab = tab) }
        soundHelper.playChime("tap")
    }

    fun startPlayingGame(domain: CognitiveDomain) {
        _uiState.update { it.copy(activeGame = domain) }
        soundHelper.playChime("tap")
        if (_uiState.value.voiceInstructions) {
            speak("${domain.title}. Tap cards to begin gently.")
        }
    }

    fun exitGame() {
        _uiState.update { it.copy(activeGame = null) }
        soundHelper.playChime("tap")
    }

    fun acknowledgeReminder(reminderId: String) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.map {
                    if (it.id == reminderId) it.copy(status = ReminderStatus.ACKNOWLEDGED) else it
                }
            )
        }
        soundHelper.playChime("complete")
        soundHelper.vibrate("success")
        showToast("Activity recorded with joy ✓")
    }

    fun snoozeReminder(reminderId: String) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.map {
                    if (it.id == reminderId) it.copy(status = ReminderStatus.SNOOZED, snoozeCount = it.snoozeCount + 1) else it
                }
            )
        }
        soundHelper.playChime("gentle")
        soundHelper.vibrate("medium")
        showToast("Snoozed for 10 minutes")
    }

    fun addWaterGlass() {
        _uiState.update { it.copy(waterGlassesCount = (it.waterGlassesCount + 1).coerceAtMost(6)) }
        soundHelper.playChime("water")
        soundHelper.vibrate("success")
        showToast("Fresh water hydration recorded 💧")
    }

    fun addReminder(title: String, time: String, type: ReminderType = ReminderType.CUSTOM) {
        val newRem = Reminder(
            id = "rem_${System.currentTimeMillis()}",
            patientId = _uiState.value.currentUser?.uid ?: "patient-ner-001",
            type = type,
            title = title,
            time = time,
            status = ReminderStatus.PENDING,
            icon = "bell"
        )
        _uiState.update { it.copy(reminders = listOf(newRem) + it.reminders) }
        soundHelper.playChime("tap")
        showToast("New reminder scheduled")
    }

    fun addProgressLog(logText: String, score: Double? = null) {
        val doctorId = _uiState.value.currentUser?.uid ?: "doctor-001"
        val patientId = _uiState.value.selectedPatient?.uid ?: "patient-ner-001"
        val log = ProgressLog(
            id = "log_${System.currentTimeMillis()}",
            patientId = patientId,
            doctorId = doctorId,
            logText = logText,
            score = score,
            authorId = doctorId
        )
        _uiState.update { it.copy(progressLogs = listOf(log) + it.progressLogs) }
        soundHelper.playChime("complete")
        showToast("Clinical progress log saved to Firestore")

        viewModelScope.launch {
            firestoreService.addProgressLog(log)
        }
    }

    fun sendDoctorChatMessage(text: String) {
        val user = _uiState.value.currentUser ?: return
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = user.uid,
            senderName = user.name,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        _uiState.update { it.copy(doctorChatMessages = it.doctorChatMessages + msg) }
        soundHelper.playChime("tap")

        val chatId = "chat_patient-ner-001_doctor-001"
        viewModelScope.launch {
            firestoreService.sendChatMessage(chatId, msg)
        }
    }

    fun sendAiChatMessage(userMessage: String) {
        val userMsg = ChatMessage(
            id = "ai_u_${System.currentTimeMillis()}",
            senderId = "user",
            text = userMessage,
            timestamp = System.currentTimeMillis()
        )
        _uiState.update {
            it.copy(
                aiCompanionMessages = it.aiCompanionMessages + userMsg,
                isAiThinking = true
            )
        }
        soundHelper.playChime("tap")

        viewModelScope.launch {
            // Empathy AI logic with warm comforting response
            kotlinx.coroutines.delay(1200)
            val replyText = generateEmpatheticAiReply(userMessage)
            val aiMsg = ChatMessage(
                id = "ai_m_${System.currentTimeMillis()}",
                senderId = "model",
                senderName = "Smriti AI",
                text = replyText,
                timestamp = System.currentTimeMillis()
            )
            _uiState.update {
                it.copy(
                    aiCompanionMessages = it.aiCompanionMessages + aiMsg,
                    isAiThinking = false
                )
            }
            soundHelper.playChime("gentle")
            if (_uiState.value.voiceInstructions) {
                speak(replyText)
            }
        }
    }

    private fun generateEmpatheticAiReply(query: String): String {
        val q = query.lowercase()
        return when {
            "tea" in q || "chai" in q ->
                "Assam tea is so soothing, Aita! Savoring a warm cup of orthodox golden tea warms the body and keeps the mind peaceful."
            "bihu" in q || "song" in q || "music" in q ->
                "The cheerful sound of Bihu Dhol and Pepa brings back wonderful memories under the banyan tree! You hummed that rhythm beautifully."
            "medicine" in q || "pill" in q ->
                "Taking medicines regularly keeps your heart and brain healthy. Your son Rahul and Dr. Bhaskar are caring for you every day."
            "water" in q || "thirsty" in q ->
                "Drinking fresh lukewarm water is great for your energy! Let us take a slow, comforting sip together."
            "family" in q || "son" in q || "daughter" in q ->
                "Your family loves you very dearly! Rahul is checking on your routine and smiles every time you complete your activities."
            else ->
                "You are doing wonderfully today! Remember to take gentle breaths, enjoy the soft sunlight, and cherish peaceful thoughts."
        }
    }

    fun recordGameSession(domain: CognitiveDomain, score: Int, accuracy: Int, mistakes: Int, durationSeconds: Int, completed: Boolean, abandoned: Boolean = false) {
        val session = GameSession(
            id = "sess_${System.currentTimeMillis()}",
            clientEventId = UUID.randomUUID().toString(),
            patientId = "patient-ner-001",
            gameId = domain.id,
            cognitiveDomain = domain,
            difficulty = 1,
            score = score,
            accuracy = accuracy,
            durationSeconds = durationSeconds,
            attempts = accuracy + mistakes,
            mistakes = mistakes,
            hintsUsed = 0,
            averageResponseTimeMs = 2100L,
            completed = completed,
            abandoned = abandoned
        )
        _uiState.update {
            it.copy(
                sessions = listOf(session) + it.sessions,
                activeGame = null,
                streakDays = it.streakDays + 1
            )
        }
        soundHelper.playChime("complete")
        soundHelper.vibrate("success")
        showToast("Activity score $accuracy% saved to your daily care record!")
    }

    fun addCaregiverNote(text: String) {
        val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
        val note = CaregiverNote(
            id = "note_${System.currentTimeMillis()}",
            author = _uiState.value.currentUser?.name ?: "Caregiver",
            text = text,
            date = sdf.format(Date())
        )
        _uiState.update { it.copy(caregiverNotes = listOf(note) + it.caregiverNotes) }
        soundHelper.playChime("complete")
        showToast("Observation noted for healthcare team")
    }

    fun addFamilyMember(name: String, relation: String) {
        val member = FamilyMember("fam_${System.currentTimeMillis()}", name, relation)
        _uiState.update { it.copy(familyMembers = it.familyMembers + member) }
        soundHelper.playChime("tap")
        showToast("Family member added for Face & Name recall game")
    }

    fun removeFamilyMember(id: String) {
        _uiState.update { it.copy(familyMembers = it.familyMembers.filter { it.id != id }) }
        soundHelper.playChime("tap")
    }

    fun speak(text: String) {
        soundHelper.speak(text, _uiState.value.currentLanguage)
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
        viewModelScope.launch {
            kotlinx.coroutines.delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    private fun syncWithFirestore() {
        viewModelScope.launch {
            val patientId = "patient-ner-001"
            val doctorId = "doctor-001"
            val remoteLogs = firestoreService.getProgressLogs(patientId, doctorId)
            if (remoteLogs.isNotEmpty()) {
                _uiState.update { it.copy(progressLogs = remoteLogs) }
            }
            val remoteChat = firestoreService.getChatMessages("chat_${patientId}_${doctorId}")
            if (remoteChat.isNotEmpty()) {
                _uiState.update { it.copy(doctorChatMessages = remoteChat) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundHelper.release()
    }
}
