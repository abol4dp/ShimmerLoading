package com.example.shimmerloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shimmerloader.ui.theme.ShimmerLoaderTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // فعال‌سازی نمایش بدون حاشیه | Enable edge-to-edge layout
        setContent {
            TestShimmer()
        }
    }

    // تابع تست برای نمایش شیمر به مدت ۲ ثانیه | Test shimmer for 2 seconds
    @Composable
    private fun TestShimmer() {
        val shimmerLoader = remember { ShimmerLoader() }
        val isLoading = remember { mutableStateOf(true) }

        // توقف لودینگ بعد از ۲ ثانیه | Stop shimmer after 2 seconds
        LaunchedEffect(Unit) {
            delay(2000)
            shimmerLoader.stopLoading()
            isLoading.value = false
        }

        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                items(6) { index ->
                    if (isLoading.value) {
                        // آیتم شیمر لودینگ | Shimmer loading item
                        shimmerLoader.ShimmerListItem(
                            contentAfterLoading = {} // محتوا واقعی غیر تستی در اینجا قرار میگیره
                        )
                    } else {
                        // محتوای نهایی بعد از لودینگ | Final content after loading
                        Column(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .padding(end = 30.dp)
                        ) {
                            Text(
                                text = "Shimmer Test",
                                color = Color.Black,
                                fontSize = 30.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(end = 5.dp),
                                text = "Test",
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
