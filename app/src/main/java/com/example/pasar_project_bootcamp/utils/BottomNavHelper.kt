package com.example.pasar_project_bootcamp.utils

import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.LayoutBottomNavBinding

enum class BottomNavTab { HOME, CART, PROFILE }

fun LayoutBottomNavBinding.setActiveTab(tab: BottomNavTab) {
    val activeColor = navHomeIcon.resources.getColor(R.color.clay_green_700, null)
    val inactiveColor = navHomeIcon.resources.getColor(R.color.clay_text_tertiary, null)

    val tabs = listOf(
        BottomNavTab.HOME to Triple(navHomeIcon, navHomeText, navHomeIndicator),
        BottomNavTab.CART to Triple(navCartIcon, navCartText, navCartIndicator),
        BottomNavTab.PROFILE to Triple(navProfileIcon, navProfileText, navProfileIndicator)
    )

    tabs.forEach { (tabId, views) ->
        val (icon, text, indicator) = views
        val isActive = tabId == tab
        val color = if (isActive) activeColor else inactiveColor
        icon.setColorFilter(color)
        text.setTextColor(color)
        text.setTypeface(text.typeface, if (isActive) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
        indicator.visibility = if (isActive) android.view.View.VISIBLE else android.view.View.INVISIBLE
    }
}
