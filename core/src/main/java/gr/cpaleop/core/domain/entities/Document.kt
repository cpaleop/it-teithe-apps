package gr.cpaleop.core.domain.entities

data class Document(
    val absolutePath: String,
    val name: String,
    val type: String,
    val size: Long,
    val previewUri: String,
    val lastModified: Long
)