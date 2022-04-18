package com.ahdan.githubuser2.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ahdan.githubuser2.database.Favorite
import com.ahdan.githubuser2.database.FavoriteDao
import com.ahdan.githubuser2.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }
    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoritesDao.getAllFavorites()
    fun insert(favorite: Favorite) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }
    fun delete(favorite: Favorite) {
        executorService.execute { mFavoritesDao.delete(favorite) }
    }
    fun update(favorite: Favorite) {
        executorService.execute { mFavoritesDao.update(favorite) }
    }
    fun isExist(username: String): Boolean {
        return mFavoritesDao.isExist(username)
    }
    fun deleteById(username: String){
        executorService.execute { mFavoritesDao.deleteById(username) }
    }
}