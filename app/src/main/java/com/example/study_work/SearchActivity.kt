package com.example.study_work

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private var searchText: String = ""
    private lateinit var inputEditText: EditText
    private lateinit var errorView: View
    private lateinit var emptyView: View

    private lateinit var refreshButton: View
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView

    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.tracksList)

        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        emptyView = findViewById(R.id.emptyView)
        errorView = findViewById(R.id.errorView)
        refreshButton = findViewById(R.id.refresh_button)

        val mainBack = findViewById<MaterialToolbar>(R.id.search_back)
        mainBack.setOnClickListener {
            finish()
        }

        setupViews()

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(inputEditText)
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                searchText = input

                clearButton.visibility = clearButtonVisibility(s)
                resetSearchState()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchText)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(searchText)
                hideKeyboard(inputEditText)
                true
            }
            false
        }

    }

    private fun setupViews() {
        trackAdapter = TrackAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        refreshButton.setOnClickListener {
            println("Нажата кнопка обновить, текст: ${inputEditText.text}")
            if (searchText.isNotEmpty()) {
                refreshButton.isEnabled = false
                performSearch(searchText)
                refreshButton.postDelayed({ refreshButton.isEnabled = true }, 1000)
            } else {
                inputEditText.requestFocus()
            }
        }
    }


    private fun performSearch(searchText: String) {
        if (searchText.isBlank()) {
            resetSearchState()
            return
        }

        search()
    }


    private fun showResult(tracks: List<Track>) {
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        trackAdapter.updateTracks(tracks)

    }


    private fun showEmptyState(messageRes: Int, iconRes: Int) {
        emptyView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        recyclerView.visibility = View.GONE

        emptyView.findViewById<TextView>(R.id.emptyResultsText).text = getString(messageRes)
        emptyView.findViewById<ImageView>(R.id.emptyResultsIcon).setImageResource(iconRes)
    }

    private fun showErrorState(messageRes: Int, iconRes: Int, show: Boolean) {
        if (show) {
            emptyView.visibility = View.GONE
            errorView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            errorView.findViewById<TextView>(R.id.errorResultsText).text = getString(messageRes)
            errorView.findViewById<ImageView>(R.id.errorResultsIcon).setImageResource(iconRes)

        } else {
            errorView.visibility = View.GONE
        }

    }

    private fun resetSearchState() {
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        trackAdapter.updateTracks(emptyList())
    }


    private fun search() {
        println("search() вызван")
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == 200) {
                            println("Код ответа: ${response.code()}")
                            println("Количество треков: ${response.body()?.results?.size}")
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showEmptyState(
                                    R.string.nothing_found,
                                    R.drawable.ic_nothing_found_120
                                )

                            } else {
                                showResult(tracks)
                            }
                        } else {
                            showErrorState(
                                R.string.something_went_wrong,
                                R.drawable.ic_internet_120,
                                true
                            )
                        }
                        refreshButton.isEnabled = true
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showErrorState(
                            R.string.something_went_wrong,
                            R.drawable.ic_internet_120,
                            true
                        )
                        refreshButton.isEnabled = true
                    }

                })
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
        inputEditText.setText(searchText)
    }

    private fun hideKeyboard(v: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)

    }

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
    }
}