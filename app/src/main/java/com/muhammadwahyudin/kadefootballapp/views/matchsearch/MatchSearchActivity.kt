package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule.MatchesScheduleAdapter
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_match_search.*
import org.jetbrains.anko.intentFor
import java.util.concurrent.TimeUnit

class MatchSearchActivity : AppCompatActivity() {
    private lateinit var viewModel: MatchSearchViewModel
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_search)

        title = "Search Match"
        viewModel = ViewModelProviders.of(this).get(MatchSearchViewModel::class.java)
        rv_match_search.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        tv_empty_match_search.text = getString(R.string.search_empty_view_instruction)
        tv_empty_match_search.visible()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.match_search_menu, menu)

        val searchView = menu?.findItem(R.id.match_search_menu)?.actionView as SearchView
        searchView.isIconified = false

        disposable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

            })
        })
            .map { text -> text.toLowerCase().trim() }
            // Debounce mesti pake param Mainthread, kalo mau ada manipulasi view
            .debounce(250, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .distinct()
//            .filter { text -> text.isNotBlank() && text.length > 3 }
            .doOnNext { text ->
                if (text.isBlank() || text.length <= 3) {
                    tv_empty_match_search.text = getString(R.string.search_empty_view_instruction)
                    return@doOnNext
                }
                progressbar_match_search.visible()
                tv_empty_match_search.invisible()
                rv_match_search.invisible()
                viewModel.fetchEventsByQuery(text).observe(this,
                    Observer<List<EventWithImage>> { events ->
                        progressbar_match_search.invisible()
                        events?.let {
                            if (it.isNullOrEmpty()) {
                                tv_empty_match_search.text = "No result for \"$text\""
                                rv_match_search.invisible()
                                tv_empty_match_search.visible()
                            } else {
                                rv_match_search.visible()
                                val adapter = MatchesScheduleAdapter(it) { event ->
                                    startActivity(
                                        intentFor<MatchDetailActivity>(
                                            MatchDetailActivity.MATCH_PARCEL to event,
                                            MatchDetailActivity.HOME_BADGE to event.strHomeTeamBadge,
                                            MatchDetailActivity.AWAY_BADGE to event.strAwayTeamBadge
                                        )
                                    )
                                }
                                rv_match_search.adapter = adapter
                            }
                        }
                    })
            }
            .doOnError {
                tv_empty_match_search.text = getString(R.string.something_went_wrong) + it.message
                progressbar_match_search.invisible()
                tv_empty_match_search.visible()
                rv_match_search.invisible()
            }.subscribe()

        return super.onCreateOptionsMenu(menu)
    }


}
