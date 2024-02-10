package com.example.imagepro.view.indotosign.sibi

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.imagepro.R
import com.example.imagepro.helper.ResultCondition
import com.example.imagepro.viewmodel.SibiViewModel
import com.example.imagepro.viewmodel.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import java.io.File

class SibiTextToSignActivity : AppCompatActivity() {
    private val sibiViewModel: SibiViewModel by viewModels { factory }
    private lateinit var factory: ViewModelFactory
    private lateinit var progressBar: ProgressBar
    private lateinit var text: EditText
    private lateinit var etlText: TextInputLayout
    private lateinit var sendButton: Button
    private lateinit var videoView: VideoView

    private val playlistVideo: MutableList<Uri> = mutableListOf()
    private var indexNow = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sibi_text_to_sign)

        factory = ViewModelFactory.getInstance(this)
        progressBar = findViewById(R.id.progressBar)
        text = findViewById(R.id.inputText)
        etlText = findViewById(R.id.etlText)
        sendButton = findViewById(R.id.sendButton)
        videoView = findViewById(R.id.outputVideoView)

        setupAction()
    }

    private fun setupAction() {
        showLoading(false)
        sendButton.setOnClickListener {
            val text = text.text.toString().lowercase()
            when {
                text.isEmpty() -> {
                    etlText.error = "Masukkan teks"
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