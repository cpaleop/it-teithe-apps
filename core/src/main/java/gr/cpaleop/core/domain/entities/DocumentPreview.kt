package gr.cpaleop.core.domain.entities

@Retention(AnnotationRetention.BINARY)
annotation class DocumentPreview {

    companion object {
        const val FILE = -1
        const val FOLDER = 1
    }
}