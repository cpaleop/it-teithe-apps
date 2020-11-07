package gr.cpaleop.create_announcement.domain.entities

data class EmptyTextException(override val message: String? = "Announcement text is empty") :
    Throwable()