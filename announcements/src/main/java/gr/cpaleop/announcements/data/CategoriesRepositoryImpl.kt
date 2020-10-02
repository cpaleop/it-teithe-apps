package gr.cpaleop.announcements.data

import com.google.gson.Gson
import gr.cpaleop.announcements.domain.repositories.CategoriesRepository
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    private val gson: Gson,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val categoryMapper: CategoryMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val remoteCategories = appDatabase.remoteCategoryDao().fetchAll()
        val remoteRegisteredCategories = categoriesApi.fetchRegisteredCategories()
        return@withContext remoteCategories.mapAsyncSuspended {
            categoryMapper(
                it,
                remoteRegisteredCategories
            )
        }.filterNotNull()
    }

    /**
     * We dont care if its registered or not for this call
     */
    override suspend fun getCachedCategories(): List<Category> =
        withContext(Dispatchers.IO) {
            val cachedRemoteCategories = appDatabase.remoteCategoryDao().fetchAll()
            cachedRemoteCategories.mapAsync {
                Category(
                    id = it.id,
                    name = it.name ?: "",
                    isRegistered = false
                )
            }
        }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) = withContext(Dispatchers.IO) {
        val registeredCategoriesString = gson.toJson(registeredCategories)
        val nonegisteredCategoriesString = gson.toJson(nonRegisteredCategories)
        categoriesApi.updateRegisteredCategories(
            registeredCategoriesString,
            nonegisteredCategoriesString
        )
    }
}