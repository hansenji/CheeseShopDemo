package com.vikingsen.cheesedemo.model.database

import android.app.Application
import android.arch.persistence.room.Room
import com.vikingsen.cheesedemo.model.database.cheese.CheeseDao
import com.vikingsen.cheesedemo.model.database.comment.CommentDao
import dagger.Module
import dagger.Provides
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideShopDatabase(application: Application): ShopDatabase {
        return Room.databaseBuilder(application, ShopDatabase::class.java, ShopDatabase.DATABASE_NAME)
                .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    fun provideCheeseDao(shopDatabase: ShopDatabase): CheeseDao {
        return shopDatabase.cheeseDao()
    }

    @Provides
    @Singleton
    fun provideCommentDao(shopDatabase: ShopDatabase): CommentDao {
        return shopDatabase.commentDao()
    }
}