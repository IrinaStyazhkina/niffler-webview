package ru.niffer_android.utils

import androidx.fragment.app.Fragment
import ru.niffer_android.activity.MainActivity

fun Fragment.showLoader() {
    (activity as? MainActivity)?.showLoader()
}

fun Fragment.hideLoader() {
    (activity as? MainActivity)?.hideLoader()
}

fun Fragment.showError(message: String) {
    (activity as? MainActivity)?.showError(message)
}

fun Fragment.showSuccess(message: String) {
    (activity as? MainActivity)?.showSuccess(message)
}
