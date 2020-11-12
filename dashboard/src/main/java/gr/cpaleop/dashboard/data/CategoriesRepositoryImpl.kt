package gr.cpaleop.dashboard.data

import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.CategoryRegisteredMapper
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    private val categoriesDataSource: CategoriesDataSource,
    private val categoriesRegisteredMapper: CategoryRegisteredMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val remoteRegisteredCategories = categoriesDataSource.fetchRegisteredCategories()
        categoriesDataSource.fetchCategories().map {
            categoriesRegisteredMapper(it, remoteRegisteredCategories)
        }.filterNotNull()
    }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) = withContext(Dispatchers.IO) {
        categoriesDataSource.updateRegisteredCategories(registeredCategories, nonRegisteredCategories)
    }
}