package com.traderx.util

import android.content.Context
import android.widget.Button

interface FragmentTitleListeners {
    fun showFragmentTitle(title: String?)

    fun showFragmentActionButton(text: String, onClick: (button: Button) -> Unit)

    fun hideFragmentActionButton()
}

interface FragmentTitleEmitters {
    fun setFragmentTitle(context: Context?, title: String?) {
        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }

    fun setFragmentActionButton(context: Context?, text: String, onClick: (button: Button) -> Unit) {
        if (context is FragmentTitleListeners) {
            context.showFragmentActionButton(text, onClick)
        }
    }

    fun hideFragmentActionButton(context: Context?) {
        if (context is FragmentTitleListeners) {
            context.hideFragmentActionButton()
        }
    }
}