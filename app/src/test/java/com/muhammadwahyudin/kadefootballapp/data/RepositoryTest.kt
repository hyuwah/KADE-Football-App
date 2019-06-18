package com.muhammadwahyudin.kadefootballapp.data

import android.content.res.Resources
import android.content.res.TypedArray
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.schedulers.Schedulers
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

    /// NOTES, real network test might suddenly fail because of downtime, key throttling, etc
    /// on the other hand, creating Mock of all responses is quite tedious, so it'll be added / used later
    private lateinit var tsdbApiMock: TheSportDbApiService
    private lateinit var tsdbApiReal: TheSportDbApiService
    private lateinit var repoWithMockedNetwork: IRepository
    private lateinit var repoWithRealNetwork: IRepository
    private val resources: Resources = mock(Resources::class.java)

    @Before
    fun setUp() {
        tsdbApiMock = mock() // if we want to mock api response (we can manipulate the response)
        tsdbApiReal = TheSportDbApiService.create() // if we wish to fetch real data (need internet connetion)
        repoWithMockedNetwork = Repository(Schedulers.trampoline(), Schedulers.trampoline(), tsdbApiMock)
        repoWithRealNetwork = Repository(Schedulers.trampoline(), Schedulers.trampoline(), tsdbApiReal)
    }

    /**
     *
     */
    @Test
    fun get_leagues() {
        // Given
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

        // Test
        val leagues = repoWithMockedNetwork.getLeagues(resources)

        // Assert
        print(leagues[0])
        assertTrue(leagues.isNotEmpty())
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_league_detail_from_network_success() {
        // Given
        val leagueId = 4328
        println("League ID: $leagueId")

        // Test
        val leagueDetail = repoWithRealNetwork.getLeagueDetail(leagueId)

        // Assert
        println("performing getLeagueDetail($leagueId)")
        leagueDetail.test()
            .assertHasValue()
            .assertValue {
                val idLeagueIsValid = it.idLeague == "4328"
                val strLeagueIsValid = it.strLeague == "English Premier League"
                println("Assertion: ${it.idLeague} == 4328; $idLeagueIsValid")
                println("Assertion: ${it.strLeague} == English Premier League; $strLeagueIsValid")
                idLeagueIsValid && strLeagueIsValid
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_league_detail_from_network_failed() {
        // Given
        val leagueId = 123
        println("League ID: $leagueId")
        // Test
        val leagueDetail = repoWithRealNetwork.getLeagueDetail(leagueId)
        // Assert
        leagueDetail.test()
            .assertValue {
                it.idLeague.isEmpty() && it.strLeague.isEmpty()
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_next_match_by_league_id_from_network_success() {
        // Given
        val leagueId = "4328"
        println("League ID: $leagueId")
        // Test
        val nextMatch = repoWithRealNetwork.getNextMatchByLeagueId(leagueId)
        // Assert
        nextMatch.test()
            .assertHasValue()
            .assertValue {
                println("Next Match Count: ${it.size}")
                it.isNotEmpty()
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_next_match_by_league_id_from_network_empty() {
        // Given
        val leagueId = "123"
        println("League ID: $leagueId")
        // Test
        val nextMatch = repoWithRealNetwork.getNextMatchByLeagueId(leagueId)
        // Assert
        nextMatch.test()
            .assertValue {
                println("Next Match Count: ${it.size}")
                it.isEmpty()
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_last_match_by_league_id_from_network_success() {
        // Given
        val leagueId = "4328"
        println("League ID: $leagueId")
        // Test
        val lastMatch = repoWithRealNetwork.getLastMatchByLeagueId(leagueId)
        // Assert
        lastMatch.test()
            .assertHasValue()
            .assertValue {
                println("Last Match Count: ${it.size}")
                it.isNotEmpty()
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_last_match_by_league_id_from_network_empty() {
        // Given
        val leagueId = "123"
        println("League ID: $leagueId")
        // Test
        val lastMatch = repoWithRealNetwork.getLastMatchByLeagueId(leagueId)
        // Assert
        lastMatch.test()
            .assertValue {
                println("Last Match Count: ${it.size}")
                it.isEmpty()
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun search_matches_from_network_exist() {
        // Given
        val query = "milan".toLowerCase()
        // Test
        val matchesRes = repoWithRealNetwork.searchMatches(query)
        val testObserver = matchesRes.test()
        // Assert
        testObserver.assertValue {
            //println(it)
            val isNotEmpty = it.events.isNotEmpty()
            val isEventExist = it.events[0].strEvent.toLowerCase().contains(query)
            isNotEmpty && isEventExist
        }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun search_matches_from_network_not_exist() {
        // Given
        val query = "persib".toLowerCase()
        // Test
        val matchesRes = repoWithRealNetwork.searchMatches(query)
        val testObserver = matchesRes.test()
        // Assert
        testObserver.assertValue {
            //println(it)
            val isEmpty = it.events.isNullOrEmpty()
            isEmpty
        }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_match_detail_from_network_success() {
        // Given
        val eventId = "441613"
        // Test
        val matchDetail = repoWithRealNetwork.getMatchDetail(eventId)
        // Assert
        matchDetail.test()
            .assertHasValue()
            .assertValue {
                //println(it)
                val isEventValid = it.idEvent == eventId
                val isEventNameValid = it.strEvent == "Liverpool vs Swansea"
                isEventValid && isEventNameValid
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_match_detail_from_network_failed() {
        // Given
        val eventId = "123"
        // Test
        val matchDetail = repoWithRealNetwork.getMatchDetail(eventId)
        // Assert
        matchDetail.test()
            .assertNoValue()
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_team_detail_from_network_success() {
        // Given
        val teamId = "133604"
        // Test
        val teamDetail = repoWithRealNetwork.getTeamDetail(teamId)
        // Assert
        teamDetail.test()
            .assertValue {
                //println(it)
                val isTeamValid = it.idTeam == teamId
                val isTeamNameValid = it.strTeam == "Arsenal"
                isTeamValid && isTeamNameValid
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun get_team_detail_from_network_failed() {
        // Given
        val teamId = "123"
        // Test
        val teamDetail = repoWithRealNetwork.getTeamDetail(teamId)
        // Assert
        teamDetail.test()
            .assertNoValue()
    }

    /**
     * Should be instrumentation testing!
     */
    @Test
    fun get_favorite_events_from_db() {
        // EMPTY TEST
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun update_events_with_team_badge_from_network_both_success() {
        // Given
        val homeTeamId = "133603"
        val awayTeamId = "133604"
        val event = EventWithImage(
            "", "", awayTeamId, "", homeTeamId, "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", ""
        )
        // Test
        val test = repoWithRealNetwork.updateEventWithTeamBadge(event)
        // Assert
        test.test()
            .assertValue {
                print(it)
                val isAwayHasTeamBadge = it[0].contains(".png")
                val isHomeHasTeamBadge = it[1].contains(".png")
                isAwayHasTeamBadge && isHomeHasTeamBadge
            }
    }

    /**
     * Using real api service
     * might fail the test if the data from network changes, or site is down
     */
    @Test
    fun update_events_with_team_badge_from_network_both_failed() {
        // Given
        val homeTeamId = "1336"
        val awayTeamId = "13360"
        val event = EventWithImage(
            "", "", awayTeamId, "", homeTeamId, "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "",
            "", ""
        )
        // Test
        val test = repoWithRealNetwork.updateEventWithTeamBadge(event)
        // Assert
        test.test()
            .assertNoValues()
    }
}