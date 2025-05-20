package ru.niffer_android.storage

object TokenStorage {
    private var _accessToken: String? = null
    private var _refreshToken: String? = null
    private var _idToken: String? = null

    var accessToken: String?
        get() = _accessToken
        set(value) {
            _accessToken = value
        }

    var refreshToken: String?
        get() = _refreshToken
        set(value) {
            _refreshToken = value
        }

    var idToken: String?
        get() = _idToken
        set(value) {
            _idToken = value
        }

    fun clearTokenStorage() {
        _accessToken = null
        _refreshToken = null
        _idToken = null
    }

    fun hasValidAccessToken(): Boolean = !_idToken.isNullOrBlank()
}