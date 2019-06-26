package com.muhammadwahyudin.kadefootballapp.views.leagues


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.EspressoIdlingResources
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class MatchSearchTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LeaguesActivity::class.java)

    private val okhttpResources = OkHttp3IdlingResource.create(
        "okhttp",
        TheSportDbApiService.client
    )
    private val espressoIdlingResources = EspressoIdlingResources.idlingResource

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(okhttpResources)
        IdlingRegistry.getInstance().register(espressoIdlingResources)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(okhttpResources)
        IdlingRegistry.getInstance().unregister(espressoIdlingResources)
    }


    @Test
    fun search_match() {
        val rvLeagues = onView(
            allOf(
                withId(R.id.rv_leagues),
                isDisplayed()
            )
        )
        rvLeagues.perform(RecyclerViewActions.actionOnItemAtPosition<LeagueAdapter.ViewHolder>(0, click()))

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.league_event_search_menu), withContentDescription("Search"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("milan"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.search_src_text), withText("milan"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(pressImeActionButton())

        // Assertion for search recyclerview
        onView(
            allOf(
                withId(R.id.rv_search),
                isDisplayed(),
                hasMinimumChildCount(1)
            )
        ) ?: error(println("Search Item Not Exist"))

    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
