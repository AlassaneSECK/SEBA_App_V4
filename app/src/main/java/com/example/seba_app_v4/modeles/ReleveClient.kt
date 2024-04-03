package com.example.seba_app_v4.modeles

data class ReleveClient(
    val id:Int?,
    val NomCampagne: String?,
    val nom:String?,
    val ph:String?,
    val debit:String?,
    val temperature:String?,
    val conductivite_electrique:String?,
    val turbidite:String?,
    val niveaux_oxygen_dissous:String?,
    val nitrate_phophate:String?,
    val metaux_lourd:String?,
    val substances_organiques:String?,
    val micros_organismes:String?,
    val chlorophylle:String?,
    val horodatage:String?
)
