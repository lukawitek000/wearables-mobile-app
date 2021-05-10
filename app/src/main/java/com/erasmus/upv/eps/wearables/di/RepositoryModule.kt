package com.erasmus.upv.eps.wearables.di

import com.erasmus.upv.eps.wearables.WearablesApplication_HiltComponents
import com.erasmus.upv.eps.wearables.db.dao.*
import com.erasmus.upv.eps.wearables.repositories.*
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
    fun provideTeamRepository(teamDao: TeamDao, playerDao: PlayerDao): TeamRepository {
        return TeamRepository(teamDao, playerDao)
    }

    @Singleton
    @Provides
    fun provideMatchRepository(matchDao: MatchDao): MatchRepository {
        return MatchRepository(matchDao)
    }

    @Singleton
    @Provides
    fun provideConfigRepository(configDao: ConfigDao): ConfigRepository{
        return ConfigRepository(configDao)
    }

    @Singleton
    @Provides
    fun provideStatisticsRepository(statisticsDao: StatisticsDao): StatisticsRepository{
        return StatisticsRepository(statisticsDao)
    }
}