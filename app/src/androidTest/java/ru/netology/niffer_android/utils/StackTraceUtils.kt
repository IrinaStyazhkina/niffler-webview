package ru.dodopizza.app.utils

object StackTraceUtils {

    fun parseVariableNameFromGetter(pageObjectClass: Class<*>): String {
        val stackTrace = Thread.currentThread().stackTrace
        val caller = stackTrace.firstOrNull { it.className == pageObjectClass.name }
        val methodName = caller?.methodName ?: "Unknown"

        if (!methodName.startsWith("get")) {
            throw IllegalStateException(
                "Parsing method '$methodName' is not a getter. Initialize variable via the getter"
            )
        }

        return methodName.removePrefix("get")
            .replaceFirstChar { it.lowercaseChar() }
    }
}
