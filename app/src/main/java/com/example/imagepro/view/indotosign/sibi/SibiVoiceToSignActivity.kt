package com.example.imagepro.view.indotosign.sibi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.imagepro.R
import com.example.imagepro.helper.ResultCondition
import com.example.imagepro.viewmodel.SibiViewModel
import com.example.imagepro.viewmodel.ViewModelFactory
import java.io.File
import java.util.Locale

class SibiVoiceToSignActivity : AppCompatActivity() {

    private lateinit var recordButton: Button
    private lateinit var transcriptionText: TextView
    private val REQUEST_CODE_SPEECH_INPUT = 1

    private val sibiViewModel: SibiViewModel by viewModels { factory }
    private lateinit var factory: ViewModelFactory
    private lateinit var progressBar: ProgressBar
    private lateinit var sendButton: Button
    private lateinit var videoView: VideoView

    private val playlistVideo: MutableList<Uri> = mutableListOf()
    private var indexNow = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sibi_voice_to_sign)

        recordButton = findViewById(R.id.recordButton)
        transcriptionText = findViewById(R.id.transcriptionText)

        recordButton.setOnClickListener {
            startRecord()
        }

        factory = ViewModelFactory.getInstance(this)
        progressBar = findViewById(R.id.progressBar)
        sendButton = findViewById(R.id.sendButton)
        videoView = findViewById(R.id.outputVideoView)

        setupAction()
    }

    private fun startRecord() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this, " ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                transcriptionText.text = result?.get(0)
            }
        }
    }

    private fun setupAction() {
        showLoading(false)
        sendButton.setOnClickListener {
            val text = transcriptionText.text.toString().lowercase()
            when {
                text.isEmpty() -> {
                    showDialog(false)
                }
                else -> {
                    getsibiVideos(text)
                }
            }
        }
    }

    private fun getsibiVideos(kalimat: String) {
        when (kalimat.lowercase()) {
            "selamat malam" -> {
                getVideoForSpecialCase(kalimat)
            }
            "selamat siang" -> {
                getVideoForSpecialCase(kalimat)
            }
            "selamat sore" -> {
                getVideoForSpecialCase(kalimat)
            }
            "hari ini" -> {
                getVideoForSpecialCase(kalimat)
            }
            "kepala sekolah" -> {
                getVideoForSpecialCase(kalimat)
            }
            else -> {
                // Jika input bukan kalimat khusus, lakukan pemrosesan seperti sebelumnya
                val kataKata = kalimat.split(" ")

                indexNow = 0
                playlistVideo.clear()

                val videoResponseList = mutableListOf<Pair<String, ByteArray>>()

                for (kata in kataKata) {
                    val currentKata = kata

                    val finalKata = currentKata

                    sibiViewModel.getSibiVideo(finalKata).observe(this) { hasil ->
                        when (hasil) {
                            is ResultCondition.LoadingState -> {
                                showLoading(true)
                            }
                            is ResultCondition.ErrorState -> {
                                showLoading(false)
                                showDialog(false)
                            }
                            is ResultCondition.SuccessState -> {
                                showLoading(false)
                                val dataVideo: ByteArray = hasil.data.bytes()

                                videoResponseList.add(finalKata to dataVideo)

                                if (videoResponseList.size == kataKata.size) {
                                    videoResponseList.sortBy { kataKata.indexOf(it.first) }
                                    processVideos(videoResponseList)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getVideoForSpecialCase(kalimat: String) {
        // Ambil video langsung untuk kalimat khusus
        sibiViewModel.getSibiVideo(kalimat).observe(this) { hasil ->
            when (hasil) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                    showDialog(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val dataVideo: ByteArray = hasil.data.bytes()
                    // Proses video langsung
                    processVideos(listOf(kalimat to dataVideo))
                }
            }
        }
    }

    private fun processVideos(videoResponseList: List<Pair<String, ByteArray>>) {
        for ((kata, dataVideo) in videoResponseList) {
            saveVideoToPlaylist(kata, dataVideo)
        }

        playNextVideo()
    }

    private fun saveVideoToPlaylist(kata: String, dataVideo: ByteArray?) {
        if (dataVideo != null && dataVideo.isNotEmpty()) {
            val fileSementara = File.createTempFile("videoSementara", ".mp4", cacheDir)
            fileSementara.writeBytes(dataVideo)

            playlistVideo.add(Uri.fromFile(fileSementara))
        }
    }

    private fun playNextVideo() {
        if (indexNow < playlistVideo.size) {
            videoView.setVideoURI(playlistVideo[indexNow])
            Log.d("KATA", "kata: index: $indexNow")

            val kontrolMedia = MediaController(this)
            kontrolMedia.setAnchorView(videoView)
            videoView.setMediaController(kontrolMedia)

            videoView.setOnCompletionListener {
                indexNow++
                playNextVideo()
            }

            videoView.start()
        }
    }

    private fun showDialog(isSuccess: Boolean) {
        if (!isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Kata belum tersedia.")
                setMessage("Maaf kata yang anda masukkan belum tersedia, kami akan terus melakukan update secara berkala.")
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}