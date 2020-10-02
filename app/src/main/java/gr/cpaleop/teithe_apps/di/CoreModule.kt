package gr.cpaleop.teithe_apps.di

import android.content.Context
import androidx.room.Room
import gr.cpaleop.core.data.mappers.AnnouncementMapper
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.data.mappers.TokenMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.model.local.Migration
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.DateFormatterImpl
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapperImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    single<AnnouncementPresentationMapper> { AnnouncementPresentationMapperImpl(get()) }
    single { TokenMapper() }
    single { CategoryMapper() }
    single { AnnouncementMapper() }
    single<DateFormatter> { DateFormatterImpl() }
    single { provideAppDatabase(get()) }
    single { provideAnnouncementsApi(get()) }
    single { provideCategoriesApi(get()) }
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