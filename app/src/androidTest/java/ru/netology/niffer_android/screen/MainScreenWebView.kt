package ru.netology.niffer_android.screen

import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.assertion.WebViewAssertions.webContent
import androidx.test.espresso.web.matcher.DomMatchers.hasElementWithId
import androidx.test.espresso.web.sugar.Web.*
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.Locator
import com.kaspersky.kaspresso.screens.KScreen
import org.hamcrest.CoreMatchers.containsString


object MainScreenWebView : KScreen<MainScreenWebView>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    fun checkElementTextById(id: String, expectedText: String) {
        onWebView()
            .forceJavascriptEnabled()
            .withElement(findElement(Locator.ID, id))
            .check(WebViewAssertions.webMatches(getText(), containsString(expectedText)))
    }

    fun checkElementIsVisible(id: String) {
            onWebView()
                .forceJavascriptEnabled()
                .check(webContent(hasElementWithId(id)))
    }
}