package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface BreedLocalDataSource {
    fun getBreedList(): Result<List<CatBreed>>
    fun saveBreedList(breeds: List<CatBreed>)
}