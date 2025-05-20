package ru.niffer_android.repository.currency

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Currency
import ru.niffer_android.model.Result

interface CurrencyRepository {
    suspend fun getCurrencies(): Flow<Result<List<Currency>>>
}