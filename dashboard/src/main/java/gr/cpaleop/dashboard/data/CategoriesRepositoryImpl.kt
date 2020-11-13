package gr.cpaleop.dashboard.data

import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.CategoryRegisteredMapper
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val categoriesDataSource: CategoriesDataSource,
    private val categoriesRegisteredMapper: CategoryRegisteredMapper
) : CategoriesRepository {

    override suspend fun refreshCategories() = withContext(ioDispatcher) {
        categoriesDataSource.fetchCategories()
        return@withContext
    }

    override suspend fun getCategoriesFlow(): Flow<List<Category>> = withContext(ioDispatcher) {
        val remoteRegisteredCategories = categoriesDataSource.fetchRegisteredCategories()
        categoriesDataSource.fetchCategoriesFlow(true).map { remoteCategories ->
            remoteCategories.mapNotNull {
                categoriesRegisteredMapper(it, remoteRegisteredCategories)
            }
        }
    }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) = withContext(ioDispatcher) {
        categoriesDataSource.updateRegisteredCategories(registeredCategories, nonRegisteredCategories)
    }
}