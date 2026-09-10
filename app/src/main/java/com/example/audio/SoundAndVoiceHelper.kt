package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class SoundAndVoiceHelper(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        initTts()
    }

    private fun initTts() {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                tts?.setSpeechRate(0.85f)
                tts?.setPitch(1.0f)
                try {
                    tts?.language = Locale("en", "IN")
                } catch (_: Exception) {}
            }
        }
    }

    fun speak(text: String, languageCode: String = "en") {
        if (!isTtsReady || tts == null) return
        try {
            val locale = when (languageCode.lowercase()) {
                "as" -> Locale("as", "IN")
                "bn" -> Locale("bn", "IN")
                "mni" -> Locale("mni", "IN")
                else -> Locale("en", "IN")
            }
            tts?.language = locale
            tts?.setSpeechRate(0.85f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "smriti_tts_${System.currentTimeMillis()}")
        } catch (_: Exception) {
            try {
                tts?.language = Locale.ENGLISH
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "smriti_fallback")
            } catch (_: Exception) {}
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun playChime(type: String) {
        scope.launch {
            try {
                when (type) {
                    "tap" -> generateTone(320.0, 70, 0.2)
                    "flip" -> generateTone(520.0, 100, 0.25)
                    "water" -> {
                        generateTone(600.0, 80, 0.2)
                        generateTone(850.0, 100, 0.2)
                    }
                    "match" -> {
                        generateTone(523.25, 120, 0.3) // C5
                        generateTone(659.25, 120, 0.3) // E5
                        generateTone(783.99, 200, 0.35) // G5
                    }
                    "complete", "fanfare" -> {
                        generateTone(523.25, 90, 0.25)
                        generateTone(659.25, 90, 0.25)
                        generateTone(783.99, 90, 0.25)
                        generateTone(880.00, 100, 0.25)
                        generateTone(1046.50, 240, 0.3)
                    }
                    "gentle" -> generateTone(432.0, 350, 0.25)
                    else -> generateTone(440.0, 100, 0.2)
                }
            } catch (_: Exception) {}
        }
    }

    private fun generateTone(freqHz: Double, durationMs: Int, volume: Double) {
        val sampleRate = 44100
        val numSamples = (sampleRate * durationMs / 1000.0).toInt()
        if (numSamples <= 0) return

        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val time = i.toDouble() / sampleRate
            // Apply gentle fade out envelope to avoid click
            val envelope = 1.0 - (i.toDouble() / numSamples)
            val sample = sin(2.0 * Math.PI * freqHz * time) * envelope * volume
            buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        val track = AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()
        Thread.sleep(durationMs.toLong() + 20)
        track.stop()
        track.release()
    }

    fun vibrate(type: String = "light") {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                val ms = when (type) {
                    "success" -> longArrayOf(0, 40, 60, 40)
                    "medium" -> longArrayOf(0, 35)
                    else -> longArrayOf(0, 15)
                }
                vibrator.vibrate(VibrationEffect.createWaveform(ms, -1))
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {}
    }
}
