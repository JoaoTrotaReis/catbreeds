package com.joaoreis.catbreeds.catbreedlist.data.local

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.BreedLocalDataSource
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class BreedLocalDataSourceImplementation(
    val catBreedsDao: CatBreedsDao
): BreedLocalDataSource {
    override fun getBreedList(): Result<List<CatBreed>> {
        return try {
            Result.Success(catBreedsDao.getCatBreeds().map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.Error()
        }
    }

    override fun saveBreedList(breeds: List<CatBreed>) {
        catBreedsDao.saveCatBreeds(breeds.map { it.toDatabaseEntity() })
    }

    override fun searchBreed(name: String): Result<List<CatBreed>> {
        return try {
            Result.Success(catBreedsDao.findCatBreedByName("%${name}%").map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.Error()
        }
    }
}