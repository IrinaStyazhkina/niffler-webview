package ru.niffer_android.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.niffer_android.model.Result
import ru.niffer_android.model.Statistics
import ru.niffer_android.repository.statistics.StatisticsRepository
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private val _statistics = MutableStateFlow<Result<Statistics>>(Result.Loading)
    val statistics: StateFlow<Result<Statistics>> = _statistics.asStateFlow()

    fun loadStatistics() {
        viewModelScope.launch {
            statisticsRepository.getStatistics()
               .collect { stats ->
                    _statistics.value = stats
                }
        }
    }


}