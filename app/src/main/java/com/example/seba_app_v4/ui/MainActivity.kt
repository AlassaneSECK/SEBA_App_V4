package com.example.seba_app_v4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.seba_app_v4.R
import com.example.seba_app_v4.modeles.Releves
import com.example.seba_app_v4.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
import java.lang.StringBuilder
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var connexion : Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btValider.visibility = View.INVISIBLE
        binding.edtCode.visibility = View.INVISIBLE
        val regex: Regex = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?(\\.|\$)){4}".toRegex()
        binding.apply {
            btConnexion.setOnClickListener {
                if (edtAddressIP.text.isNotEmpty()) {

                    val ip = edtAddressIP.text.toString()
                    val port = 1234
                    var flag = true
                    if (regex.matches(ip)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                connexion = seConnecter(ip, port)!!
                            }catch (e: Exception){
                                withContext(Dispatchers.Main){
                                    Toast.makeText(this@MainActivity, "erreur de connexion : $e", Toast.LENGTH_LONG).show()
                                }
                                flag = false
                            }

                            if (!flag){
                                lifecycleScope.launch(Dispatchers.Main) {
                                    btValider.visibility = View.INVISIBLE
                                    edtCode.visibility = View.INVISIBLE
                                    btConnexion.visibility = View.VISIBLE
                                }
                            }else{
                                lifecycleScope.launch(Dispatchers.Main) {
                                    btValider.visibility = View.VISIBLE
                                    edtCode.visibility = View.VISIBLE
                                    btConnexion.visibility = View.INVISIBLE
                                }
                            }
                            btValider.setOnClickListener {
                                if (connexion != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        if (edtCode.text.toString().isNotEmpty()){
                                            val leCodeRecu = recevoirCode(connexion)
                                            if (leCodeRecu.toInt() == edtCode.text.toString().toInt()) {
                                                envoyerResultat(connexion, "ok")
                                                val monobjet = recevoirJson(connexion)
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    suppressionCampagnePrecedente()
                                                }
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    enregistrementRecubdd(monobjet)
                                                    btValider.visibility = View.INVISIBLE
                                                    btConnexion.visibility = View.VISIBLE
                                                    edtCode.setText("")
                                                    edtCode.visibility = View.INVISIBLE
                                                }
                                            } else {
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    Toast.makeText(this@MainActivity,"Code erroné",Toast.LENGTH_LONG).show()
                                                }.join()
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    envoyerResultat(connexion, "nok")
                                                    connexion.close()
                                                }
                                            }
                                        }else{
                                            Toast.makeText(this@MainActivity,"remplissez d'abord le code", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }else{
                                    Toast.makeText(this@MainActivity, "Connexion au serveur échouée", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Entrez le bon format d'adresse ip",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity, "Remplissez d'abord l'adress ip", Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        binding.btRemplir.setOnClickListener {
            val intent = Intent(this@MainActivity, ListerRemplissage::class.java)
            startActivity(intent)
        }
    }

    private suspend fun seConnecter(ADDRESS: String, PORT: Int): Socket? {
        val socket = Socket()
        socket.connect(InetSocketAddress(ADDRESS,PORT), 2000)
        return if (socket.isConnected) {
            socket
        } else {
            null
        }
    }


    private suspend fun recevoirCode(socket: Socket): String {
        return try {
            val inputStrem = socket.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStrem))
            val codeArray = CharArray(4)
            bufferedReader.read(codeArray, 0, 4)
            String(codeArray)
        } catch (e: IOException) {
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

    private suspend fun suppressionCampagnePrecedente() {
        val relevesCRUD = RelevesCRUD(this)
        relevesCRUD.deleteAll()
    }
}


