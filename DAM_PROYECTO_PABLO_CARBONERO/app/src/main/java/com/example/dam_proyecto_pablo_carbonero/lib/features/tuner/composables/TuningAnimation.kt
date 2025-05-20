package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun TuningAnimation(
    graphValue: Float,  // Value from 0 (too low) to 1 (too high), 0.5 is centered
    modifier: Modifier = Modifier,
    color: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = graphValue,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 100f),
        label = "tuningAnimation"
    )

    //if (min == null) min = 0f

    var containerWidth by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .onSizeChanged { containerWidth = it.width },
            //.background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            // Center line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            // Range indicator
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .background(Color.Green.copy(alpha = 0.5f))
            )

            // Moving sphere
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = ((animatedValue - 0.5f) * containerWidth * 0.8f).toInt(), //Function invocation 'size(...)' expected. // Function invocation 'width(...)' expected.
                            y = 0
                        )
                    }
                    .size(40.dp)
                    .background(
                        color = when {
                            animatedValue in 0.45f..0.55f -> color
                            animatedValue < 0.45f -> color
                            else -> color
                        },
                        shape = CircleShape
                    )
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}