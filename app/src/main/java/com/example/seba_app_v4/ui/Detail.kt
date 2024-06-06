package com.example.seba_app_v4.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.seba_app_v4.R
import com.example.seba_app_v4.modeles.Valeur
import com.example.seba_app_v4.databinding.ActivityDetailBinding
import com.example.seba_app_v4.modeles.Releve
import com.michael.sqlite.bdd.RelevesCRUD
import java.text.SimpleDateFormat
import java.util.Date


class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    var valeur: Valeur? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val releve = intent.getSerializableExtra("releve") as? Releve

        valeur = intent.getSerializableExtra("valeur") as? Valeur


        binding.apply {


            nom.isFocusable = false
            nom.isClickable = false
            findAllEditText(rootView,valeur?.laValeur.toString())


            ph.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    ph.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            debit.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    debit.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            temperature.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    temperature.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            conductiviteElectrique.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    conductiviteElectrique.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            turbidite.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    turbidite.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            niveauxOxygenDissous.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    niveauxOxygenDissous.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            nitratePhosphate.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    nitratePhosphate.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            metauxLourd.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    metauxLourd.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            substancesOrganiques.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    substancesOrganiques.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }

            microsOrganismes.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    microsOrganismes.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }
            chlorophylle.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    chlorophylle.setText(valeur?.laValeur)
                    valeur = Valeur("")
                }
            }
            nom.setText(releve?.nom)
            ph.setText(releve?.ph)
            debit.setText(releve?.debit)
            temperature.setText(releve?.temperature)
            conductiviteElectrique.setText(releve?.conductivite_electrique)
            turbidite.setText(releve?.turbidite)
            niveauxOxygenDissous.setText(releve?.niveaux_oxygen_dissous)
            nitratePhosphate.setText(releve?.nitrate_phophate)
            metauxLourd.setText(releve?.metaux_lourd)
            substancesOrganiques.setText(releve?.substances_organiques)
            microsOrganismes.setText(releve?.micros_organismes)
            chlorophylle.setText(releve?.chlorophylle)



            if (ph.text.toString() == "null" ) {
                ph.isFocusable = false
                ph.isClickable = false
                idGph.setBackgroundColor(Color.RED)
            }
            if (debit.text.toString() == "null") {
                debit.isFocusable = false
                debit.isClickable = false
                idGdebit.setBackgroundColor(Color.RED)
            }
            if (temperature.text.toString() == "null") {
                temperature.isFocusable = false
                temperature.isClickable = false
                idGtemp.setBackgroundColor(Color.RED)
            }
            if (conductiviteElectrique.text.toString() == "null") {
                conductiviteElectrique.isFocusable = false
                conductiviteElectrique.isClickable = false
                idGCE.setBackgroundColor(Color.RED)
            }
            if (turbidite.text.toString() == "null") {
                turbidite.isFocusable = false
                turbidite.isClickable = false
                idGTurb.setBackgroundColor(Color.RED)
            }
            if (niveauxOxygenDissous.text.toString() == "null") {
                niveauxOxygenDissous.isFocusable = false
                niveauxOxygenDissous.isClickable = false
                idGNOD.setBackgroundColor(Color.RED)
            }
            if (nitratePhosphate.text.toString() == "null") {
                nitratePhosphate.isFocusable = false
                nitratePhosphate.isClickable = false
                idGNP.setBackgroundColor(Color.RED)
            }
            if (metauxLourd.text.toString() == "null") {
                metauxLourd.isFocusable = false
                metauxLourd.isClickable = false
                idGML.setBackgroundColor(Color.RED)
            }
            if (substancesOrganiques.text.toString() == "null") {
                substancesOrganiques.isFocusable = false
                substancesOrganiques.isClickable = false
                idGSO.setBackgroundColor(Color.RED)
            }
            if (microsOrganismes.text.toString() == "null") {
                microsOrganismes.isFocusable = false
                microsOrganismes.isClickable = false
                idGMO.setBackgroundColor(Color.RED)
            }
            if (chlorophylle.text.toString() == "null") {
                chlorophylle.isFocusable = false
                chlorophylle.isClickable = false
                idGChlorophylle.setBackgroundColor(Color.RED)
            }


            enregistrer.setOnClickListener {
                val relevesCRUD = RelevesCRUD(this@Detail)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = Date()
                val formattedDate = dateFormat.format(currentDate)
                relevesCRUD.updateReleve(
                    releve?.id,
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
                relevesCRUD.deleteReleve(releve?.id)
                finish()
            }
            retour.setOnClickListener {
                Intent(this@Detail, ListerRemplissage::class.java).also {
                    startActivity(it)
                }
            }
            btReconnaissance.setOnClickListener {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = Date()
                val formattedDate = dateFormat.format(currentDate)

                val relevepassage = Releve(
                    id = releve?.id,
                    NomCampagne = "",
                    nom = nom.text.toString(),
                    ph = ph.text.toString(),
                    debit = debit.text.toString(),
                    temperature = temperature.text.toString(),
                    conductivite_electrique = conductiviteElectrique.text.toString(),
                    turbidite = turbidite.text.toString(),
                    niveaux_oxygen_dissous = niveauxOxygenDissous.text.toString(),
                    nitrate_phophate = nitratePhosphate.text.toString(),
                    metaux_lourd = metauxLourd.text.toString(),
                    substances_organiques = substancesOrganiques.text.toString(),
                    micros_organismes = microsOrganismes.text.toString(),
                    chlorophylle = chlorophylle.text.toString(),
                    horodatage = ""
                )
                Intent(this@Detail, OCR::class.java).putExtra("releve",relevepassage).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun findAllEditText(view: View, lavaleur : String?) {
        var value : String? = lavaleur
        if (view is EditText) {
            view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    view.setText(value)
                }
            }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                findAllEditText(child, lavaleur)
            }
        }
    }
}