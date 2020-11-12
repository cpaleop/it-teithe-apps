package gr.cpaleop.core.data

import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.data.mappers.CategoryRegisteredMapper
import gr.cpaleop.core.data.model.local.RemoteCategoryDao
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.domain.repositories.CategoriesRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoriesRepositoryImpl(
    private val categoriesApi: CategoriesApi,
    private val remoteCategoryDao: RemoteCategoryDao,
    private val categoryMapper: CategoryMapper,
    private val categoryRegisteredMapper: CategoryRegisteredMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> {
        val remoteCategoryList = categoriesApi.fetchCategories()
        remoteCategoryDao.nukeAndInsertAll(remoteCategoryList)
        return remoteCategoryList.mapAsync(categoryMapper::invoke)
    }

    override suspend fun getCategoriesFlow(): Flow<List<Category>> {
        return coroutineScope {
            launch {
                val remoteCategoryList = categoriesApi.fetchCategories()
                remoteCategoryDao.nukeAndInsertAll(remoteCategoryList)
            }
            return@coroutineScope remoteCategoryDao.fetchAllFlow().map {
                it.map(categoryMapper::invoke)
            }
        }
    }

    override suspend fun getRegisteredCategories(): List<Category> {
        val remoteCategoryList = categoriesApi.fetchCategories()
        remoteCategoryDao.nukeAndInsertAll(remoteCategoryList)
        val remoteRegisteredCategories = categoriesApi.fetchRegisteredCategories()
        return remoteCategoryList.mapAsyncSuspended { categoryRegisteredMapper(it, remoteRegisteredCategories) }
            .filterNotNull()
    }

    override suspend fun getRegisteredCategoriesFlow(): Flow<List<Category>> {
        return coroutineScope {
            launch {
                val remoteCategoryList = categoriesApi.fetchCategories()
                remoteCategoryDao.nukeAndInsertAll(remoteCategoryList)
            }
            val remoteRegisteredCategories = categoriesApi.fetchRegisteredCategories()
            return@coroutineScope remoteCategoryDao.fetchAllFlow().map {
                it.mapAsyncSuspended { categoryRegisteredMapper(it, remoteRegisteredCategories) }
                    .filterNotNull()
            }
        }
    }
}