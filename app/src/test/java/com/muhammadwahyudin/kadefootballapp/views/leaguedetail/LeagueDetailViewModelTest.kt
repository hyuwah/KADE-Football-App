package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jraska.livedata.test
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers

class LeagueDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LeagueDetailViewModel
    private val repository: IRepository = mock()

    @Before
    fun setUp() {
        viewModel = LeagueDetailViewModel(repository)
    }

    @Test
    fun getLeagueDetailSuccess() {
        // Given
        val leagueId = 4328
        println("League Id: $leagueId")
        whenever(repository.getLeagueDetail(ArgumentMatchers.anyInt()))
            .thenAnswer {
                MutableLiveData<LeagueDetailRes.League>(LeagueDetailRes.League(idLeague = "4328"))
            }
        // Test
        val ld = viewModel.getLeagueDetail(leagueId)
        // Assert
        verify(repository).getLeagueDetail(leagueId)
        ld.test()
            .assertHasValue()
            .assertValue {
                it.idLeague == leagueId.toString()
            }
        println("Expected: $leagueId\tActual: ${ld.value?.idLeague}")
    }

    @Test
    fun getLeagueDetailNoResult() {
        // Given
        val leagueId = 1234
        println("League Id: $leagueId")
        whenever(repository.getLeagueDetail(ArgumentMatchers.anyInt()))
            .thenAnswer {
                MutableLiveData<LeagueDetailRes.League>(null)
            }
        // Test
        val ld = viewModel.getLeagueDetail(leagueId)
        // Assert
        verify(repository).getLeagueDetail(leagueId)
        ld.test()
            .assertValue {
                it == null
            }
        println("Expected: null\tActual:${ld.value}")
    }
}