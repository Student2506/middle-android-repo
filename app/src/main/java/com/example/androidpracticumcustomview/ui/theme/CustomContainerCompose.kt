package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.graphicsLayer
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
    val coroutineScope = rememberCoroutineScope()
    var isSecondElementVisible by remember { mutableStateOf(false) }
    var isFirstElementVisible by remember { mutableStateOf(false) }
    val animatedFirstAlpha by animateFloatAsState(
        targetValue = if (isFirstElementVisible) 1f else 0f, label = "alpha"
    )
    val offsetY = remember { Animatable(0f) }
    val alphaFirst = remember { Animatable(0f)}
    fun animateText() {
        coroutineScope.launch {

            alphaFirst.animateTo(
                targetValue = 1f, animationSpec = tween(600)
            )
            offsetY.animateTo(
                targetValue = -100f, animationSpec = tween(600)
            )
        }
    }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isFirstElementVisible = true
            animateText()
            delay(2000)
            isSecondElementVisible = true
        }
    }

    // Основной контейнер
    Box {


            Box(modifier = Modifier.offset(0.dp, offsetY.value.dp).alpha(alphaFirst.value) ) {
                firstChild?.let {
                    it()
                }
            }
        AnimatedVisibility(
            visible = isSecondElementVisible,
            enter = fadeIn(animationSpec = tween(600)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            secondChild?.let {
                it()
            }
        }

    }


}