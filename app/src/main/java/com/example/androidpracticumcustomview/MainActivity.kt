package com.example.androidpracticumcustomview

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer

/*
Задание:
Реализуйте необходимые компоненты.
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)/*
        Раскомментируйте нужный вариант
         */
        startXmlPracticum() // «традиционный» android (XML)
//          setContent { // Jetpack Compose
//             MainScreen()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)

        val firstView = TextView(this).apply {
            text = context.getString(R.string.first_textview)
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                20f,
                context.resources.displayMetrics
            )
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            customContainer.addView(this)
        }

        val secondView = TextView(this).apply {
            text = context.getString(R.string.second_textview)
        }

        // Добавление второго элемента через некоторое время
//        Handler(Looper.getMainLooper()).postDelayed({
//            customContainer.addView(secondView)
//        }, 2000)
    }
}