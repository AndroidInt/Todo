package com.androidint.todo.screen.addtask

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.androidint.todo.repository.model.Category
import com.androidint.todo.utils.DataStore
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters


@RunWith(Parameterized::class)
class AddTaskElementKtTest(private val category: Category) {


    @get:Rule
    val composeTestRule = createComposeRule()


    private lateinit var fullCategories: MutableList<Category>
    private lateinit var nullCategories: MutableList<Category>
    private lateinit var categories: MutableList<Category>

    @Before
    fun setup() {
        fullCategories = mutableListOf()
        nullCategories = mutableListOf<Category>()
        categories = mutableListOf()
        repeat(6) {
            fullCategories.add(Category("cat ${it + 1}", it, it + 1))
        }
        repeat(2) {
            categories.add(Category("cat ${it + 1}", it, it + 1))
        }

    }


    companion object {
        @JvmStatic
        @Parameters
        fun data(): Collection<Array<Any>> {
            val categories = mutableListOf<Category>()
            repeat(6) {
                categories.add(Category("cat ${it + 1}", it, it + 1))
            }

            return categories.map { arrayOf(it) }
        }
    }


    @Test
    fun colorPickerFullCategoriesTest() {
        var selectedCat: Category? = null
        composeTestRule.setContent {
            ColorPicker(categories = fullCategories, onBack = { }, onSetCategory = {
                selectedCat = it
            })
        }


            composeTestRule
                .onNode(hasContentDescription("choose ${DataStore.categoryToColor(category.color)}"))
                .performClick()
            composeTestRule.onNodeWithText("Next").performClick()
            assert(selectedCat?.color == category.color)



    }




    @Test
    fun colorPickerNullCategoriesTest() {
        var selectedCat: Category? = null
        composeTestRule.setContent {
            ColorPicker(categories = nullCategories, onBack = { }, onSetCategory = {
                selectedCat = it
            })
        }

        composeTestRule.onNode(hasContentDescription("choose ${DataStore.categoryToColor(
            category.color
        )}"))
            .performClick()
        composeTestRule.onNode(hasContentDescription("input category name"))
            .performTextInput(category.name)
        composeTestRule.onNodeWithText("Next").performClick()
        assert(selectedCat?.color == category.color)
        assert(selectedCat?.name == category.name)
    }


}