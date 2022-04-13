package com.sample.marketprices.view

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.sample.marketprices.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class HomeActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun init() {
        hiltRule.inject()
    }


    @Test
    fun mainActivityTest(){
        val searchText = "chittor"
        val searchMText = "chittoor"
        val activityScenario: ActivityScenario<HomeActivity> =
            ActivityScenario.launch(HomeActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.searchDistrict)).perform(click())
        onView(withId(R.id.searchDistrict)).perform(typeText(searchText))
        onView(withId(R.id.searchMarket)).perform(click())
        onView(withId(R.id.searchMarket)).perform(typeText(searchMText))
        onView(withId(R.id.buttonSearch)).perform(click())
        onView(withId(R.id.searchDistrict)).check(matches(withText(searchText)))
        onView(withId(R.id.searchMarket)).check(matches(withText(searchMText)))
    }

}