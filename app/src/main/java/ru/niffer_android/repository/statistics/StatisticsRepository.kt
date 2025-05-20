package ru.niffer_android.repository.statistics

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Result
import ru.niffer_android.model.Statistics

interface StatisticsRepository {

    suspend fun getStatistics(): Flow<Result<Statistics>>

}