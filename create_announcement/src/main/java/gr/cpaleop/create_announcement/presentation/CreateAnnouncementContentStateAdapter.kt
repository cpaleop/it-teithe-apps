package gr.cpaleop.create_announcement.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.presentation.content.AnnouncementContentEnglishFragment
import gr.cpaleop.create_announcement.presentation.content.AnnouncementContentGreekFragment

class CreateAnnouncementContentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnnouncementContentEnglishFragment()
            1 -> AnnouncementContentGreekFragment()
            else -> throw IllegalArgumentException("No fragment found for position: $position")
        }
    }

    companion object {

        private const val SIZE = 2
        val titles = listOf(
            R.string.create_announcement_english,
            R.string.create_announcement_greek
        )
    }
}