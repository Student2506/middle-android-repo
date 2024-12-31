package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?,
) {
    // Блок создания и инициализации переменных
    // ..
    val ANIMATION_LENGTH = 5000
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val coroutineScope = rememberCoroutineScope()
    var isSecondElementVisible by remember { mutableStateOf(false) }
    var isFirstElementVisible by remember { mutableStateOf(false) }
    val translation =
        updateTransition(targetState = isFirstElementVisible, label = "ChangeVisibility")
    val boxVisibility by translation.animateFloat(
        transitionSpec = { tween(durationMillis = ANIMATION_LENGTH) }, label = "Visibility"
    ) { state ->
        if (state) 1f else 0f
    }
    val boxPosition by translation.animateDp(
        transitionSpec = { tween(durationMillis = ANIMATION_LENGTH) }, label = "Position"
    ) { state ->
        if (state) -screenHeight / 4 else 0.dp
    }
    val translationSecond =
        updateTransition(targetState = isSecondElementVisible, label = "ChangeVisibilityPosition")
    val secondBoxVisibility by translationSecond.animateFloat(
        transitionSpec = { tween(durationMillis = ANIMATION_LENGTH) }, label = "Visibility"
    ) { state ->
        if (state) 1f else 0f
    }


    val secondBoxPosition by translationSecond.animateDp(
        transitionSpec = { tween(durationMillis = ANIMATION_LENGTH) }, label = "Position"
    ) { state ->
        if (state) screenHeight / 4 else 0.dp
    }


    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isFirstElementVisible = true
            delay(2000)
            isSecondElementVisible = true
        }
    }

    // Основной контейнер
    Box {
        Box(
            modifier = Modifier
                .offset(0.dp, boxPosition)
                .alpha(boxVisibility)
        ) {
            firstChild?.let {
                it()
            }
        }
        secondChild?.let {
            Box(
                modifier = Modifier
                    .offset(0.dp, secondBoxPosition)
                    .alpha(secondBoxVisibility)
            ) {
                it()
            }
        }
    }

}