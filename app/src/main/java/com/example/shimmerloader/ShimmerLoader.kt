package com.example.shimmerloader
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ShimmerLoader {


    // Duration of the shimmer animation in milliseconds
    // مدت زمان انیمیشن شیمر بر حسب میلی‌ثانیه
    private val animationDuration = 1000

    // Multiplier to determine shimmer movement range
    // ضریب جابجایی شیمر برای ایجاد افکت مناسب
    private val shimmerOffsetMultiplier = 2f

    // Size of the shimmered icon
    // اندازه آیکن در حالت لودینگ
    private val iconSize = 100.dp

    // Height of the shimmered text blocks
    // ارتفاع مستطیل‌های متنی در حالت لودینگ
    private val textHeight = 17.dp

    // Space between shimmer items horizontally
    // فاصله افقی بین آیتم‌های لودینگ
    private val spacerWidth = 16.dp

    // Space between shimmer items vertically
    // فاصله عمودی بین آیتم‌ها
    private val spacerHeight = 8.dp

    // Loading state to control shimmer visibility
    // وضعیت لودینگ برای نمایش یا مخفی‌سازی شیمر
    private var isLoading by mutableStateOf(true)

    // Start shimmer loading
    // شروع حالت لودینگ
    fun startLoading() {
        isLoading = true
    }

    // Stop shimmer loading
    // توقف حالت لودینگ
    fun stopLoading() {
        isLoading = false
    }

    /**
     * Composable for showing shimmer effect or real content
     * کامپوزیبل برای نمایش افکت شیمر یا محتوای واقعی
     */
    @Composable
    fun ShimmerListItem(
        contentAfterLoading: @Composable () -> Unit,
        modifier: Modifier = Modifier
    ) {
        if (isLoading) {

            // Set layout direction to RTL for proper alignment
            // تنظیم جهت راست‌چین برای نمایش بهتر در زبان فارسی
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {


                Box(modifier = modifier.padding(top = 6.dp)) {

                    var size by remember { mutableStateOf(IntSize.Zero) }

                    // Animation for shimmer movement
                    // انیمیشن تکرار شونده برای حرکت شیمر
                    val transition = rememberInfiniteTransition(label = "shimmerTransition")
                    val startOffsetX by transition.animateFloat(
                        initialValue = -shimmerOffsetMultiplier * size.width.toFloat(),
                        targetValue = shimmerOffsetMultiplier * size.width.toFloat(),
                        animationSpec = infiniteRepeatable(
                            animation = TweenSpec(durationMillis = animationDuration),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "shimmerOffset"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        // Shimmer box for image/icon
                        // باکس شیمر برای تصویر یا آیکن
                        Box(
                            modifier = Modifier
                                .size(iconSize)
                                .shimmerBackground(
                                    size,
                                    startOffsetX,
                                    onSizeChanged = { newSize -> size = newSize }
                                )
                        )

                        Spacer(modifier = Modifier.width(spacerWidth))

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {

                            // First shimmer line (title)
                            // خط اول شیمر (مثلاً تیتر)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(textHeight)
                                    .shimmerBackground(
                                        size,
                                        startOffsetX,
                                        onSizeChanged = { newSize -> size = newSize }
                                    )
                            )

                            Spacer(modifier = Modifier.height(spacerHeight))

                            // Second shimmer line (subtitle or content)
                            // خط دوم شیمر (مثلاً زیرتیتر)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(textHeight)
                                    .shimmerBackground(
                                        size,
                                        startOffsetX,
                                        onSizeChanged = { newSize -> size = newSize }
                                    )
                            )
                        }
                    }
                }
            }
        } else {

            // Show actual content after loading finishes
            // نمایش محتوای واقعی پس از پایان لودینگ
            Box(modifier = Modifier.fillMaxWidth()) {
                contentAfterLoading()
            }
        }
    }


}

/**
 * Extension modifier for shimmer background with animation
 * مودیفایر اختصاصی برای ایجاد افکت پس‌زمینه‌ی شیمر به همراه انیمیشن
 */
private fun Modifier.shimmerBackground(
    size: IntSize,
    startOffsetX: Float,
    onSizeChanged: (IntSize) -> Unit
): Modifier = composed {

    var localSize by remember { mutableStateOf(size) }

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9BA5C9),
                Color(0xFF3E414D),
                Color(0xFF808FAB)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + localSize.width.toFloat(), localSize.height.toFloat())
        )
    ).onGloballyPositioned { layoutCoordinates ->
        val newSize = layoutCoordinates.size
        if (newSize != localSize) {
            onSizeChanged(newSize)
            localSize = newSize
        }
    }
}
