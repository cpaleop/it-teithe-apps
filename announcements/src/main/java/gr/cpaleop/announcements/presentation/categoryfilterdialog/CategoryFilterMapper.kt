package gr.cpaleop.announcements.presentation.categoryfilterdialog

import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryFilterMapper {

    suspend operator fun invoke(category: Category): CategoryFilter =
        withContext(Dispatchers.Default) {
            CategoryFilter(
                id = category.id,
                name = category.name,
                selected = false
            )
        }
}