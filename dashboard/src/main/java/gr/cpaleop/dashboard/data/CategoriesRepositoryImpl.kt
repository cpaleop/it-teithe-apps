package gr.cpaleop.dashboard.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.CategoryRegisteredMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoriesRepositoryImpl(
    private val json: Json,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val categoryRegisteredMapper: CategoryRegisteredMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val remoteCategories = appDatabase.remoteCategoryDao().fetchAll()
        val remoteRegisteredCategories = categoriesApi.fetchRegisteredCategories()
        return@withContext remoteCategories.mapAsyncSuspended {
            categoryRegisteredMapper(
                it,
                remoteRegisteredCategories
            )
        }.filterNotNull()
    }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) = withContext(Dispatchers.IO) {
        val registeredCategoriesString = json.encodeToString(registeredCategories)
        val nonegisteredCategoriesString = json.encodeToString(nonRegisteredCategories)
        categoriesApi.updateRegisteredCategories(
            registeredCategoriesString,
            nonegisteredCategoriesString
        )
    }
}