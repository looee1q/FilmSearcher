package com.example.filmsearcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var searchButton: Button
    lateinit var editTextFilmsSearcher: EditText
    lateinit var recyclerViewFilmsList: RecyclerView

//    private val filmsMock = listOf(Film("", "The Lord of the Rings: The Rings of Power", "2022- TV Series Morfydd Clark, Ismael Cruz Cordova"),
//        Film("", "The Lord of the Rings: The Fellowship of the Ring", "2001 Elijah Wood, Ian McKellen"),
//        Film("", "The Lord of the Rings: The Return of the King", "2003 Elijah Wood, Viggo Mortensen"),
//        Film("https://upload.wikimedia.org/wikipedia/en/f/fc/The_Lord_of_the_Rings%2C_T2T_%282002%29.jpg",
//            "The Lord of the Rings: The Two Towers", "2003 Elijah Wood, Ian McKellen, Liv Tyler"))

    private val apiKey = "k_j0oge517"
    private val baseUrl = "https://imdb-api.com"

    private val films = mutableListOf<Film>()
    private val adapter = FilmAdapter(films)

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val imdApi = retrofit.create(ImdApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById<Button>(R.id.search_button)
        editTextFilmsSearcher = findViewById<EditText>(R.id.edit_text_film_searcher)
        recyclerViewFilmsList = findViewById(R.id.recycler_view_films_list)

        recyclerViewFilmsList.adapter = adapter
        recyclerViewFilmsList.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        searchButton.setOnClickListener {

            imdApi.search(apiKey, editTextFilmsSearcher.text.toString()).enqueue(object : Callback<FilmsResponse> {
                override fun onResponse(call: Call<FilmsResponse>,
                                        response: Response<FilmsResponse>) {
                    if (response.isSuccessful) {
                        if(response.body()?.foundFilms?.isNotEmpty() == true) {
                            adapter.films.clear()
                            adapter.films.addAll(response.body()?.foundFilms.orEmpty())
                            adapter.notifyDataSetChanged()

                            Toast.makeText(
                                this@MainActivity,
                                "Successful!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Пусто, капуста ${editTextFilmsSearcher.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.errorBody()?.toString() + "\nNotSuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<FilmsResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }





    }
}