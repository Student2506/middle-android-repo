package com.example.androidpracticumcustomview.ui.theme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import kotlinx.coroutines.delay

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
    var timeShift = 0L

    init {
        this.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (children.size == 1) {
                        animation(children[0], -measuredHeight / 4f)
                    }
                    timeShift = SystemClock.uptimeMillis() - timeShift
                }
            })
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        children.forEachIndexed { _, child ->
            val width = child.measuredWidth
            val height = child.measuredHeight
            val demiHeight = (bottom - top) / 2
            val childLeft = left + (right - left - width) / 2
            val childTop = top + demiHeight - height / 2
            child.layout(childLeft, childTop, childLeft + width, childTop + height)
        }
    }

    override fun addView(child: View) {
        super.addView(child)
        if (children.size >= 2) throw IllegalStateException("2 children MAX")
        children.add(child)
        child.alpha = 0f
        if (children.size > 1) {
            animation(child, measuredHeight / 4f)
        } else {
            timeShift = SystemClock.uptimeMillis()
        }
        requestLayout()
    }

    private fun animation(view: View, height: Float) {
        val visibilityAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f)
        visibilityAnimation.setDuration(TRANSPARENCY_ANIMATION_LENGTH)
        val offsetAnimation = ObjectAnimator.ofFloat(view, "translationY", height)
        offsetAnimation.setDuration(MOVE_ANIMATION_LENGTH)
        val animSet = AnimatorSet()
        animSet.playTogether(visibilityAnimation, offsetAnimation)
        animSet.startDelay = if (children.size == 1) { 0 } else { timeShift }
        animSet.start()
    }

    companion object {
        const val MOVE_ANIMATION_LENGTH = 5000L
        const val TRANSPARENCY_ANIMATION_LENGTH = 2000L
    }
}