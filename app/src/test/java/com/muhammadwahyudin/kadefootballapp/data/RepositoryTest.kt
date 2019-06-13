package com.muhammadwahyudin.kadefootballapp.data

import android.content.res.Resources
import android.content.res.TypedArray
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var tsdbApi: TheSportDbApiService
    private lateinit var repo: IRepository
    private val resources: Resources = mock(Resources::class.java)

    @Before
    fun setUp() {
        tsdbApi = TheSportDbApiService.create()
        repo = Repository(tsdbApi)
    }

    @Test
    fun getLeagues() {
        val leagueNamesMock = arrayOf("English Premier League", "French Ligue 1", "English League 1")
        val leagueIdsMock = intArrayOf(4328, 4334, 4396)
        val leagueDrawablesMock = mock(TypedArray::class.java)
        val leagueDescsMock = arrayOf(
            "Premier league desc",
            "French ligue desc",
            "English league desc"
        )
        `when`(resources.getStringArray(R.array.league_name)).thenReturn(leagueNamesMock)
        `when`(resources.getIntArray(R.array.league_id)).thenReturn(leagueIdsMock)
        `when`(resources.obtainTypedArray(R.array.league_image)).thenReturn(leagueDrawablesMock)
        `when`(resources.getStringArray(R.array.league_desc)).thenReturn(leagueDescsMock)
        val leagues = repo.getLeagues(resources)
        print(leagues[0])
        assertTrue(leagues.isNotEmpty())
    }

    @Test
    fun getLeagueDetail() {
    }

    @Test
    fun getNextMatchByLeagueId() {
    }

    @Test
    fun getLastMatchByLeagueId() {
    }

    @Test
    fun searchMatches() {
    }

    @Test
    fun getMatchDetail() {
    }

    @Test
    fun getTeamDetail() {
    }

    @Test
    fun getFavoriteEvents() {
    }

    @Test
    fun updateEventWithTeamBadge() {
    }
}