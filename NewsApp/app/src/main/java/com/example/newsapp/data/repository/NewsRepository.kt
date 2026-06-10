package com.example.newsapp.data.repository

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.api.RetrofitClient

class NewsRepository {
    suspend fun getNews() =
        RetrofitClient.apiService.getTopHeadlines(
            apiKey = BuildConfig.API_KEY
        )
}
