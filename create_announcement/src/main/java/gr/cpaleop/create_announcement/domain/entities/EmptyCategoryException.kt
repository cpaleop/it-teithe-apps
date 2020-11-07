package gr.cpaleop.create_announcement.domain.entities

data class EmptyCategoryException(override val message: String? = "Announcement category is empty") :
    Throwable()