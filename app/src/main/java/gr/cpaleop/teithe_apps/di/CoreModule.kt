package gr.cpaleop.teithe_apps.di

import android.content.Context
import androidx.room.Room
import gr.cpaleop.core.connection.Connection
import gr.cpaleop.core.connection.InternetConnection
import gr.cpaleop.core.connection.MobileConnection
import gr.cpaleop.core.connection.WifiConnection
import gr.cpaleop.core.connection.types.Internet
import gr.cpaleop.core.connection.types.Mobile
import gr.cpaleop.core.connection.types.Wifi
import gr.cpaleop.core.data.interceptors.ConnectionInterceptor
import gr.cpaleop.core.data.interceptors.RefreshTokenInterceptor
import gr.cpaleop.core.data.interceptors.TokenInterceptor
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.data.mappers.TokenMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.model.local.Migration
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.DateFormatterImpl
import gr.cpaleop.core.domain.repositories.AuthenticationRepository
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapperImpl
import gr.cpaleop.teithe_apps.data.PreferencesRepositoryImpl
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    single<AnnouncementPresentationMapper> { AnnouncementPresentationMapperImpl(get()) }
    single { TokenMapper() }
    single { CategoryMapper() }
    single { AnnouncementMapper() }
    single<DateFormatter> { DateFormatterImpl() }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single { provideAppDatabase(get()) }
    single { provideAnnouncementsApi(get()) }
    single { provideCategoriesApi(get()) }
    single { provideRefreshTokenInterceptor(get(), get()) }
    single { provideTokenInterceptor(get()) }
    single { provideConnectionInterceptor(get(named<Internet>())) }
    single { provideHttpLoggingInterceptor() }
    single<Connection>(named<Wifi>()) { WifiConnection(get()) }
    single<Connection>(named<Mobile>()) { MobileConnection(get()) }
    single<Connection>(named<Internet>()) {
        InternetConnection(
            get(named<Wifi>()),
            get(named<Mobile>())
        )
    }
}

private fun provideAppDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "it-teithe-apps-db"
    )
        .addMigrations(Migration())
        .build()
}

private fun provideAnnouncementsApi(retrofit: Retrofit): AnnouncementsApi {
    return retrofit.create(AnnouncementsApi::class.java)
}

private fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi {
    return retrofit.create(CategoriesApi::class.java)
}

private fun provideRefreshTokenInterceptor(
    preferencesRepository: PreferencesRepository,
    authenticationRepository: AuthenticationRepository
): RefreshTokenInterceptor {
    return RefreshTokenInterceptor(preferencesRepository, authenticationRepository)
}

private fun provideTokenInterceptor(preferencesRepository: PreferencesRepository): TokenInterceptor {
    return TokenInterceptor(preferencesRepository)
}

private fun provideConnectionInterceptor(@Internet connection: Connection): ConnectionInterceptor {
    return ConnectionInterceptor(connection)
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}