package com.example.filmsearcher

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class FilmViewHolder(itemView: View) : ViewHolder(itemView) {
    val filmPoster = itemView.findViewById<ImageView>(R.id.film_poster)
    val filmTitle = itemView.findViewById<TextView>(R.id.film_title)
    val filmDescription = itemView.findViewById<TextView>(R.id.film_description)

    fun bind(film: Film) {
        filmTitle.text = film.filmTitle
        filmDescription.text = film.filmDescription

        Glide.with(itemView)
            .load(film.filmPoster)
            .placeholder(R.drawable.not_found)
            .centerCrop()
            .into(filmPoster)
    }
}