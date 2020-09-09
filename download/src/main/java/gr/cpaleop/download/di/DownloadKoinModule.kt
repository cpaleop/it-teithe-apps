package gr.cpaleop.download.di

import android.content.Context
import android.os.Environment
import gr.cpaleop.download.data.DeviceStorageRepositoryImpl
import gr.cpaleop.download.data.DownloadedFileMapper
import gr.cpaleop.download.data.FileRepositoryImpl
import gr.cpaleop.download.data.remote.DownloadApi
import gr.cpaleop.download.domain.repositories.DeviceStorageRepository
import gr.cpaleop.download.domain.repositories.FileRepository
import gr.cpaleop.download.domain.usecases.DownloadFileUseCase
import gr.cpaleop.download.domain.usecases.DownloadFileUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.File

val downloadModule = module {
    single<DownloadFileUseCase> { DownloadFileUseCaseImpl(get(), get()) }
    single { DownloadedFileMapper() }
    single<DeviceStorageRepository> { DeviceStorageRepositoryImpl(get(named<DownloadFolder>())) }
    single<FileRepository> { FileRepositoryImpl(get(), get()) }
    single { provideDownloadApi(get()) }
    single(named<DownloadFolder>()) { provideDownloadFolder(get()) }
}

private fun provideDownloadApi(retrofit: Retrofit): DownloadApi {
    return retrofit.create(DownloadApi::class.java)
}

@DownloadFolder
private fun provideDownloadFolder(applicationContext: Context): File {
    return applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        ?: applicationContext.cacheDir
}