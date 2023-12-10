package com.dotjoo.aghslilnidelivery.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
 import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
 import com.dotjoo.aghslilnidelivery.R

class SimpleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable = ContextCompat.getDrawable(context, R.drawable.line_divider)!!;


    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        var left = parent.paddingLeft
        var right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
              left = child.paddingLeft
              right = child.paddingRight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

}