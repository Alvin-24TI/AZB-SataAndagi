package com.example.azb_sataandagi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityQrScannerBinding
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

class QrScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrScannerBinding

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                decodeQrFromBitmap(bitmap)
            } else {
                Toast.makeText(this, "Pengambilan foto kamera dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val inputStream = contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        decodeQrFromBitmap(bitmap)
                    } else {
                        Toast.makeText(this, "Gagal memuat file gambar!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Gagal memproses gambar galeri!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Scan QR Code Member"
            subtitle = "Kamera & Galeri Foto QR"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnScanCamera.setOnClickListener {
            cameraLauncher.launch(null)
        }

        binding.btnUploadGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun decodeQrFromBitmap(bitmap: Bitmap) {
        try {
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

            val source = RGBLuminanceSource(width, height, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = MultiFormatReader().decode(binaryBitmap)

            openResultCardPage(result.text)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Tidak menemukan QR Code pada foto ini! Pastikan foto QR cukup jelas.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openResultCardPage(rawContent: String) {
        val intent = Intent(this, QrResultActivity::class.java).apply {
            putExtra(QrResultActivity.EXTRA_RAW_QR, rawContent)
        }
        startActivity(intent)
    }
}
