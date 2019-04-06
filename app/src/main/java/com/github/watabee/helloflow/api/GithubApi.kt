package com.github.watabee.helloflow.api

import retrofit2.http.GET

interface GithubApi {

    @GET("/users/watabee/repos")
    suspend fun findRepositories(): List<Repository>
}