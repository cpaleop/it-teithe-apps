package gr.cpaleop.download.di

import gr.cpaleop.core.data.DeviceStorageRepositoryImpl
import gr.cpaleop.core.domain.behavior.DownloadFolder
import gr.cpaleop.core.domain.repositories.DeviceStorageRepository
import gr.cpaleop.download.data.DownloadedFileMapper
import gr.cpaleop.download.data.FileRepositoryImpl
import gr.cpaleop.download.data.remote.DownloadApi
import gr.cpaleop.download.domain.DownloadAnnouncementNotifier
import gr.cpaleop.download.domain.DownloadProgressNotifier
import gr.cpaleop.download.domain.repositories.FileRepository
import gr.cpaleop.download.domain.usecases.DownloadFilesUseCase
import gr.cpaleop.download.domain.usecases.DownloadFilesUseCaseImpl
import gr.cpaleop.download.presentation.DownloadNotificationManager
import gr.cpaleop.download.presentation.DownloadNotificationManagerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@FlowPreview
val downloadModule = module {
    single<DownloadNotificationManager> { DownloadNotificationManagerImpl(get()) }
    single<DownloadFilesUseCase> { DownloadFilesUseCaseImpl(get(), get(), get(), get()) }
    single { DownloadedFileMapper() }
    single { DownloadProgressNotifier() }
    single { DownloadAnnouncementNotifier() }
    single<DeviceStorageRepository> {
        DeviceStorageRepositoryImpl(
            get(named<DownloadFolder>()),
            get(),
            get()
        )
    }
    single<FileRepository> { FileRepositoryImpl(get(), get()) }
    single { provideDownloadApi(get()) }
}

private fun provideDownloadApi(retrofit: Retrofit): DownloadApi {
    return retrofit.create(DownloadApi::class.java)
}