package com.example.filmsearcher

import com.google.gson.annotations.SerializedName

data class Film(@SerializedName("image") val filmPoster: String,
                @SerializedName("title") val filmTitle: String,
                @SerializedName("description") val filmDescription: String)
