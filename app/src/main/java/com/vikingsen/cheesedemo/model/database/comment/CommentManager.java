/*
 * CommentManager.java
 *
 * Generated on: 04/01/2017 09:45:31
 *
 */



package com.vikingsen.cheesedemo.model.database.comment;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;


@javax.inject.Singleton
public class CommentManager extends CommentBaseManager {


    @javax.inject.Inject
    public CommentManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }


}