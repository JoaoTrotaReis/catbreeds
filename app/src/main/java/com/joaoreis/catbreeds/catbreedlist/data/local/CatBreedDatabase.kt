package com.joaoreis.catbreeds.catbreedlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CatBreedEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class CatBreedDatabase: RoomDatabase() {
    abstract fun catBreedDao(): CatBreedsDao
}