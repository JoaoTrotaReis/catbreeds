package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface BreedRemoteDataSource {
    fun getBreedList(): Result<List<CatBreed>>
}