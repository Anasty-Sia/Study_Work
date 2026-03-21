package com.example.study_work

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search_btb)

        buttonSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val buttonLibrary = findViewById<Button>(R.id.library_btb)

        buttonLibrary.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        val buttonSettings = findViewById<Button>(R.id.settings_btb)

        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }


    }

}