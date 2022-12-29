package com.sparklab.ai

import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayout
import com.sparklab.ai.databinding.ActivityWritingBinding

class WritingActivity : AppCompatActivity() {

    lateinit var binding: ActivityWritingBinding
    lateinit var mContext : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navOptions: NavOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.getStartDestination(), false)
            .build()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(mContext, R.color.tab_text_color)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                when (tab.position) {
                    0 -> navController.navigate(R.id.nav_home, null, navOptions)
                    1 -> navController.navigate(R.id.nav_gallery, null, navOptions)
                    2 -> navController.navigate(R.id.nav_slideshow, null, navOptions)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.tabs.getTabAt(0)?.select()


    }
}