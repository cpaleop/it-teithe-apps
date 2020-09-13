package gr.cpaleop.categoryfilter.domain.usecases

interface GetCategoryNameUseCase {

    suspend operator fun invoke(categoryId: String): String
}