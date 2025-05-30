package ru.netology.niffer_android.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.netology.niffer_android.screen.LoginUiScreen
import ru.netology.niffer_android.screen.MainScreenWebView
import ru.netology.niffer_android.screen.StartScreen
import ru.niffer_android.ui.auth.StartActivity


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : TestCase() {
    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<StartActivity?> = ActivityScenarioRule(
        StartActivity::class.java
    )
    @Test
    fun successLoginViaCustomTabsTest() = run {
        StartScreen {
            buttonLogin {
                isVisible()
                isClickable()
                click()
            }
        }

        LoginUiScreen {
            usernameInput {
                isDisplayed()
                replaceText("stage")
            }

            passwordInput {
                isDisplayed()
                replaceText("12345")
            }

            loginButton {
                isDisplayed()
                click()
            }
        }

        MainScreenWebView {
            flakySafely(timeoutMs = 30000, intervalMs = 500) {
                checkElementIsVisible("legend-container")
            }
        }
    }
}