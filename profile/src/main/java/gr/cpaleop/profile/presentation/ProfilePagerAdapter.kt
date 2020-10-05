package gr.cpaleop.profile.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gr.cpaleop.profile.presentation.personal.ProfilePersonalDetailsFragment
import gr.cpaleop.profile.presentation.socials.ProfileSocialsFragment

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilePersonalDetailsFragment()
            1 -> ProfileSocialsFragment()
            else -> throw IllegalArgumentException("No fragment found for position: $position")
        }
    }

    companion object {

        private const val SIZE = 2
        val titles = listOf("Personal Details", "Socials")
    }
}