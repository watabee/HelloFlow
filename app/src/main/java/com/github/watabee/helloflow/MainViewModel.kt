package com.github.watabee.helloflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.watabee.helloflow.api.GithubApi
import com.github.watabee.helloflow.api.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val githubApi: GithubApi = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.github.com")
        .client(OkHttpClient.Builder().build())
        .build()
        .create(GithubApi::class.java)

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> = _repositories

    init {
        findRepositories()
    }

    private fun findRepositories() {
        viewModelScope.launch {
            try {
                flow { emit(githubApi.findRepositories()) }
                    .flowOn(Dispatchers.IO)
                    .single()
            } catch (e: Throwable) {
                Timber.e(e)
                null
            }?.apply(_repositories::setValue)
        }
    }
}