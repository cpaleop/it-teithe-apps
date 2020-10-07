package gr.cpaleop.profile.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gr.cpaleop.profile.R
import gr.cpaleop.profile.presentation.personal.ProfilePersonalDetailsFragment
import gr.cpaleop.profile.presentation.settings.SettingsFragment
import gr.cpaleop.profile.presentation.socials.ProfileSocialsFragment

class ProfileStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilePersonalDetailsFragment()
            1 -> ProfileSocialsFragment()
            2 -> SettingsFragment()
            else -> throw IllegalArgumentException("No fragment found for position: $position")
        }
    }

    companion object {

        private const val SIZE = 3
        val titles = listOf(
            R.string.profile_details_personal,
            R.string.profile_details_socials,
            R.string.profile_details_settings
        )
    }
}