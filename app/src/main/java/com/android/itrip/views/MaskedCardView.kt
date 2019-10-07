package com.android.itrip.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView
import android.annotation.SuppressLint
import android.graphics.Path
import android.graphics.RectF

/**
 * A Card view that clips the content of any shape, this should be done upstream in card,
 * working around it for now.
 */
class MaskedCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {
    @SuppressLint("RestrictedApi")
    private val path: Path = Path()
    private val rectF = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    @SuppressLint("RestrictedApi")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectF.right = w.toFloat()
        rectF.bottom = h.toFloat()
        super.onSizeChanged(w, h, oldw, oldh)
    }
}