package ru.niffer_android.repository.session


import ru.niffer_android.network.api.SessionApi
import ru.niffer_android.model.Session
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val sessionApi: SessionApi):
    SessionRepository {

    override suspend fun getCurrentSession(): Session? {
        val res = sessionApi.getCurrentSession()
        if (res.isSuccessful) {
            return res.body()
        }
        return null
    }
}