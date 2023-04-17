package io.finbridge.vepay.moneytransfersdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.finbridge.vepay.moneytransfersdk.api.MoneyTransferClient
import io.finbridge.vepay.moneytransfersdk.data.repository.MoneyTransferRepository
import io.finbridge.vepay.moneytransfersdk.data.repository.MoneyTransferRepositoryImpl
import io.ktor.client.HttpClient

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun getHttpClient(httpClient: MoneyTransferClient): HttpClient = httpClient.getHttpClient()

    @Provides
    fun getMoneyTransferRepository(moneyTransferRepositoryImpl: MoneyTransferRepositoryImpl): MoneyTransferRepository =
        moneyTransferRepositoryImpl
}
