package com.mikhailgrigorev.mts_home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewDecoration
(
    private val margin_top: Int,
    private val margin_horizontal: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = margin_top
        outRect.bottom = margin_top
        outRect.left = margin_horizontal
        outRect.right = margin_horizontal
    }

}