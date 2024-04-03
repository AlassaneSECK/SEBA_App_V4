package com.michael.sqlite.bdd
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.seba_app_v4.modeles.Releve


class RelevesCRUD(cxt: Context) {

    private var releves: RelevesStructureDB? = null
    private var bdd: SQLiteDatabase? = null

    init {
        releves = RelevesStructureDB(cxt, RelevesStructureDB.TABLE_MESURES, null, 1)
    }

    fun openForWrite() {
        bdd = releves!!.writableDatabase
    }

    fun openForRead() {
        bdd = releves!!.readableDatabase
    }

    fun close() {
        releves!!.close()
    }

    fun readAllReleve():ArrayList<Releve>{
        val retValue = ArrayList<Releve>()
        openForRead()
        val rs = bdd!!.query(
            RelevesStructureDB.TABLE_MESURES,
            arrayOf(
                RelevesStructureDB.COL_ID,
                RelevesStructureDB.COL_NOMCAMPAGNE,
                RelevesStructureDB.COL_NOM,
                RelevesStructureDB.COL_PH,
                RelevesStructureDB.COL_DEBIT,
                RelevesStructureDB.COL_TEMPERATURE,
                RelevesStructureDB.COL_CONDUCTIVITE_ELECTRIQUE,
                RelevesStructureDB.COL_TURBIDITE,
                RelevesStructureDB.COL_NIVEAUX_OXYGEN_DISSOUS,
                RelevesStructureDB.COL_NITRATE_PHOSPHATE,
                RelevesStructureDB.COL_METAUX_LOURD,
                RelevesStructureDB.COL_SUBSTANCES_ORGANIQUES,
                RelevesStructureDB.COL_MICROS_ORGANISMES,
                RelevesStructureDB.COL_CHLOROPHYLLE,
                RelevesStructureDB.COL_HORODATAGE,
            ), null, null, null, null, null
        )
        if (rs.count > 0) {
            while (rs.moveToNext()) {
                retValue.add(
                    Releve(
                        rs.getInt(RelevesStructureDB.NUM_COL_ID),
                        rs.getString(RelevesStructureDB.NUM_COL_NOM_CAMPAGNE),
                        rs.getString(RelevesStructureDB.NUM_COL_NOM),
                        rs.getString(RelevesStructureDB.NUM_COL_PH),
                        rs.getString(RelevesStructureDB.NUM_COL_DEBIT),
                        rs.getString(RelevesStructureDB.NUM_COL_TEMPERATURE),
                        rs.getString(RelevesStructureDB.NUM_COL_CONDUCTIVITE_ELECTRIQUE),
                        rs.getString(RelevesStructureDB.NUM_COL_TURBIDITE),
                        rs.getString(RelevesStructureDB.NUM_COL_NIVEAUX_OXYGEN_DISSOUS),
                        rs.getString(RelevesStructureDB.NUM_COL_NITRATE_PHOSPHATE),
                        rs.getString(RelevesStructureDB.NUM_COL_METAUX_LOURD),
                        rs.getString(RelevesStructureDB.NUM_COL_SUBSTANCES_ORGANIQUES),
                        rs.getString(RelevesStructureDB.NUM_COL_MICROS_ORGANISMES),
                        rs.getString(RelevesStructureDB.NUM_COL_CHLOROPHYLLE),
                        rs.getString(RelevesStructureDB.NUM_COL_HORODATAGE)
                    )
                )
            }
        }
        return retValue
    }


    fun insertReleve( releve: Releve):Long{
        openForWrite()
        val cv = ContentValues()
        // cv.put(RelevesStructureDB.COL_ID, null)
        cv.put(RelevesStructureDB.COL_NOMCAMPAGNE,releve.NomCampagne)
        cv.put(RelevesStructureDB.COL_NOM,releve.nom)
        cv.put(RelevesStructureDB.COL_PH,releve.ph)
        cv.put(RelevesStructureDB.COL_DEBIT,releve.debit)
        cv.put(RelevesStructureDB.COL_TEMPERATURE,releve.temperature)
        cv.put(RelevesStructureDB.COL_CONDUCTIVITE_ELECTRIQUE,releve.conductivite_electrique)
        cv.put(RelevesStructureDB.COL_TURBIDITE,releve.turbidite)
        cv.put(RelevesStructureDB.COL_NIVEAUX_OXYGEN_DISSOUS,releve.niveaux_oxygen_dissous)
        cv.put(RelevesStructureDB.COL_NITRATE_PHOSPHATE,releve.nitrate_phophate)
        cv.put(RelevesStructureDB.COL_METAUX_LOURD,releve.metaux_lourd)
        cv.put(RelevesStructureDB.COL_SUBSTANCES_ORGANIQUES,releve.substances_organiques)
        cv.put(RelevesStructureDB.COL_MICROS_ORGANISMES,releve.micros_organismes)
        cv.put(RelevesStructureDB.COL_CHLOROPHYLLE,releve.chlorophylle)
        cv.put(RelevesStructureDB.COL_HORODATAGE, releve.horodatage)
        val retValue = bdd!!.insert(RelevesStructureDB.TABLE_MESURES,null,cv)
        close()
        return retValue
    }

    fun deleteReleve( id: Int?):Int{
        openForWrite()
        val retValue=bdd!!.delete(RelevesStructureDB.TABLE_MESURES,"id=?",arrayOf<String>(id.toString()))
//        close()
        return retValue
    }

    fun updateReleve( id: Int?,
                      ph:String,
                      debit:String,
                      temperature:String,
                      conductivite_electrique:String,
                      turbidite:String,
                      niveaux_oxygen_dissous:String,
                      nitrate_phosphate:String,
                      metaux_lourd:String,
                      substances_organiques:String,
                      micros_organismes:String,
                      chlorophylle:String,
                      horodatage:String):Int{
        openForWrite()
        val cv = ContentValues()
        cv.put(RelevesStructureDB.COL_PH,ph)
        cv.put(RelevesStructureDB.COL_DEBIT,debit)
        cv.put(RelevesStructureDB.COL_TEMPERATURE,temperature)
        cv.put(RelevesStructureDB.COL_CONDUCTIVITE_ELECTRIQUE,conductivite_electrique)
        cv.put(RelevesStructureDB.COL_TURBIDITE,turbidite)
        cv.put(RelevesStructureDB.COL_NIVEAUX_OXYGEN_DISSOUS,niveaux_oxygen_dissous)
        cv.put(RelevesStructureDB.COL_NITRATE_PHOSPHATE,nitrate_phosphate)
        cv.put(RelevesStructureDB.COL_METAUX_LOURD,metaux_lourd)
        cv.put(RelevesStructureDB.COL_SUBSTANCES_ORGANIQUES,substances_organiques)
        cv.put(RelevesStructureDB.COL_MICROS_ORGANISMES,micros_organismes)
        cv.put(RelevesStructureDB.COL_CHLOROPHYLLE,chlorophylle)
        cv.put(RelevesStructureDB.COL_HORODATAGE,horodatage)
        val retValue=bdd!!.update(RelevesStructureDB.TABLE_MESURES,cv,"id=?",arrayOf<String>(id.toString()))
//        close()
        return retValue
    }


}