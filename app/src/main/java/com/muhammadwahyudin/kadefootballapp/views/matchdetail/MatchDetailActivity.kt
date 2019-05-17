package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val MATCH_PARCEL = "match_parcel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        val match = intent.getParcelableExtra<EventWithImage>(MATCH_PARCEL)

        title = match.strEvent
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
