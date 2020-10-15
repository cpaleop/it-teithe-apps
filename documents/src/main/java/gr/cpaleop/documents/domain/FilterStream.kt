package gr.cpaleop.documents.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Singleton cold channel for filtering since AnnouncementFolder list and Document list use the same source of filter
 */
@ExperimentalCoroutinesApi
class FilterStream : MutableStateFlow<String> by MutableStateFlow("")