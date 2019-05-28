package com.raywenderlich.android.ingredisearch.search

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.raywenderlich.android.ingredisearch.R
import org.junit.Rule
import org.junit.Test

class SearchScreen : Screen<SearchScreen>() {
    val searchButton = KButton { withId(R.id.searchButton) }
    val snackbar = KView {
        withId(com.google.android.material.R.id.snackbar_text)
    }
}

// 1
@LargeTest
class SearchUITests {

    // 2
    @Rule
    @JvmField
    var rule = ActivityTestRule(SearchActivity::class.java)

    // 3
    private val screen = SearchScreen()

    // 4
    @Test
    fun search_withEmptyText_shouldShowSnackbarError() {
        // 5
        screen {
            searchButton.click()
            snackbar.isDisplayed()
        }
    }
}
