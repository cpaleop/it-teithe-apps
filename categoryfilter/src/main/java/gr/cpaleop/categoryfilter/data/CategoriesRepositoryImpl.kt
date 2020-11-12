package gr.cpaleop.categoryfilter.data

import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository
import gr.cpaleop.core.datasource.model.local.AppDatabase
import gr.cpaleop.core.datasource.model.response.RemoteCategoryFilter
import gr.cpaleop.core.datasource.remote.CategoriesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoriesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val categoriesApi: CategoriesApi,
    private val json: Json
) : CategoriesRepository {

    override suspend fun getCategoryNameById(categoryId: String): String =
        withContext(Dispatchers.IO) {
            val cachedCategory = appDatabase.remoteCategoryDao().fetchFromId(categoryId)?.name

            if (cachedCategory.isNullOrEmpty()) {
                val categoryQuery = json.encodeToString(RemoteCategoryFilter(categoryId))
                return@withContext categoriesApi.fetchCategoryById(categoryQuery)
                    .firstOrNull()?.name ?: ""
            } else cachedCategory
        }
}