package com.example.seba_app_v4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.seba_app_v4.databinding.ActivityEnvoiReleveBinding
import com.google.gson.Gson
import com.michael.sqlite.bdd.Releve
import com.michael.sqlite.bdd.RelevesCRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.Random

class Envoi_Releve : AppCompatActivity() {
    lateinit var binding: ActivityEnvoiReleveBinding
    var laSocket: Socket? = null
    var laliste: ArrayList<Releve>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envoi_releve)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_envoi_releve)
        binding.apply {
            btEnvoyerCode.visibility = View.INVISIBLE
            btEnvoyerDonnees.visibility = View.INVISIBLE
            btConnexionSend.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    laSocket = seConnecterouDeconnecter(edtIP.text.toString(), 1234)
                    lifecycleScope.launch(Dispatchers.Main) {
                        if (laSocket == null) {
                            imgSignalConnection.setImageResource(R.drawable.rouge)
                        } else {
                            imgSignalConnection.setImageResource(R.drawable.vert)
                            btEnvoyerCode.visibility = View.VISIBLE
                        }
                    }
                }
            }

            btEnvoyerCode.setOnClickListener {
                val leCode = generateRandomCode()
                tvCode.text = leCode.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    envoyerCode(laSocket, leCode)
                }
                btEnvoyerDonnees.visibility = View.VISIBLE
            }
            btEnvoyerDonnees.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val reponse = recevoirReponse(laSocket)
                    withContext(Dispatchers.Main) {
                        if (reponse == "ok") {
                            imgSignalOkNok.setImageResource(R.drawable.vert)
                            val relevesCRUD = RelevesCRUD(this@Envoi_Releve)
                            laliste = relevesCRUD.readAllReleve()
                            CoroutineScope(Dispatchers.IO).launch {
                                envoyerDonnees(laSocket, laliste)
                            }
                        } else if (reponse == "nok") {
                            imgSignalOkNok.setImageResource(R.drawable.rouge)
                            Toast.makeText(this@Envoi_Releve, "CODE PAS BON", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(
                                this@Envoi_Releve,
                                "Problème de réception",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }



            imgBtAnnuler.setOnClickListener {
                laSocket?.close()
                finish()
            }
        }

    }

    private suspend fun seConnecterouDeconnecter(ADDRESS: String, PORT: Int): Socket? {
        val connexion = Socket(ADDRESS, PORT)
        return if (connexion.isConnected) {
            connexion
        } else {
            null
        }
    }

    private suspend fun envoyerCode(socket: Socket?, code: Int) {
        val connexion = socket
        val writer = PrintWriter(OutputStreamWriter(connexion?.getOutputStream()))
        writer.println(code)
        writer.flush()
    }

    private suspend fun recevoirReponse(socket: Socket?): String {
        return try {
            val inputStream = socket?.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val responseBuilder = StringBuilder()

            var char: Int
            while (bufferedReader.read().also { char = it } != -1) {
                responseBuilder.append(char.toChar())
                if (responseBuilder.length >= 2 && responseBuilder.substring(responseBuilder.length - 2) == "ok") {
                    break
                }
            }

            responseBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }.toString()
    }

    fun generateRandomCode(): Int {
        val nombreAleatoire = (1000..9999).random()
        return nombreAleatoire
    }

    private suspend fun envoyerDonnees(socket: Socket?, laliste: ArrayList<Releve>?) {
        try {
            val connexion = socket
            val gson = Gson()
            val listeJson = gson.toJson(laliste)

            val writer = PrintWriter(OutputStreamWriter(connexion?.getOutputStream()))
            writer.println(listeJson)
            writer.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}