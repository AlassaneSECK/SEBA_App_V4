package com.example.seba_app_v4.ui

import android.content.Intent
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
    val valeur: Valeur? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val releve = intent.getSerializableExtra("releve") as? Releve

        var lavaleur = intent.getSerializableExtra("valeur") as? Valeur
        Toast.makeText(this, "La valeur est de : ${lavaleur?.laValeur}", Toast.LENGTH_LONG).show()


        binding.apply {


            nom.isFocusable = false
            nom.isClickable = false
            findAllEditText(rootView,lavaleur?.laValeur.toString())


            ph.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    ph.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            debit.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    debit.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            temperature.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    temperature.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            conductiviteElectrique.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    conductiviteElectrique.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            turbidite.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    turbidite.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            niveauxOxygenDissous.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    niveauxOxygenDissous.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            nitratePhosphate.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    nitratePhosphate.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            metauxLourd.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    metauxLourd.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            substancesOrganiques.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    substancesOrganiques.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }

            microsOrganismes.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    microsOrganismes.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
                }
            }
            chlorophylle.onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->
                if (hasfocus){
                    chlorophylle.setText(lavaleur?.laValeur)
                    lavaleur = Valeur("")
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