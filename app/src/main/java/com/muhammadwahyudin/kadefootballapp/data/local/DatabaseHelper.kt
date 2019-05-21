package com.muhammadwahyudin.kadefootballapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteEvent.TABLE_NAME, true)
    }

}

val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(applicationContext)