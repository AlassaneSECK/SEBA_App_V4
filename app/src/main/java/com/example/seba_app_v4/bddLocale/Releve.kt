package com.michael.sqlite.bdd

import java.io.Serializable

data class Releve(
    val id: Int? = null,
    val NomCampagne: String?,
    val nom:String? = null,
    val ph:String? = null,
    val debit:String? = null,
    val temperature:String? = null,
    val conductivite_electrique:String? = null,
    val turbidite:String? = null,
    val niveaux_oxygen_dissous:String? = null,
    val nitrate_phophate:String? = null,
    val metaux_lourd:String? = null,
    val substances_organiques:String? = null,
    val micros_organismes:String? = null,
    val chlorophylle:String? = null,
    val horodatage:String? = null
):Serializable
