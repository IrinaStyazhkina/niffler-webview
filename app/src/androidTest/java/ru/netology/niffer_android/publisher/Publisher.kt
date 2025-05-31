package ru.netology.niffer_android.publisher

import ru.dodopizza.app.utils.StackTraceUtils.parseVariableNameFromGetter

interface Publisher {

    fun publish(
        pageObjectClass: Class<*>,
        element: String,
    )

    fun publish(pageObjectClass: Class<*>) {
        val variableName = parseVariableNameFromGetter(pageObjectClass)
        publish(pageObjectClass, variableName)
    }

    companion object {
        val instance: Publisher = InMemPublisher
    }
}