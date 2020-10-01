package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.documents.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveDocumentSortUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    ObserveDocumentSortUseCase {

    private var documentSortChannel = ConflatedBroadcastChannel<DocumentSort>()

    override suspend fun invoke(): Flow<DocumentSort> {
        val documentSort = preferencesRepository.getDocumentSort()
        return documentSortChannel.apply {
            send(documentSort)
        }.asFlow().onEach(preferencesRepository::updateDocumentSort)
    }

    override suspend fun update(documentSort: DocumentSort) {
        documentSortChannel.send(documentSort)
    }
}