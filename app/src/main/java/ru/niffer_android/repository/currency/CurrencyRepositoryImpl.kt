package ru.niffer_android.repository.currency

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Currency
import ru.niffer_android.model.Result
import ru.niffer_android.network.api.CurrencyApi
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val currencyApi: CurrencyApi): CurrencyRepository {
    override suspend fun getCurrencies(): Flow<Result<List<Currency>>> =
        flowWithResult {
            currencyApi.getCurrencies()
        }
}