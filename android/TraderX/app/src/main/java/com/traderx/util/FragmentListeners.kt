package com.traderx.util

import android.content.Context

interface FragmentTitleListeners {
    fun showFragmentTitle(title: String?)
}
interface FragmentTitleEmitters {
    fun setFragmentTitle(context: Context?, title: String?)
}