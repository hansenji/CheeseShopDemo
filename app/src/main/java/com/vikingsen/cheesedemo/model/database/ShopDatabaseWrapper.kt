package com.vikingsen.cheesedemo.model.database

import android.app.Application
import android.arch.persistence.room.Room
import org.dbtools.android.room.CloseableDatabaseWrapper
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopDatabaseWrapper
@Inject constructor(application: Application): CloseableDatabaseWrapper<ShopDatabase>(application) {

    fun getCheeseDao() = getDatabase().cheeseDao()
    fun getCommentDao() = getDatabase().commentDao()

    override fun createDatabase(): ShopDatabase {
        return Room.databaseBuilder(application, ShopDatabase::class.java, ShopDatabase.DATABASE_NAME)
                .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
            // TODO: 8/13/18 ADD V2 MIGRATION AND TESTS
                .fallbackToDestructiveMigration()
                .build()
    }
}