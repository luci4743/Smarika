package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.data.model.AppUser
import com.example.data.model.ChatMessage
import com.example.data.model.ProgressLog
import com.example.data.model.UserRole
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreService(context: Context) {
    companion object {
        const val PROJECT_ID = "exemplary-condition-zlcf1"
        const val DATABASE_ID = "ai-studio-smritinercogniti-735c86bf-48d8-4dd8-85f9-7dbcf4b9614a"
        const val API_KEY = "AIzaSyBwJYoYgksBPwTxEjNVA8m9ApFUrnmbEv0"
        const val APP_ID = "1:676129307547:web:a7d8c700a1493c7a0c7f3e"
        const val STORAGE_BUCKET = "exemplary-condition-zlcf1.firebasestorage.app"
        private const val TAG = "FirestoreService"
    }

    private var firestore: FirebaseFirestore? = null

    init {
        try {
            val options = FirebaseOptions.Builder()
                .setApplicationId(APP_ID)
                .setApiKey(API_KEY)
                .setProjectId(PROJECT_ID)
                .setStorageBucket(STORAGE_BUCKET)
                .build()

            val app = try {
                FirebaseApp.getInstance("SmritiNERApp")
            } catch (_: Exception) {
                FirebaseApp.initializeApp(context.applicationContext, options, "SmritiNERApp")
            }
            firestore = FirebaseFirestore.getInstance(app, DATABASE_ID)
            Log.d(TAG, "Connected to Firestore database: $DATABASE_ID on project: $PROJECT_ID")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Firebase: ${e.message}")
        }
    }

    suspend fun saveUser(user: AppUser): Boolean = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext false
        try {
            val userMap = mutableMapOf<String, Any>(
                "uid" to user.uid,
                "email" to user.email,
                "name" to user.name,
                "role" to user.role.key
            )
            user.doctorId?.let { userMap["doctorId"] = it }
            user.patientId?.let { userMap["patientId"] = it }
            user.relationship?.let { userMap["relationship"] = it }
            user.specialty?.let { userMap["specialty"] = it }

            fs.collection("users").document(user.uid)
                .set(userMap, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.w(TAG, "saveUser failed (offline or auth): ${e.message}")
            false
        }
    }

    suspend fun getUser(uid: String): AppUser? = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext null
        try {
            val snap = fs.collection("users").document(uid).get().await()
            if (snap.exists()) {
                val data = snap.data ?: return@withContext null
                AppUser(
                    uid = data["uid"] as? String ?: uid,
                    email = data["email"] as? String ?: "",
                    name = data["name"] as? String ?: "",
                    role = UserRole.fromString(data["role"] as? String ?: "patient"),
                    doctorId = data["doctorId"] as? String,
                    patientId = data["patientId"] as? String,
                    relationship = data["relationship"] as? String,
                    specialty = data["specialty"] as? String
                )
            } else null
        } catch (e: Exception) {
            Log.w(TAG, "getUser failed: ${e.message}")
            null
        }
    }

    suspend fun getProgressLogs(patientId: String, doctorId: String): List<ProgressLog> = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext emptyList()
        try {
            val snap = fs.collection("progressLogs")
                .whereEqualTo("patientId", patientId)
                .get()
                .await()

            snap.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                ProgressLog(
                    id = doc.id,
                    patientId = data["patientId"] as? String ?: "",
                    doctorId = data["doctorId"] as? String ?: "",
                    logText = data["logText"] as? String ?: "",
                    score = (data["score"] as? Number)?.toDouble(),
                    authorId = data["authorId"] as? String ?: "",
                    timestamp = (data["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
                )
            }.sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            Log.w(TAG, "getProgressLogs failed: ${e.message}")
            emptyList()
        }
    }

    suspend fun addProgressLog(log: ProgressLog): Boolean = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext false
        try {
            val logMap = mutableMapOf<String, Any>(
                "patientId" to log.patientId,
                "doctorId" to log.doctorId,
                "logText" to log.logText,
                "authorId" to log.authorId,
                "timestamp" to log.timestamp
            )
            log.score?.let { logMap["score"] = it }

            fs.collection("progressLogs").add(logMap).await()
            true
        } catch (e: Exception) {
            Log.w(TAG, "addProgressLog failed: ${e.message}")
            false
        }
    }

    suspend fun getChatMessages(chatId: String): List<ChatMessage> = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext emptyList()
        try {
            val snap = fs.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .get()
                .await()

            snap.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                ChatMessage(
                    id = doc.id,
                    senderId = data["senderId"] as? String ?: "",
                    text = data["text"] as? String ?: "",
                    timestamp = (data["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getChatMessages failed: ${e.message}")
            emptyList()
        }
    }

    suspend fun sendChatMessage(chatId: String, message: ChatMessage): Boolean = withContext(Dispatchers.IO) {
        val fs = firestore ?: return@withContext false
        try {
            val msgMap = mapOf(
                "senderId" to message.senderId,
                "text" to message.text,
                "timestamp" to message.timestamp
            )
            fs.collection("chats").document(chatId)
                .collection("messages").add(msgMap).await()

            fs.collection("chats").document(chatId)
                .set(mapOf("updatedAt" to message.timestamp, "lastMessage" to message.text), SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.w(TAG, "sendChatMessage failed: ${e.message}")
            false
        }
    }
}
