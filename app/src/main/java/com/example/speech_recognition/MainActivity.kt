package com.example.speech_recognition

import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

// Enable Sensor

class MainActivity : AppCompatActivity() {
    private var speechRecognizer : SpeechRecognizer? = null

    // Enable Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable Sensor

        val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
        if (granted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer?.setRecognitionListener(createRecognitionListenerStringStream { recognize_text_view.text = it })

        recognize_start_button.setOnClickListener { speechRecognizer?.startListening(Intent(
            RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE)) }
        recognize_stop_button.setOnClickListener { speechRecognizer?.stopListening() }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    // Enable Sensor

    private fun createRecognitionListenerStringStream(onResult : (String)-> Unit) : RecognitionListener {
        return object : RecognitionListener {
            override fun onRmsChanged(rmsdB: Float) { /** 今回は特に利用しない */ }
            override fun onReadyForSpeech(params: Bundle) { onResult("話す") }
            override fun onBufferReceived(buffer: ByteArray) { onResult("バッファ受け取り") }
            override fun onPartialResults(partialResults: Bundle) { onResult("処理中") }
            override fun onEvent(eventType: Int, params: Bundle) { onResult("イベント") }
            override fun onBeginningOfSpeech() { onResult("開始") }
            override fun onEndOfSpeech() { onResult("終了") }
            override fun onError(error: Int) { onResult("エラー") }
            override fun onResults(results: Bundle) {
                val stringArray = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                for (element in stringArray.orEmpty()) {
                    onResult(element);
                }
            }
        }
    }

    companion object {
        private const val PERMISSIONS_RECORD_AUDIO = 1000
    }
}