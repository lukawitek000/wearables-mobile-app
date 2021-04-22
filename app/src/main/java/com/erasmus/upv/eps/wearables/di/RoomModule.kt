package com.erasmus.upv.eps.wearables.di

import android.content.Context
import androidx.room.Room
import com.erasmus.upv.eps.wearables.db.AppDatabase
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideWearablesDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Wearables-Database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePLayerDao(wearablesDb: AppDatabase): PlayerDao{
        return wearablesDb.playerDao()
    }


}