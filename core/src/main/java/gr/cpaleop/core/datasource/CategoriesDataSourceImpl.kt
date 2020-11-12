package gr.cpaleop.core.datasource

import androidx.room.withTransaction
import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.datasource.model.local.AppDatabase
import gr.cpaleop.core.datasource.model.local.RemoteCategoryDao
import gr.cpaleop.core.datasource.model.response.RemoteCategory
import gr.cpaleop.core.datasource.model.response.RemoteCategoryFilter
import gr.cpaleop.core.datasource.model.response.RemoteRegisteredCategory
import gr.cpaleop.core.datasource.remote.CategoriesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalCoroutinesApi
class CategoriesDataSourceImpl(
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val remoteCategoryDao: RemoteCategoryDao,
    private val json: Json
) : CategoriesDataSource {

    override suspend fun fetchPublicCategories(): List<RemoteCategory> {
        return categoriesApi.fetchPublicCategories().also {
            appDatabase.withTransaction {
                remoteCategoryDao.nukeAndInsertAll(it)
            }
        }
    }

    override suspend fun fetchCategories(): List<RemoteCategory> {
        return categoriesApi.fetchCategories().also {
            appDatabase.withTransaction {
                remoteCategoryDao.nukeAndInsertAll(it)
            }
        }
    }

    override suspend fun fetchCategoriesFlow(cache: Boolean): Flow<List<RemoteCategory>> {
        if (!cache) {
            categoriesApi.fetchCategories().also {
                appDatabase.withTransaction {
                    remoteCategoryDao.nukeAndInsertAll(it)
                }
            }
        }
        return remoteCategoryDao.fetchAllFlow()
    }

    override suspend fun fetchRegisteredCategories(): List<RemoteRegisteredCategory> {
        return categoriesApi.fetchRegisteredCategories()
    }

    override suspend fun fetchCategoryById(id: String?): RemoteCategory {
        val cachedCategory = remoteCategoryDao.fetchFromId(id)
        return if (cachedCategory == null) {
            val categoryQuery = json.encodeToString(RemoteCategoryFilter(id ?: ""))
            categoriesApi.fetchCategoryById(categoryQuery).first()
        } else {
            cachedCategory
        }
    }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) {
        val registeredCategoriesString = json.encodeToString(registeredCategories)
        val nonegisteredCategoriesString = json.encodeToString(nonRegisteredCategories)
        categoriesApi.updateRegisteredCategories(registeredCategoriesString, nonegisteredCategoriesString)
    }
}