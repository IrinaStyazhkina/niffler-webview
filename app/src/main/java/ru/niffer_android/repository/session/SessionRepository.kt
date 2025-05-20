package ru.niffer_android.repository.session

import ru.niffer_android.model.Session


interface SessionRepository {

    suspend fun getCurrentSession(): Session?
}