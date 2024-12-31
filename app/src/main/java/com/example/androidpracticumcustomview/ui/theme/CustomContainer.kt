package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs) {

    val children: MutableList<View> = ArrayList()

    init {
//        setWillNotDraw(false)
        this.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (children.size == 1)
                    children[0].animate().alpha(1f)
                        .translationY(-measuredHeight / 4f)
                        .setDuration(DURATION)
            }
        });
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        children.forEachIndexed { index, child ->
            val width = child.measuredWidth
            val height = child.measuredHeight
            val demiHeight = (bottom - top) / 2
            val childLeft = left + (right - left - width) / 2
//            val childTop = top + (demiHeight - top - height) / 2 + demiHeight * index
            val childTop = top + demiHeight - height / 2 //+ demiHeight * index
            child.layout(childLeft, childTop, childLeft + width, childTop + height)
        }
    }

    override fun addView(child: View) {
        super.addView(child)
        if (children.size >= 2) throw IllegalStateException("2 children MAX")
        children.add(child)
        child.alpha = 0f
        Log.d(TAG, height.toString())
        if (children.size > 1) {
            child.animate().alpha(1f)
                .translationY(measuredHeight / 4f)
                .setDuration(DURATION)
        }
        requestLayout()
    }

    companion object {
        private const val DURATION = 5000L
        private const val TAG = "CustomContainer"
    }
}