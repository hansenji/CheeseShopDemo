package com.vikingsen.cheesedemo.model.database

import android.app.Application
import androidx.room.Room
import org.dbtools.android.room.CloseableDatabaseWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopDatabaseWrapper
@Inject constructor(
    application: Application
) : CloseableDatabaseWrapper<ShopDatabase>(application) {

    override fun createDatabase(): ShopDatabase {
        return Room.databaseBuilder(application, ShopDatabase::class.java, ShopDatabase.DATABASE_NAME)
//            .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory()) // This is used for providing our own version of sqlite
//            .addMigrations(*CalendarDatabase.migrations)
            .fallbackToDestructiveMigration()
            .build()
    }
}