package com.example.filmsearcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class FilmAdapter(
    val films: MutableList<Film>
): RecyclerView.Adapter<FilmViewHolder>() {

    var clickListener: MovieClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_description, parent, false)
        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
        holder.itemView.setOnClickListener { clickListener?.onMovieClick(films[position]) }
    }

    fun interface MovieClickListener {
        fun onMovieClick(film: Film)
    }
}