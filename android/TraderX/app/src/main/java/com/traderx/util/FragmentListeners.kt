package com.traderx.util

import android.content.Context

interface FragmentTitleListeners {
    fun showFragmentTitle(title: String?)
}

interface FragmentTitleEmitters {
    fun setFragmentTitle(context: Context?, title: String?) {
        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }
}