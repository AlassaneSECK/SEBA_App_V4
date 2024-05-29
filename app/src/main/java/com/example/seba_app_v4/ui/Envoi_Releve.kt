package com.example.seba_app_v4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.seba_app_v4.R
import com.example.seba_app_v4.databinding.ActivityEnvoiReleveBinding
import com.google.gson.Gson
import com.example.seba_app_v4.modeles.Releve
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
import java.net.InetSocketAddress
import java.net.Socket

class Envoi_Releve : AppCompatActivity() {
    lateinit var binding: ActivityEnvoiReleveBinding
    var laSocket: Socket? = null
    var laliste: ArrayList<Releve>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envoi_releve)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_envoi_releve)
        val regex: Regex = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?(\\.|\$)){4}".toRegex()
        binding.apply {
            btEnvoyerCode.visibility = View.INVISIBLE
            btEnvoyerDonnees.visibility = View.INVISIBLE
            var flag = true
            btConnexionSend.setOnClickListener {
                if (regex.matches(edtIP.text)) {
                    if (edtIP.text.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                laSocket = seConnecter(edtIP.text.toString(), 1234)
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@Envoi_Releve, "erreur de connexion : $e", Toast.LENGTH_LONG).show()
                                }
                                flag = false
                            }
                            if (!flag){
                                btConnexionSend.visibility = View.VISIBLE
                            }else{
                                btConnexionSend.visibility = View.INVISIBLE
                            }
                            lifecycleScope.launch(Dispatchers.Main) {
                                if (laSocket == null) {
                                    imgSignalConnection.setImageResource(R.drawable.rouge)
                                } else {
                                    imgSignalConnection.setImageResource(R.drawable.vert)
                                    btEnvoyerCode.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@Envoi_Releve,
                            "Remplissez d'abord l'adresse ip",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@Envoi_Releve,
                        "Entrez le bon format d'adresse ip",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            btEnvoyerCode.setOnClickListener {
                val leCode = generateRandomCode()
                tvCode.text = leCode.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    envoyerCode(laSocket, leCode)
                }
                btEnvoyerDonnees.visibility = View.VISIBLE
                btEnvoyerCode.visibility = View.INVISIBLE
            }
            btEnvoyerDonnees.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val reponse = recevoirReponse(laSocket)
                    withContext(Dispatchers.Main) {
                        if (reponse == "ok") {
                            val relevesCRUD = RelevesCRUD(this@Envoi_Releve)
                            laliste = relevesCRUD.readAllReleve()
                            CoroutineScope(Dispatchers.IO).launch {
                                envoyerDonnees(laSocket, laliste)
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                if (recevoirDeconnexion(laSocket) == "deconnexion") {
                                    btEnvoyerCode.visibility = View.INVISIBLE
                                    btEnvoyerDonnees.visibility = View.INVISIBLE
                                    laSocket?.close()
                                }
                            }
                        } else if (reponse == "nok") {
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
                lifecycleScope.launch(Dispatchers.Main){
                    btConnexionSend.visibility = View.VISIBLE
                    btEnvoyerDonnees.visibility = View.INVISIBLE
                    imgSignalConnection.setImageDrawable(null)
                    tvCode.setText("")
                }
            }



            imgBtAnnuler.setOnClickListener {
                laSocket!!.close()
                finish()
            }
        }

    }

    private suspend fun seConnecter(ADDRESS: String, PORT: Int): Socket? {
        val socket = Socket()
        socket.connect(InetSocketAddress(ADDRESS,PORT),2000)
        return if (socket.isConnected) {
            socket
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
            socket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
            socket!!.close()
        }
    }


    private suspend fun recevoirDeconnexion(socket: Socket?): String {
        return try {
            val inputStream = socket?.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var codeDeconnexion = CharArray(11)
            bufferedReader.read(codeDeconnexion, 0, 11)
            String(codeDeconnexion)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }.toString()
    }

}