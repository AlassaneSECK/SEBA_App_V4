package com.example.seba_app_v4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import com.example.seba_app_v4.adapter.ReleveAdapter
import com.example.seba_app_v4.databinding.ActivityListerRemplissageBinding
import com.michael.sqlite.bdd.Releve
import com.michael.sqlite.bdd.RelevesCRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListerRemplissage : AppCompatActivity() {
    private lateinit var binding: ActivityListerRemplissageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lister_remplissage)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lister_remplissage)

        binding.apply {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val releveList = RelevesCRUD(this@ListerRemplissage).readAllReleve()
                    val adapter = ReleveAdapter(this@ListerRemplissage, releveList)

                    val listView: ListView = listView

                    listView.adapter = adapter

                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedReleve = releveList[position]
                        openReleveDetails(selectedReleve)
                    }
                } catch (e: Exception) {
                    Log.e("BDD", e.toString())
                }
            }


            btsendCampagne.setOnClickListener {
                squareContainer.visibility = View.VISIBLE
                listView.visibility = View.INVISIBLE
            }
            btnYes.setOnClickListener {
                squareContainer.visibility = View.VISIBLE
                Intent(this@ListerRemplissage, Envoi_Releve::class.java).also {
                    startActivity(it)
                }
            }
            btnNo.setOnClickListener {
                squareContainer.visibility = View.INVISIBLE
                listView.visibility = View.VISIBLE
            }
        }
    }


    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val releveList = RelevesCRUD(this@ListerRemplissage).readAllReleve()
                val adapter = ReleveAdapter(this@ListerRemplissage, releveList)

                withContext(Dispatchers.Main) {
                    binding.listView.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("BDD", e.toString())
            }
        }
    }

    override fun onBackPressed() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
        super.onBackPressed()
    }

    private fun openReleveDetails(releve: Releve) {
        val intent = Intent(this, Detail::class.java)
        intent.putExtra("releve", releve)
        startActivity(intent)
    }
}