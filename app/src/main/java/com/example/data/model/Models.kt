package com.example.data.model

enum class UserRole(val key: String) {
    PATIENT("patient"),
    DOCTOR("doctor"),
    CAREGIVER("caregiver");

    companion object {
        fun fromString(value: String): UserRole {
            return entries.firstOrNull { it.key.equals(value, ignoreCase = true) } ?: PATIENT
        }
    }
}

data class AppUser(
    val uid: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val doctorId: String? = null,
    val patientId: String? = null,
    val relationship: String? = null,
    val specialty: String? = null
)

data class FamilyMember(
    val id: String,
    val name: String,
    val relation: String
)

enum class ReminderType {
    MEDICINE,
    HYDRATION,
    MEALS,
    EXERCISE,
    CUSTOM
}

enum class ReminderStatus {
    PENDING,
    ACKNOWLEDGED,
    SNOOZED
}

data class Reminder(
    val id: String,
    val patientId: String,
    val type: ReminderType,
    val title: String,
    val time: String,
    val scheduleType: String = "DAILY",
    var status: ReminderStatus = ReminderStatus.PENDING,
    val doseInfo: String? = null,
    var snoozeCount: Int = 0,
    val icon: String = "bell"
)

enum class CognitiveDomain(val id: String, val title: String, val description: String, val emoji: String) {
    MEMORY("memory", "Memory Match", "Match familiar cards with culturally resonant regional objects", "🧠"),
    SEQUENCE_RECALL("sequence_recall", "Sequence Recall", "Observe and repeat chronological order of symbols", "📋"),
    OBJECT_RECOGNITION("object_recognition", "Object Recognition", "Identify everyday regional tools and textiles", "🔍"),
    PATTERN_MATCHING("pattern_matching", "Pattern Matching", "Discover the recurring motif and pick the next one", "📐"),
    DAILY_ROUTINE("daily_routine", "Daily Routine Order", "Arrange everyday activities in familiar order", "📅"),
    ATTENTION("attention", "Gentle Attention", "Tap target tea cups while looking past distractors", "🎯"),
    STORY_RECALL("story_recall", "Folk Story Recall", "Listen to short regional folk tales and answer questions", "📖"),
    ORIENTATION("orientation", "Time & Place Orientation", "Warm check-in on today, season, and homeland", "🧭"),
    NAME_MEMORY("name_memory", "Family & Friendly Faces", "Recognize loved ones and friendly community helpers", "👤"),
    CULTURAL_MEMORY("cultural_memory", "NER Cultural Heritage", "Festivals, traditional attire, and foods of North East India", "✨")
}

data class GameSession(
    val id: String,
    val clientEventId: String,
    val patientId: String,
    val gameId: String,
    val cognitiveDomain: CognitiveDomain,
    val difficulty: Int,
    val score: Int,
    val accuracy: Int,
    val durationSeconds: Int,
    val attempts: Int,
    val mistakes: Int,
    val hintsUsed: Int,
    val averageResponseTimeMs: Long,
    val completed: Boolean,
    val abandoned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class CaregiverAlert(
    val id: String,
    val patientId: String,
    val patientName: String,
    val type: String,
    val severity: String, // "info", "warning", "critical"
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    var resolved: Boolean = false
)

data class ProgressLog(
    val id: String,
    val patientId: String,
    val doctorId: String,
    val logText: String,
    val score: Double? = null,
    val authorId: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String? = null,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CaregiverNote(
    val id: String,
    val author: String,
    val text: String,
    val date: String
)
