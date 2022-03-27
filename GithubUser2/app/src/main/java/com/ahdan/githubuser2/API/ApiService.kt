package com.ahdan.githubuser2.API

import com.ahdan.githubuser2.Model.SearchResponse
import com.ahdan.githubuser2.Model.UserDetailResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/search/users")
    @Headers("Authorization: token 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")
    fun searchUser(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("/users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") login : String
    ): Call<UserDetailResponse>
}