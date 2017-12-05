package com.vikingsen.cheesedemo.model.database

import android.app.Application
import android.arch.persistence.room.Room
import org.dbtools.android.room.CloseableDatabaseManager
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopDatabaseManager
@Inject constructor(application: Application): CloseableDatabaseManager<ShopDatabase>(application) {

    fun getCheeseDao() = getDatabase().cheeseDao()
    fun getCommentDao() = getDatabase().commentDao()

    override fun createDatabase(application: Application): ShopDatabase {
        return Room.databaseBuilder(application, ShopDatabase::class.java, ShopDatabase.DATABASE_NAME)
                .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
                .fallbackToDestructiveMigration()
                .build()
    }
}