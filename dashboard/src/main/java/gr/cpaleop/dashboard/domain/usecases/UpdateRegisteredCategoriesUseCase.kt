package gr.cpaleop.dashboard.domain.usecases

interface UpdateRegisteredCategoriesUseCase {

    suspend operator fun invoke(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    )
}