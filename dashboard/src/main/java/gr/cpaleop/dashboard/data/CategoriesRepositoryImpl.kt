package gr.cpaleop.dashboard.data

import com.google.gson.Gson
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.local.AppDatabase
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.dashboard.data.mappers.CategoryMapper
import gr.cpaleop.dashboard.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    private val gson: Gson,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val categoryMapper: CategoryMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val remoteCategories = appDatabase.remoteCategoryDao().getAll()
        val remoteRegisteredCategories = categoriesApi.fetchRegisteredCategories()
        return@withContext remoteCategories.mapAsyncSuspended {
            categoryMapper(
                it,
                remoteRegisteredCategories
            )
        }.filterNotNull()
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