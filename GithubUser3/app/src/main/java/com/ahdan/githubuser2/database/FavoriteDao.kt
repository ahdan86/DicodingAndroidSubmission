package com.ahdan.githubuser2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE login = :user)")
    fun isExist(user : String) : Boolean

    @Query("DELETE from favorite WHERE login = :user")
    fun deleteById(user: String)
}