package com.erasmus.upv.eps.wearables.di

import com.erasmus.upv.eps.wearables.WearablesApplication_HiltComponents
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.db.dao.TeamDao
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
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

    @Singleton
    @Provides
    fun provideTeamRepository(teamDao: TeamDao): TeamRepository {
        return TeamRepository(teamDao)
    }


}