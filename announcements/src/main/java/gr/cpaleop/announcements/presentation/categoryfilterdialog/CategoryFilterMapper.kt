package gr.cpaleop.announcements.presentation.categoryfilterdialog

import gr.cpaleop.core.domain.entities.Category

class CategoryFilterMapper {

    operator fun invoke(category: Category): CategoryFilter {
        return CategoryFilter(
            id = category.id,
            name = category.name,
            selected = false
        )
    }
}