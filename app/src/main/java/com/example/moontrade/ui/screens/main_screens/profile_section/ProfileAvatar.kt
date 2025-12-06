package com.example.moontrade.ui.screens.main_screens.profile_section
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moontrade.ui.screens.components.glasskit.resolveAvatarRes

@Composable
fun ProfileAvatar(
    avatarUrl: String?,
    avatarId: Int,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 350f
        ),
        label = "avatarPressScale"
    )

    Box(
        modifier = modifier
            .size(200.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { pressed = true; true }
                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> { pressed = false; true }
                    else -> false
                }
            }
            .background(
                Brush.radialGradient(
                    listOf(
                        cs.primary.copy(alpha = 1f),
                        Color.Transparent
                    )
                ),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, cs.primary, CircleShape)
                .background(Color.Transparent)
        ) {
            if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Image(
                    painter = painterResource(resolveAvatarRes(avatarId)),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}
