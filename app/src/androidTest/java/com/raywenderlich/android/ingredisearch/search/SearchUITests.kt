package com.raywenderlich.android.ingredisearch.search

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.intent.KIntent
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.raywenderlich.android.ingredisearch.R
import com.raywenderlich.android.ingredisearch.recentIngredients.RecentIngredientsActivity
import com.raywenderlich.android.ingredisearch.searchResults.SearchResultsActivity
import org.junit.Rule
import org.junit.Test

class SearchScreen : Screen<SearchScreen>() {
    val searchButton = KButton { withId(R.id.searchButton) }
    val snackbar = KView {
        withId(com.google.android.material.R.id.snackbar_text)
    }
    val ingredients = KEditText { withId(R.id.ingredients) }
    val recentButton = KView { withId(R.id.recentButton) }

}

// 1
@LargeTest
class SearchUITests {

    // 2
    @Rule
    @JvmField
    var rule = IntentsTestRule(SearchActivity::class.java)

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

    @Test
    fun search_withText_shouldNotShowSnackbarError() {
        screen {
            ingredients.typeText("eggs, ham, cheese")
            searchButton.click()
            snackbar.doesNotExist()
        }
    }

    @Test
    fun search_withText_shouldLaunchSearchResults() {
        screen {
            val query = "eggs, ham, cheese"
            // 1
            ingredients.typeText(query)
            searchButton.click()

            // 2
            val searchResultsIntent = KIntent {
                hasComponent(SearchResultsActivity::class.java.name)
                hasExtra("EXTRA_QUERY", query)
            }
            // 3
            searchResultsIntent.intended()
        }
    }

    @Test
    fun choosingRecentIngredients_shouldSetCommaSeparatedIngredients() {
        screen {
            // 1
            val recentIngredientsIntent = KIntent {
                hasComponent(RecentIngredientsActivity::class.java.name)
                withResult {
                    withCode(RESULT_OK)
                    withData(
                        Intent().putStringArrayListExtra(
                            RecentIngredientsActivity.EXTRA_INGREDIENTS_SELECTED,
                            ArrayList(listOf("eggz", "onionz"))
                        )
                    )
                }
            }
            // 2
            recentIngredientsIntent.intending()

            // 3
            recentButton.click()

            // 4
            ingredients.hasText("eggz,onionz")
        }
    }



}
