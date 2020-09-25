package gr.cpaleop.dashboard.domain.entities

@Retention(AnnotationRetention.BINARY)
annotation class DocumentPreview {

    companion object {
        const val FILE = -1
        const val FOLDER = 0
    }
}