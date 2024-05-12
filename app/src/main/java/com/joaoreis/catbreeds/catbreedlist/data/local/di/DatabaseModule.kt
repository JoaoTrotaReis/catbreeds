package com.joaoreis.catbreeds.catbreedlist.data.local.di

import android.content.Context
import androidx.room.Room
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedDatabase
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesDatabase(@ApplicationContext appContext: Context): CatBreedDatabase {
        return Room.databaseBuilder(
            appContext,
            CatBreedDatabase::class.java,
            "CatBreeds"
        ).build()
    }

    @Provides
    fun providesCatBreedsDao(catBreedDatabase: CatBreedDatabase): CatBreedsDao {
        return catBreedDatabase.catBreedDao()
    }
}