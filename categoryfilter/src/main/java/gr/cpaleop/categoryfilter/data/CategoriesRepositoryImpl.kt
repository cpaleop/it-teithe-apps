package gr.cpaleop.categoryfilter.data

import com.google.gson.Gson
import gr.cpaleop.categoryfilter.data.model.RemoteCategoryFilter
import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository
import gr.cpaleop.core.data.remote.CategoriesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(private val categoriesApi: CategoriesApi, private val gson: Gson) :
    CategoriesRepository {

    override suspend fun getCategoryNameById(categoryId: String): String =
        withContext(Dispatchers.IO) {
            val categoryQuery = gson.toJson(RemoteCategoryFilter(categoryId))
            categoriesApi.fetchCategoryById(categoryQuery).firstOrNull()?.name ?: ""
        }
}