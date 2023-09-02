package com.example.filmsearcher

import com.google.gson.annotations.SerializedName

class FilmsResponse(@SerializedName("results") val foundFilms: List<Film>)