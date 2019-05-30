package com.raywenderlich.android.ingredisearch.searchResults

import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.intent.KIntent
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.raywenderlich.android.ingredisearch.R
import com.raywenderlich.android.ingredisearch.recipe.RecipeActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


@LargeTest
class SearchResultsUITests {

    @Rule
    @JvmField
    var rule:
        IntentsTestRule<SearchResultsActivity> =
        object : IntentsTestRule<SearchResultsActivity>
        (SearchResultsActivity::class.java) {

            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry
                    .getInstrumentation().targetContext
                val result = Intent(targetContext, SearchResultsActivity::class.java)
                result.putExtra("EXTRA_QUERY", "eggs, tomato")
                return result
            }
        }

    private val screen = SearchResultsScreen()

    @Test
    fun shouldRenderRecipesFromRepository() {
        screen {
            recycler {
                hasSize(10)
            }
        }
    }

    @Test
    fun shouldRenderTitleAndFavorite() {
        screen {
            recycler {
                for (i in 0..9) {
                    // 1
                    scrollTo(i)
                    childAt<Item>(i) {
                        // 2
                        title.hasText("Title " + (i + 1))
                        // 3
                        if (i != 1) {
                            favButton.hasDrawable(R.drawable.ic_favorite_border_24dp)
                        } else {
                            favButton.hasDrawable(R.drawable.ic_favorite_24dp)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun scrollToNotFavoriteAndMakeFavorite() {
        screen {
            recycler {
                    scrollTo(0)
                    childAt<Item>(0) {
                        favButton.click()
                        favButton.hasDrawable(R.drawable.ic_favorite_24dp)
                    }
                }
            }
        }

    @Test
    fun scrollToFavoriteAndMakeNotFavorite() {
        screen {
            recycler {
                scrollTo(1)
                childAt<Item>(1) {
                    favButton.click()
                    favButton.hasDrawable(R.drawable.ic_favorite_border_24dp)
                }
            }
        }
    }

    @Test
    fun shouldLaunchRecipeActivity() {
        screen {
            recycler {
                scrollTo(0)
                childAt<Item>(0) {
                    click()

                    val recipeIntent = KIntent {
                        hasComponent(RecipeActivity::class.java.name)
                        hasExtra("EXTRA_URL", "https://www.simplyrecipes.com/recipes/homemade_pizza")
                    }
                    recipeIntent.intended()
                }
            }
        }
    }


}

class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
    val title = KTextView(parent) { withId(R.id.title) }
    val favButton = KImageView(parent) { withId(R.id.favButton) }
}

class SearchResultsScreen : Screen<SearchResultsScreen>() {
    val recycler: KRecyclerView = KRecyclerView({
        withId(R.id.list)
    }, itemTypeBuilder = {
        itemType(::Item)
    })
}