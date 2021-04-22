package com.erasmus.upv.eps.wearables.di

import com.erasmus.upv.eps.wearables.WearablesApplication_HiltComponents
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun providePlayerRepository(playerDao: PlayerDao): PlayerRepository{
        return PlayerRepository(playerDao)
    }


}