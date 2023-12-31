package com.cb.myshowcase.common

import android.content.Context
import android.content.ContextWrapper
import android.view.HapticFeedbackConstants
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import java.io.File

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024
val File.sizeInGb get() = sizeInMb / 1024
val File.sizeInTb get() = sizeInGb / 1024

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xB9B5B5),
                Color(0xDDFFFFFF),
                Color(0xC2C1C1),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}


/**
 * Find a [androidx.activity.ComponentActivity] from the current context.
 * By default Jetpack Compose project uses ComponentActivity for MainActivity,
 * It is a parent of [androidx.fragment.app.FragmentActivity] or [AppCompatActivity]
 */
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


private enum class ButtonState {
    Pressed, Idle;
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.bounceClickable(
    dampingRatio: Float = 0.85f,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    useHapticFeedback: Boolean = true,
) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        targetValue = when (buttonState) {
            ButtonState.Pressed -> dampingRatio
            ButtonState.Idle -> 1f
        }
    )
    val view = LocalView.current

    this
        .clip(shape)
        .combinedClickable(
            enabled = enabled,
            onClick = onClick,
            onDoubleClick = onDoubleClick,
            onLongClick = onLongClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        )
        .graphicsLayer {
            this.shape = shape
            this.scaleX = scale
            this.scaleY = scale
        }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = when (buttonState) {
                    ButtonState.Pressed -> {
                        waitForUpOrCancellation()
                        if (useHapticFeedback) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        ButtonState.Idle
                    }
                    ButtonState.Idle -> {
                        awaitFirstDown(false)
                        ButtonState.Pressed
                    }
                }
            }
        }
}