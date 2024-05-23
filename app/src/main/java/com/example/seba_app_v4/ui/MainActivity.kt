package com.example.seba_app_v4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.seba_app_v4.R
import com.example.seba_app_v4.bddLocale.Releves
import com.example.seba_app_v4.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.example.seba_app_v4.modeles.Releve
import com.michael.sqlite.bdd.RelevesCRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.lang.StringBuilder
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btValider.visibility = View.INVISIBLE
        binding.edtCode.visibility = View.INVISIBLE
        println("début")
        binding.apply {
            btConnexion.setOnClickListener {
                val ip = edtAddressIP.text.toString()
                val port = 1234
                CoroutineScope(Dispatchers.IO).launch {
                    val connexion = seConnecterou(ip, port)
                    btValider.setOnClickListener {
                        if (connexion != null) {
                            binding.btValider.visibility = View.VISIBLE
                            binding.edtCode.visibility = View.VISIBLE
                            CoroutineScope(Dispatchers.IO).launch {
                                val leCodeRecu = recevoirCode(connexion)
                                if (leCodeRecu.toInt() == edtCode.text.toString().toInt()) {
                                    envoyerResultat(connexion, "ok")
                                    val monobjet = recevoirJson(connexion)
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        suppressionRelevesPrecedent()
                                        enregistrementRecubdd(monobjet)
                                    }
                                } else {
                                    envoyerResultat(connexion, "nok")
                                    connexion.close()
                                }
                            }
                        }
                    }

                }
            }
        }

        binding.btRemplir.setOnClickListener {
            val intent = Intent(this@MainActivity, ListerRemplissage::class.java)
            startActivity(intent)
        }
    }

    private suspend fun seConnecterou(ADDRESS: String, PORT: Int): Socket? {
        val connexion = Socket(ADDRESS, PORT)
        return if (connexion.isConnected) {
            connexion
        } else {
            null
        }
    }


    private suspend fun recevoirCode(socket: Socket): String{
        return try {
            val inputStrem = socket.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStrem))
            val codeArray = CharArray(4)
            bufferedReader.read(codeArray, 0, 4)
            String(codeArray)
        }catch (e: IOException){
            e.printStackTrace()
            null
        }.toString()
    }

    private suspend fun envoyerResultat(socket: Socket, message: String) {
        val connexion = socket
        val writer = PrintWriter(OutputStreamWriter(connexion.getOutputStream()))
        writer.println(message)
        writer.flush()
    }

    private suspend fun recevoirJson(socket: Socket): Releves? {
        var objet: Releves? = null

        val connexion = socket

        try {
            val reader = BufferedReader(InputStreamReader(connexion.getInputStream()))
            val jsonBuilder = StringBuilder()

            var line: String? = reader.readLine()
            while (line != null) {
                jsonBuilder.append(line)
                line = reader.readLine()
            }

            val json = jsonBuilder.toString()
            val gson = Gson()
            objet = gson.fromJson(json, Releves::class.java)
        } catch (e: JsonSyntaxException) {
            objet = null
        } finally {
            connexion.close()
        }
        return objet
    }

    private suspend fun enregistrementRecubdd(lobjet: Releves?) {
        val relevesCRUD = RelevesCRUD(this)
        for (i in 0 until lobjet!!.size) {
            val leretourInsert = relevesCRUD.insertReleve(
                Releve(
                    id = null,
                    lobjet.get(i).NomCampagne,
                    lobjet.get(i).nom,
                    lobjet.get(i).ph,
                    lobjet.get(i).debit,
                    lobjet.get(i).temperature,
                    lobjet.get(i).conductivite_electrique,
                    lobjet.get(i).turbidite,
                    lobjet.get(i).niveaux_oxygen_dissous,
                    lobjet.get(i).nitrate_phophate,
                    lobjet.get(i).metaux_lourd,
                    lobjet.get(i).substances_organiques,
                    lobjet.get(i).micros_organismes,
                    lobjet.get(i).chlorophylle,
                    "00/00/0000"
                )
            )
            if (leretourInsert == (-1).toLong()) {
                Log.e("BDD", "erreur lors de l'insertion")
            }
        }
    }

    private suspend fun suppressionRelevesPrecedent(){
        val relevesCRUD = RelevesCRUD(this)
        for (i in 0 until relevesCRUD.getnbrReleve()){
            relevesCRUD.deleteReleve(i)
        }
    }
}


