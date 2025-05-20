package ru.niffer_android.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.niffer_android.repository.category.CategoryRepository
import ru.niffer_android.repository.category.CategoryRepositoryImpl
import ru.niffer_android.repository.currency.CurrencyRepository
import ru.niffer_android.repository.currency.CurrencyRepositoryImpl
import ru.niffer_android.repository.friend.FriendRepository
import ru.niffer_android.repository.friend.FriendRepositoryImpl
import ru.niffer_android.repository.invitation.InvitationRepository
import ru.niffer_android.repository.invitation.InvitationRepositoryImpl
import ru.niffer_android.repository.session.SessionRepository
import ru.niffer_android.repository.session.SessionRepositoryImpl
import ru.niffer_android.repository.spending.SpendingRepository
import ru.niffer_android.repository.spending.SpendingRepositoryImpl
import ru.niffer_android.repository.statistics.StatisticsRepository
import ru.niffer_android.repository.statistics.StatisticsRepositoryImpl
import ru.niffer_android.repository.user.UserRepository
import ru.niffer_android.repository.user.UserRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        authRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticsRepository

    @Binds
    @Singleton
    abstract fun bindSpendingRepository(
        spendingRepositoryImpl: SpendingRepositoryImpl
    ): SpendingRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository

    @Binds
    @Singleton
    abstract fun bindInvitationRepository(
        invitationRepositoryImpl: InvitationRepositoryImpl
    ): InvitationRepository

    @Binds
    @Singleton
    abstract fun bindFriendRepository(
        friendRepositoryImpl: FriendRepositoryImpl
    ): FriendRepository
}