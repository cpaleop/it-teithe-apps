package gr.cpaleop.dashboard.presentation.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentCatalogBinding

class CatalogFragment : BaseFragment<FragmentCatalogBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCatalogBinding {
        return FragmentCatalogBinding.inflate(inflater, container, false)
    }
}