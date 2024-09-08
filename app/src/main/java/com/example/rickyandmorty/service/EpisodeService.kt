package com.example.rickyandmorty.service

import com.example.rickyandmorty.model.Episode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface EpisodeService {
    @GET
    fun getEpisodeByUrl(@Url url: String): Call<Episode>
}