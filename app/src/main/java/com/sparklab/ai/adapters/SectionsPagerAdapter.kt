package com.sparklab.ai.adapters

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sparklab.ai.R
import com.sparklab.ai.ui.improve.ImproveFragment
import com.sparklab.ai.ui.replay.ReplyFragment
import com.sparklab.ai.ui.write.WriteFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_write,
    R.string.tab_replay,
    R.string.tab_improve,
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return WriteFragment()
        } else if (position == 1) {
            return ReplyFragment()
        } else  {
            return ImproveFragment()
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {

        //val drawable = ContextCompat.getDrawable(ApplicationProvider.getApplicationContext<Context>(), imageList.get(position))
        val drawable = context.getDrawable(R.drawable.ic_menu_camera)

        // set bound

        // set bound
        drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

       // drawable.setTint(context.resources.getColor(R.color.white))
        // Initialize spannable image

        // Initialize spannable image
        val spannableString = SpannableString("" + TAB_TITLES.get(position))

        // Initialize image span

        // Initialize image span
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_CENTER)

        // Set span

        // Set span
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        // return spannable string

        // return spannable string
        return spannableString


    }

    override fun getCount(): Int {
        return 3
    }
}