package ru.netology.niffer_android.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton
import ru.niffer_android.R

object StartScreen : KScreen<StartScreen>() {

    override val layoutId: Int = R.layout.activity_start
    override val viewClass: Class<*>? = null

    val buttonLogin = KButton { withId(R.id.buttonLogin) }
}