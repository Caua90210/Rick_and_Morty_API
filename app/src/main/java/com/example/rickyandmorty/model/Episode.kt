package com.example.rickyandmorty.model

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>
)
