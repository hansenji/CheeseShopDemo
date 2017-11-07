
package com.vikingsen.cheesedemo.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.config.DatabaseConfig
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DatabaseManager
@Inject constructor(databaseConfig: DatabaseConfig) : DatabaseBaseManager(databaseConfig) {

    companion object {
         const val MAIN_TABLES_VERSION = 1
         const val MAIN_VIEWS_VERSION = 1
    }

    override fun onUpgrade(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        logger.i(TAG, "Upgrading database [${androidDatabase.name}] from version $oldVersion to $newVersion")
    }

    override fun onUpgradeViews(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        logger.i(TAG, "Upgrading database [${androidDatabase.name}] VIEWS from version $oldVersion to $newVersion")
        // automatically drop/create views
        super.onUpgradeViews(androidDatabase, oldVersion, newVersion)
    }


}