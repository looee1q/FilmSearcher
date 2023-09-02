package com.example.filmsearcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val POSTER = "POSTER"

class MainActivity : AppCompatActivity() {

    lateinit var editTextFilmsSearcher: EditText
    lateinit var recyclerViewFilmsList: RecyclerView
    lateinit var errorMessage: TextView
    lateinit var progressBar: ProgressBar

//    private val filmsMock = listOf(Film("", "The Lord of the Rings: The Rings of Power", "2022- TV Series Morfydd Clark, Ismael Cruz Cordova"),
//        Film("", "The Lord of the Rings: The Fellowship of the Ring", "2001 Elijah Wood, Ian McKellen"),
//        Film("", "The Lord of the Rings: The Return of the King", "2003 Elijah Wood, Viggo Mortensen"),
//        Film("https://upload.wikimedia.org/wikipedia/en/f/fc/The_Lord_of_the_Rings%2C_T2T_%282002%29.jpg",
//            "The Lord of the Rings: The Two Towers", "2003 Elijah Wood, Ian McKellen, Liv Tyler"))

    private val apiKey = "k_zcuw1ytf"
    private val baseUrl = "https://imdb-api.com"

    private val films = mutableListOf<Film>()
    private val adapter = FilmAdapter(films)

    private var searchRequest: String = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val imdApi = retrofit.create(ImdApi::class.java)

    private val debouncer = Debouncer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextFilmsSearcher = findViewById<EditText>(R.id.edit_text_film_searcher)
        recyclerViewFilmsList = findViewById<RecyclerView>(R.id.recycler_view_films_list)
        errorMessage = findViewById<TextView>(R.id.error_message)
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        adapter.clickListener = FilmAdapter.MovieClickListener { film ->
            if (debouncer.clickDebounce()) {
                val intent = Intent(this@MainActivity, PosterActivity::class.java)
                intent.putExtra(POSTER, film.filmPoster)
                startActivity(intent)
            }
        }

        recyclerViewFilmsList.adapter = adapter
        recyclerViewFilmsList.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        debouncer.searchRunnable = Runnable {
            progressBar.visibility = View.VISIBLE

            imdApi.search(apiKey, searchRequest).enqueue(object : Callback<FilmsResponse> {
                override fun onResponse(call: Call<FilmsResponse>,
                                        response: Response<FilmsResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        if (response.body()?.foundFilms?.isNotEmpty() == true) {
                            adapter.films.clear()
                            adapter.films.addAll(response.body()?.foundFilms.orEmpty())
                            adapter.notifyDataSetChanged()
                            showMessage("", "Successful response!!!")
                            Log.d("THREAD", "${Thread.currentThread().name}")
                        } else {
                            showMessage(getString(R.string.nothing_found), "Пусто, капуста!")
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong), response.errorBody()?.toString() ?: "")
                    }
                }

                override fun onFailure(call: Call<FilmsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }
            })
        }

        editTextFilmsSearcher.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (input?.isNotBlank() == true) {
                    searchRequest = input.toString()
                }
                debouncer.searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    fun showMessage(message: String, extraInformation: String) {
        if (message.isNotEmpty()) {
            films.clear()
            adapter.notifyDataSetChanged()
            errorMessage.text = message
            errorMessage.visibility = View.VISIBLE

            if(extraInformation.isNotEmpty()) {
                Toast.makeText(this, extraInformation, Toast.LENGTH_SHORT).show()
            }
        }
        else {
            errorMessage.visibility = View.GONE
        }
    }
}