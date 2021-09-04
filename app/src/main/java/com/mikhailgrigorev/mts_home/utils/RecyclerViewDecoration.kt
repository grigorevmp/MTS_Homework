package com.mikhailgrigorev.mts_home.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewDecoration
(
    private val margin_top: Int,
    private val margin_horizontal: Int,
    private val spanCount: Int = 2,
    private val includeEdge: Boolean = false
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position: Int = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if(includeEdge) {
            outRect.left = margin_horizontal - column * margin_horizontal / spanCount
            outRect.right = (column + 1) * margin_horizontal / spanCount

            if (position < spanCount) {
                outRect.top = margin_horizontal
            }
        }
        else{
            outRect.left = margin_horizontal
            outRect.right = margin_horizontal
        }


        outRect.top = margin_top
        outRect.bottom = margin_top
    }

}