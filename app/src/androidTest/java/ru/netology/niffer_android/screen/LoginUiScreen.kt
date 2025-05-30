package ru.netology.niffer_android.screen

import com.kaspersky.components.kautomator.component.edit.UiEditText
import com.kaspersky.components.kautomator.component.text.UiButton
import com.kaspersky.components.kautomator.screen.UiScreen

object LoginUiScreen : UiScreen<LoginUiScreen>() {

    override val packageName: String = "com.android.chrome"

    val usernameInput = UiEditText {
        withResourceName("username")
    }

    val passwordInput = UiEditText{
        withResourceName("password")
    }

    val loginButton = UiButton {
        withResourceName("login-button")
    }
}