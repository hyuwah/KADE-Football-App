package com.muhammadwahyudin.kadefootballapp.views.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.LoadingState
import com.muhammadwahyudin.kadefootballapp.data.model.NoResultState
import com.muhammadwahyudin.kadefootballapp.data.model.PopulatedState
import com.muhammadwahyudin.kadefootballapp.views._utils.HidingScrollListener
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule.MatchesScheduleAdapter
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.teamlist.TeamListAdapter
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import com.muhammadwahyudin.kadefootballapp.views.teamdetail.TeamDetailActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.intentFor
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private val viewmodel: SearchViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var searchView: SearchView
    private var searchQuery = ""
    private var searchFlags = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = "Search"
        rv_search.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_search.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
                card_search_toggle.animate().translationY(150f).interpolator = AccelerateInterpolator(2f)
            }

            override fun onShow() {
                card_search_toggle.animate().translationY(0f).interpolator = DecelerateInterpolator(2f)
            }
        })

//        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        search_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            if (searchQuery.isNotEmpty() && !searchView.query.isNullOrEmpty()) {
                when (position) {
                    0 -> {
                        if (position != searchFlags) {
                            viewmodel.searchMatch(searchQuery)
                            searchFlags = position
                        }
                    }
                    1 -> {
                        if (position != searchFlags) {
                            viewmodel.searchTeam(searchQuery)
                            searchFlags = position
                        }
                    }
                }
            }
        }
        observeMatchSearch()
        observeTeamSearch()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun observeMatchSearch() {
        viewmodel.matchState.observe(this, Observer { state ->
            when (state) {
                is LoadingState -> {
                    showView(progressBar = true)
                }
                is PopulatedState -> {
                    showView(recyclerView = true)
                    val adapter = MatchesScheduleAdapter(state.data) { event ->
                        startActivity(
                            intentFor<MatchDetailActivity>(
                                MatchDetailActivity.MATCH_ID to event.idEvent,
                                MatchDetailActivity.HOME_BADGE to event.strHomeTeamBadge,
                                MatchDetailActivity.AWAY_BADGE to event.strAwayTeamBadge
                            )
                        )
                    }
                    rv_search.adapter = adapter
                }
                is NoResultState -> {
                    showView(emptyView = true)
                }
            }
        })
    }

    private fun observeTeamSearch() {
        viewmodel.teamState.observe(this, Observer { state ->
            when (state) {
                is LoadingState -> showView(progressBar = true)
                is PopulatedState -> {
                    showView(recyclerView = true)
                    val adapter = TeamListAdapter(state.data) { team ->
                        startActivity(
                            intentFor<TeamDetailActivity>(
                                TeamDetailActivity.TEAM_PARCEL to team
                            )
                        )
                    }
                    rv_search.adapter = adapter
                }
                is NoResultState -> showView(emptyView = true)
            }
        })
    }

    private fun search(searchView: SearchView) {
        val disp = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.isNullOrEmpty()) searchQuery = ""
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
            .debounce(250, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .doOnNext { text ->
                if (text.isBlank() || text.length <= 3) {
                    tv_empty_search.text = getString(R.string.search_empty_view_instruction)
                    showView(emptyView = true)
                    return@doOnNext
                }

                searchQuery = text
                when (search_toggle.checkedTogglePosition) {
                    0 -> viewmodel.searchMatch(text)
                    1 -> viewmodel.searchTeam(text)
                }

            }
            .doOnError {
                tv_empty_search.text = getString(R.string.something_went_wrong) + it.message
                showView(emptyView = true)
            }
            .subscribe()
        compositeDisposable.add(disp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.match_search_menu, menu)
        searchView = menu?.findItem(R.id.match_search_menu)?.actionView as SearchView
        searchView.isIconified = false
        search(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.match_search_menu -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showView(progressBar: Boolean = false, emptyView: Boolean = false, recyclerView: Boolean = false) {
        if (progressBar) progressbar_search.visible() else progressbar_search.invisible()
        if (emptyView) tv_empty_search.visible() else tv_empty_search.invisible()
        if (recyclerView) rv_search.visible() else rv_search.invisible()
    }
}
