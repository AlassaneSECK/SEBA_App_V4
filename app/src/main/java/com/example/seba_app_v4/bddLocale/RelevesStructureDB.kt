package com.michael.sqlite.bdd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class RelevesStructureDB(context : Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int):
    SQLiteOpenHelper(context , name, factory, version ){
    private var db: SQLiteDatabase?=null

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        db=sqLiteDatabase
        db!!.execSQL(CREATE_BDD)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE $TABLE_MESURES")
        onCreate(db!!)
    }

    companion object{
        val TABLE_MESURES="table_mesures"

        val COL_ID="id"
        val NUM_COL_ID=0

        val COL_NOMCAMPAGNE="idCampagne"
        val NUM_COL_NOM_CAMPAGNE=1

        val COL_NOM="nom"
        val NUM_COL_NOM=2

        val COL_PH="ph"
        val NUM_COL_PH=3

        val COL_DEBIT="debit"
        val NUM_COL_DEBIT=4

        val COL_TEMPERATURE="temperature"
        val NUM_COL_TEMPERATURE=5

        val COL_CONDUCTIVITE_ELECTRIQUE="conductivite_electrique"
        val NUM_COL_CONDUCTIVITE_ELECTRIQUE=6

        val COL_TURBIDITE="turbidite"
        val NUM_COL_TURBIDITE=7

        val COL_NIVEAUX_OXYGEN_DISSOUS="niveaux_oxygen_dissous"
        val NUM_COL_NIVEAUX_OXYGEN_DISSOUS=8

        val COL_NITRATE_PHOSPHATE="nitrate_phophate"
        val NUM_COL_NITRATE_PHOSPHATE=9

        val COL_METAUX_LOURD="metaux_lourd"
        val NUM_COL_METAUX_LOURD=10

        val COL_SUBSTANCES_ORGANIQUES="substances_organiques"
        val NUM_COL_SUBSTANCES_ORGANIQUES=11

        val COL_MICROS_ORGANISMES="micros_organismes"
        val NUM_COL_MICROS_ORGANISMES=12

        val COL_CHLOROPHYLLE="chlorophylle"
        val NUM_COL_CHLOROPHYLLE=13

        val COL_HORODATAGE="horodatage"
        val NUM_COL_HORODATAGE=14


        private val CREATE_BDD = "CREATE TABLE " + TABLE_MESURES +
                "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NOMCAMPAGNE + " TEXT NOT NULL," +
                COL_NOM + " TEXT NOT NULL," +
                COL_PH + " TEXT NOT NULL," +
                COL_DEBIT + " TEXT NOT NULL," +
                COL_TEMPERATURE + " TEXT NOT NULL," +
                COL_CONDUCTIVITE_ELECTRIQUE + " TEXT NOT NULL," +
                COL_TURBIDITE + " TEXT NOT NULL," +
                COL_NIVEAUX_OXYGEN_DISSOUS + " TEXT NOT NULL," +
                COL_NITRATE_PHOSPHATE + " TEXT NOT NULL," +
                COL_METAUX_LOURD + " TEXT NOT NULL," +
                COL_SUBSTANCES_ORGANIQUES + " TEXT NOT NULL," +
                COL_MICROS_ORGANISMES + " TEXT NOT NULL," +
                COL_CHLOROPHYLLE + " TEXT NOT NULL," +
                COL_HORODATAGE + " TEXT);"
    }
}
