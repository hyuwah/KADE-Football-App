package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule.MatchesScheduleAdapter
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_match_search.*
import org.jetbrains.anko.intentFor
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MatchSearchActivity : AppCompatActivity() {
    private val matchSearchViewModel: MatchSearchViewModel by viewModel()
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_search)
        title = "Search Match"
        rv_match_search.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        matchSearchViewModel.state.observe(this,
            Observer<ResourceState<List<EventWithImage>>> {
                when (it) {
                    is EmptyState -> {
                        showView(emptyView = true)
                        tv_empty_match_search.text = getString(R.string.search_empty_view_instruction)
                    }
                    is LoadingState -> {
                        showView(progressBar = true)
                    }
                    is PopulatedState -> {
                        showView(recyclerView = true)
                        val adapter = MatchesScheduleAdapter(it.data) { event ->
                            startActivity(
                                intentFor<MatchDetailActivity>(
                                    MatchDetailActivity.MATCH_ID to event.idEvent,
                                    MatchDetailActivity.HOME_BADGE to event.strHomeTeamBadge,
                                    MatchDetailActivity.AWAY_BADGE to event.strAwayTeamBadge
                                )
                            )
                        }
                        rv_match_search.adapter = adapter
                    }
                    is NoResultState -> {
                        showView(emptyView = true)
                        tv_empty_match_search.text = String.format("No result for \"%s\"", it.data)
                    }
                    is ErrorState -> {
                        showView(emptyView = true)
                        tv_empty_match_search.text = it.message
                    }
                }
            })
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
        searchMatch(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchMatch(searchView: SearchView) {
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
            .doOnNext { text ->
                if (text.isBlank() || text.length <= 3) {
                    tv_empty_match_search.text = getString(R.string.search_empty_view_instruction)
                    showView(emptyView = true)
                    return@doOnNext
                }
                matchSearchViewModel.searchMatch(text)
            }
            .doOnError {
                tv_empty_match_search.text = getString(R.string.something_went_wrong) + it.message
                showView(emptyView = true)
            }.subscribe()
    }

    private fun showView(progressBar: Boolean = false, emptyView: Boolean = false, recyclerView: Boolean = false) {
        if (progressBar) progressbar_match_search.visible() else progressbar_match_search.invisible()
        if (emptyView) tv_empty_match_search.visible() else tv_empty_match_search.invisible()
        if (recyclerView) rv_match_search.visible() else rv_match_search.invisible()
    }

}
