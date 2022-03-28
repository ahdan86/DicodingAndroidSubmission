package com.ahdan.githubuser2.api

import com.ahdan.githubuser2.model.*
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

    @GET("/users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getListFollower(
        @Path("username") login : String
    ): Call<List<FollowerResponseItem>>

    @GET("/users/{username}/following")
    @Headers("Authorization: token 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")
    fun getListFollowing(
        @Path("username") login : String
    ): Call<List<FollowingResponseItem>>
}