package gr.cpaleop.teithe_apps.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.platform.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialFadeThrough
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.teithe_apps.R as appR

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateViewBinding(inflater, container).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(appR.integer.animation_duration).toLong()
        }
        returnTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(appR.integer.animation_duration).toLong()
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(appR.integer.animation_duration).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(appR.integer.animation_duration).toLong()
        }
        super.onCreate(savedInstanceState)
    }
}