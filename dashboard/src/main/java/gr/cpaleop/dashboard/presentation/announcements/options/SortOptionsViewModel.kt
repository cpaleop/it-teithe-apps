package gr.cpaleop.dashboard.presentation.announcements.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortOption
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortType
import gr.cpaleop.dashboard.presentation.announcements.options.sort.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SortOptionsViewModel : ViewModel() {

    private val _options = MutableLiveData<List<SortOption>>()
    val options: LiveData<List<SortOption>> = _options.toSingleEvent()

    fun presentSortOptions() {
        viewModelScope.launch {
            _options.value = withContext(Dispatchers.Default) {
                listOf(
                    SortOption(
                        R.string.option_sort_by_date_published,
                        Type(SortType.DESCENDING, R.drawable.ic_arrow_down),
                        false
                    ),
                    SortOption(
                        R.string.option_sort_by_category,
                        Type(SortType.DESCENDING, R.drawable.ic_arrow_down),
                        false
                    ),
                    SortOption(
                        R.string.option_sort_by_publisher,
                        Type(SortType.DESCENDING, R.drawable.ic_arrow_down),
                        false
                    )
                )
            }
        }
    }
}