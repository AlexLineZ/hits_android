package com.example.hits_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hits_android.ui.theme.Hits_androidTheme

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hits_androidTheme {
                ZoomableComposable()
            }
        }
    }
}

@Composable
fun ZoomableComposable() {
    // Reacting to state changes is the core behavior of Compose.
    // We use the state composable that is used for holding a
    // state value in this composable for representing the current
    // value scale(for zooming in the image)
    // & translation(for panning across the image).
    // Any composable that reads the value of counter will
    // be recomposed any time the value changes.
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // In the example below, we make the Column composable zoomable
    // by leveraging the Modifier.pointerInput modifier
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
    ) {
        // painterResource method loads an image resource asynchronously
        val imagepainter = painterResource(id = R.drawable.ic_launcher_background)
        // We use the graphicsLayer modifier to modify the scale & translation
        // of the image.
        // This is read from the state properties that we created above.
        Image(
            modifier = Modifier.fillMaxSize().graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            ),
            painter = imagepainter,
            contentDescription = "androids launcher default launcher background image"
        )
    }
}

@Preview
@Composable
fun ZoomableComposablePreview() {
    ZoomableComposable()
}