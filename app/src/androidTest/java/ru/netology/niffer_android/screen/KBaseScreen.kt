package ru.netology.niffer_android.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import org.hamcrest.Matcher
import ru.netology.niffer_android.publisher.Publisher

@Suppress("UnnecessaryAbstractClass")
abstract class KBaseScreen<T : KBaseScreen<T>>(
  override val layoutId: Int?,
  override val viewClass: Class<*>?,
) : KScreen<T>() {

  private val selfClass = this::class.java

  protected inline fun <reified E : KBaseView<E>> bind(
    noinline builder: ViewBuilder.() -> Unit,
  ): E = registerElement(
      E::class.java.getDeclaredConstructor(Function1::class.java)
          .newInstance(builder)
  )

  protected inline fun <reified E : KBaseView<E>> bind(
    parent: Matcher<View>,
    noinline builder: ViewBuilder.() -> Unit,
  ): E = bind {
    builder()
    isDescendantOfA { withMatcher(parent) }
  }

  protected inline fun <reified E : KBaseView<E>> bind(id: Int): E = bind { withId(id) }

  protected fun <E> registerElement(element: E): E =
    element.apply { Publisher.instance.publish(selfClass) }
}
