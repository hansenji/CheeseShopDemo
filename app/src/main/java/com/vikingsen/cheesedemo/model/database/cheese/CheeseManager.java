/*
 * CheeseManager.java
 *
 * Generated on: 04/01/2017 09:45:31
 *
 */



package com.vikingsen.cheesedemo.model.database.cheese;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;


@javax.inject.Singleton
public class CheeseManager extends CheeseBaseManager {


    @javax.inject.Inject
    public CheeseManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }


}