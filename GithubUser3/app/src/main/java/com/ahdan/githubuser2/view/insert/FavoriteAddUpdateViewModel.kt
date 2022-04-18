package com.ahdan.githubuser2.view.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.ahdan.githubuser2.database.Favorite
import com.ahdan.githubuser2.repository.FavoriteRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun update(favorite: Favorite) {
        mFavoriteRepository.update(favorite)
    }
    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
    fun isExist(username: String): Boolean {
        return mFavoriteRepository.isExist(username)
    }
    fun deleteById(username: String){
        mFavoriteRepository.deleteById(username)
    }
}