
package com.vikingsen.cheesedemo.model.database

import android.app.Application
import com.vikingsen.cheesedemo.log.DBToolsTimberLogger
import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.AndroidDatabaseBaseManager
import org.dbtools.android.domain.config.DatabaseConfig
import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.SQLCipherDatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.AndroidDBToolsContentValues
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import org.dbtools.android.domain.log.DBToolsLogger


class AppDatabaseConfig(val application: Application)  : DatabaseConfig {

    override fun identifyDatabases(databaseManager: AndroidDatabaseBaseManager) {
        databaseManager.addDatabase(application, DatabaseManagerConst.MAIN_DATABASE_NAME, DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION)
    }

    override fun createNewDatabaseWrapper(androidDatabase: AndroidDatabase) : DatabaseWrapper<*, *> {
        return SQLCipherDatabaseWrapper(application, androidDatabase.path, null)
    }

    override fun createNewDBToolsContentValues() : DBToolsContentValues<*> {
        return AndroidDBToolsContentValues()
    }

    override fun createNewDBToolsLogger() : DBToolsLogger {
        return DBToolsTimberLogger()
    }


}