package com.muhammadwahyudin.kadefootballapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import org.jetbrains.anko.db.*

const val DB_NAME = "kade_football_app.db"

class DatabaseHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, DB_NAME, null, 1) {

    companion object {
        private var instance: DatabaseHelper? = null

        fun getInstance(ctx: Context): DatabaseHelper {
            if (instance == null) instance = DatabaseHelper(ctx.applicationContext)
            return instance as DatabaseHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteEvent.TABLE_NAME, true,
            FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
            FavoriteEvent.EVENT_DATE to TEXT,
            FavoriteEvent.EVENT_NAME to TEXT,
            FavoriteEvent.TEAM_HOME_NAME to TEXT,
            FavoriteEvent.TEAM_HOME_SCORE to TEXT,
            FavoriteEvent.TEAM_HOME_BADGE_URL to TEXT,
            FavoriteEvent.TEAM_AWAY_NAME to TEXT,
            FavoriteEvent.TEAM_AWAY_SCORE to TEXT,
            FavoriteEvent.TEAM_AWAY_BADGE_URL to TEXT
        )
        db.createTable(
            Team.TABLE_NAME, true,
            Team.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Team.ID_TEAM to TEXT + UNIQUE,
            Team.ID_LEAGUE to TEXT,
            Team.STR_TEAM to TEXT,
            Team.TEAM_BADGE_URL to TEXT,
            Team.FORMED_YEAR to TEXT,
            Team.STR_ALTERNATE to TEXT,
            Team.STR_COUNTRY to TEXT,
            Team.STR_LEAGUE to TEXT,
            Team.STR_STADIUM to TEXT,
            Team.STR_STADIUM_LOC to TEXT


        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteEvent.TABLE_NAME, true)
        db.dropTable(Team.TABLE_NAME, true)
    }

}

val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(applicationContext)