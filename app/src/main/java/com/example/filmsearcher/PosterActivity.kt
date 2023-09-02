package com.example.filmsearcher

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PosterActivity: AppCompatActivity() {
    lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poster)

        poster = findViewById(R.id.poster)

        val posterFilm = intent.extras?.getString(POSTER)

        Glide.with(this)
            .load(posterFilm)
            .into(poster)

    }
}