package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MatchSearchViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // Instant executor for livedata

    private lateinit var viewModel: MatchSearchViewModel
    private val repository: IRepository = mock()
    private lateinit var searchTestObserver: TestObserver<SearchEventsRes>
    private val stateObserver: Observer<ResourceState<List<EventWithImage>>> = mock()

    @Before
    fun setUp() {
        viewModel = MatchSearchViewModel(repository).apply {
            state.observeForever(stateObserver)
        }
        searchTestObserver = TestObserver.create()
    }

    @Test
    fun searchMatchNoResult() {
        val query = "milann"
        doAnswer {
            stateObserver.onChanged(NoResultState(query))
            return@doAnswer Single.just(SearchEventsRes(emptyList()))
        }.whenever(repository).searchMatches(query)
        verify(stateObserver).onChanged(EmptyState())
        viewModel.searchMatch(query) // Invoke
        verify(repository).searchMatches(query)
        verify(stateObserver).onChanged(LoadingState())
        verify(stateObserver).onChanged(NoResultState(query))
    }

    @Test
    fun searchMatchSuccess() {
        val query = "milan"
        val searchEventsRes: SearchEventsRes = mock()
        val listOfEvents: List<EventWithImage> = mock()
        doAnswer {
            stateObserver.onChanged(PopulatedState(listOfEvents))
            Single.just(searchEventsRes)
        }.whenever(repository).searchMatches(query)
        verify(stateObserver).onChanged(EmptyState())
        viewModel.searchMatch(query) // Invoke
        verify(repository).searchMatches(query)
        verify(stateObserver).onChanged(LoadingState())
        verify(stateObserver).onChanged(PopulatedState(listOfEvents))
    }
}