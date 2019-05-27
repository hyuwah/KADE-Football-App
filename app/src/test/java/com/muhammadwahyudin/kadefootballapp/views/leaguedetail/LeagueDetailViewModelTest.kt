package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LeagueDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: LeagueDetailViewModel

    @Mock
    lateinit var observer: Observer<LeagueDetailRes.League>
    @Mock
    lateinit var repository: IRepository

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel = LeagueDetailViewModel(repository)
    }

    @Test
    fun getLeagueDetailSuccess() {
        val leagueId = 4328
        println("League Id: $leagueId")
        Mockito.`when`(viewModel.getLeagueDetail(ArgumentMatchers.anyInt())).thenAnswer {
            return@thenAnswer MutableLiveData<LeagueDetailRes.League>(LeagueDetailRes.League(idLeague = "4328"))
        }
        val ld = viewModel.getLeagueDetail(leagueId)
        ld.observeForever { observer }
        Mockito.verify(repository).getLeagueDetail(leagueId)
        assertNotNull(ld.value)
        assertEquals(leagueId.toString(), ld.value?.idLeague)
        println("Expected: $leagueId\tActual: ${ld.value?.idLeague}")
    }

    @Test
    fun getLeagueDetailFailed() {
        val leagueId = 1234
        println("League Id: $leagueId")
        Mockito.`when`(viewModel.getLeagueDetail(ArgumentMatchers.anyInt())).thenAnswer {
            return@thenAnswer MutableLiveData<LeagueDetailRes.League>(null)
        }
        val ld = viewModel.getLeagueDetail(leagueId)
        ld.observeForever { observer }
        Mockito.verify(repository).getLeagueDetail(leagueId)
        assertNull(ld.value)
        println("Expected: null\tActual:${ld.value}")
    }
}