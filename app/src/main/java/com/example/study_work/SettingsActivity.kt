package com.example.study_work


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }

        val mainBack = findViewById<MaterialToolbar>(R.id.settings_back)

        mainBack.setOnClickListener {
            finish()
        }

        val shareAppTextView = findViewById<MaterialTextView>(R.id.btn_share_app)

        shareAppTextView.setOnClickListener {
            val message  = getString(R.string.course_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,message)
            startActivity(shareIntent)
        }

        val writeSupportTextView = findViewById<MaterialTextView>(R.id.btn_write_support)

        writeSupportTextView.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val email = getString(R.string.support_email)
            val subject = getString(R.string.support_subject)
            val message = getString(R.string.support_message)
            /*supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT,subject)
            supportIntent.putExtra(Intent.EXTRA_TEXT,message)*/
            supportIntent.data = Uri.parse("mailto:$email?subject=${Uri.encode(subject)}&body=${Uri.encode(message)}")
            startActivity(supportIntent)
        }



       val userAgrementTextView = findViewById<MaterialTextView>(R.id.btn_user_agreement)

        userAgrementTextView.setOnClickListener {
            val offerIntent = Intent(Intent.ACTION_VIEW)
            val offer = getString(R.string.offer_link)
            offerIntent.data = Uri.parse(offer)
            startActivity(offerIntent)
        }

    }
}