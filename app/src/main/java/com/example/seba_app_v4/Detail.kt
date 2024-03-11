package com.example.seba_app_v4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.seba_app_v4.databinding.ActivityDetailBinding
import com.michael.sqlite.bdd.Releve
import com.michael.sqlite.bdd.RelevesCRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val releve: Releve = intent.getSerializableExtra("releve") as Releve
        binding.apply {
//            nom.isFocusable = false
//            nom.isClickable = false
            nom.setText(releve.nom)
            ph.setText(releve.ph)
            debit.setText(releve.debit)
            temperature.setText(releve.temperature)
            conductiviteElectrique.setText(releve.conductivite_electrique)
            turbidite.setText(releve.turbidite)
            niveauxOxygenDissous.setText(releve.niveaux_oxygen_dissous)
            nitratePhosphate.setText(releve.nitrate_phophate)
            metauxLourd.setText(releve.metaux_lourd)
            substancesOrganiques.setText(releve.substances_organiques)
            microsOrganismes.setText(releve.micros_organismes)
            chlorophylle.setText(releve.chlorophylle)

            enregistrer.setOnClickListener {
                val relevesCRUD = RelevesCRUD(this@Detail)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = Date()
                val formattedDate = dateFormat.format(currentDate)
                relevesCRUD.updateReleve(
                    releve.id,
                    ph.text.toString(),
                    debit.text.toString(),
                    temperature.text.toString(),
                    conductiviteElectrique.text.toString(),
                    turbidite.text.toString(),
                    niveauxOxygenDissous.text.toString(),
                    nitratePhosphate.text.toString(),
                    metauxLourd.text.toString(),
                    substancesOrganiques.text.toString(),
                    microsOrganismes.text.toString(),
                    chlorophylle.text.toString(),
                    formattedDate
                )
                Toast.makeText(this@Detail, "Les données ont été enregistré", Toast.LENGTH_LONG)
                    .show()
            }
            supprimer.setOnClickListener {
                val relevesCRUD = RelevesCRUD(this@Detail)
                relevesCRUD.deleteReleve(releve.id)
                finish()
            }
            retour.setOnClickListener {
                Intent(this@Detail, ListerRemplissage::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
//
//    override fun onResume() {
//        Log.e("DETAIL", "onResume")
//        val releve: Releve = intent.getSerializableExtra("releve") as Releve
//        binding.apply {
//            nom.setText(releve.nom)
//            ph.setText(releve.ph)
//            debit.setText(releve.debit)
//            temperature.setText(releve.temperature)
//            conductiviteElectrique.setText(releve.conductivite_electrique)
//            turbidite.setText(releve.turbidite)
//            niveauxOxygenDissous.setText(releve.niveaux_oxygen_dissous)
//            nitratePhosphate.setText(releve.nitrate_phophate)
//            metauxLourd.setText(releve.metaux_lourd)
//            substancesOrganiques.setText(releve.substances_organiques)
//            microsOrganismes.setText(releve.micros_organismes)
//            chlorophylle.setText(releve.chlorophylle)
//        }
//        super.onResume()
//    }
//
//    override fun onDestroy() {
//        Log.e("DETAIL", "onDestroy")
//        val releve: Releve = intent.getSerializableExtra("releve") as Releve
//        binding.apply {
//            nom.setText(releve.nom)
//            ph.setText(releve.ph)
//            debit.setText(releve.debit)
//            temperature.setText(releve.temperature)
//            conductiviteElectrique.setText(releve.conductivite_electrique)
//            turbidite.setText(releve.turbidite)
//            niveauxOxygenDissous.setText(releve.niveaux_oxygen_dissous)
//            nitratePhosphate.setText(releve.nitrate_phophate)
//            metauxLourd.setText(releve.metaux_lourd)
//            substancesOrganiques.setText(releve.substances_organiques)
//            microsOrganismes.setText(releve.micros_organismes)
//            chlorophylle.setText(releve.chlorophylle)
//        }
//        super.onDestroy()
//    }
//
//    override fun onPause() {
//        Log.e("DETAIL", "onPause")
//        val releve: Releve = intent.getSerializableExtra("releve") as Releve
//        binding.apply {
//            nom.setText(releve.nom)
//            ph.setText(releve.ph)
//            debit.setText(releve.debit)
//            temperature.setText(releve.temperature)
//            conductiviteElectrique.setText(releve.conductivite_electrique)
//            turbidite.setText(releve.turbidite)
//            niveauxOxygenDissous.setText(releve.niveaux_oxygen_dissous)
//            nitratePhosphate.setText(releve.nitrate_phophate)
//            metauxLourd.setText(releve.metaux_lourd)
//            substancesOrganiques.setText(releve.substances_organiques)
//            microsOrganismes.setText(releve.micros_organismes)
//            chlorophylle.setText(releve.chlorophylle)
//        }
//        super.onPause()
//    }
}