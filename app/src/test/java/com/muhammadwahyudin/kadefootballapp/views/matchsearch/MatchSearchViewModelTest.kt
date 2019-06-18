package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MatchSearchViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // Instant executor for livedata

    private lateinit var viewModel: MatchSearchViewModel
    private val repository: IRepository = mock()
    private val stateObserver: Observer<ResourceState<List<EventWithImage>>> = mock()

    @Before
    fun setUp() {
        viewModel = MatchSearchViewModel(repository).apply {
            state.observeForever(stateObserver)
        }
    }

    @Test
    fun searchMatchNoResult() {
        // Given
        val query = "milann"
        whenever(repository.searchMatches(query))
            .thenAnswer {
                stateObserver.onChanged(NoResultState(query))
                Single.just(SearchEventsRes(emptyList()))
            }
        verify(stateObserver).onChanged(EmptyState()) // Assert nyelip
        // Test
        viewModel.searchMatch(query) // Invoke
        // Assert
        verify(repository).searchMatches(query)
        verify(stateObserver).onChanged(LoadingState())
        verify(stateObserver).onChanged(NoResultState(query))
    }

    @Test
    fun searchMatchHasResult() {
        // Given
        val query = "milan"
        val searchEventsRes: SearchEventsRes = mock()
        val listOfEvents: List<EventWithImage> = mock()
        whenever(repository.searchMatches(query))
            .thenAnswer {
                stateObserver.onChanged(PopulatedState(listOfEvents))
                Single.just(searchEventsRes)
            }
        verify(stateObserver).onChanged(EmptyState()) // Assert nyelip
        // Test
        viewModel.searchMatch(query) // Invoke
        // Assert
        verify(repository).searchMatches(query)
        verify(stateObserver).onChanged(LoadingState())
        verify(stateObserver).onChanged(PopulatedState(listOfEvents))
    }
}