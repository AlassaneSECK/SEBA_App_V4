package com.example.seba_app_v4

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.seba_app_v4.bddLocale.Valeur
import com.example.seba_app_v4.databinding.ActivityOcrBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.michael.sqlite.bdd.Releve

class OCR : AppCompatActivity() {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    lateinit var binding: ActivityOcrBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ocr)

        binding.apply {
            captureImage.setOnClickListener {
                takeImage()
                texteReconnu.text = ""
            }
            detectionText.setOnClickListener {
                processImage()
            }

            retour.setOnClickListener {
                recupValeur(Valeur(texteReconnu.text.toString()))
            }
        }
    }
    private fun recupValeur(valeur: Valeur) {
        val releve = intent.getSerializableExtra("releve") as? Releve
        val lavaleur : Valeur = valeur
        val intent = Intent(this, Detail::class.java).apply {
            putExtra("valeur", lavaleur)
            putExtra("releve", releve)
        }
        startActivity(intent)
    }

    private fun processImage() {
        if (imageBitmap != null) {
            val image = imageBitmap?.let {
                InputImage.fromBitmap(it, 0)
            }
            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener {
                        binding.texteReconnu.text = it.text

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Echec de détetion de texte", Toast.LENGTH_SHORT).show()

                    }
            }
        } else {
            Toast.makeText(this, "Veuillez sélectionner une image d'abord", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: Exception) {

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            if (imageBitmap != null) {
                binding.imageView.setImageBitmap(imageBitmap)
            }

        }
    }
}