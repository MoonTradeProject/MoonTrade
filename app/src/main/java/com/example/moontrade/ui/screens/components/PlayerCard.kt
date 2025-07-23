package com.example.moontrade.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.BuildConfig
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PlayerCard(
    entry: LeaderboardEntry,
    medal: String? = null,
    onClick: () -> Unit
) {
    val avatarUrl = if (entry.avatar_url?.startsWith("/") == true) {
        BuildConfig.BASE_URL + entry.avatar_url
    } else {
        entry.avatar_url
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (medal != null) {
                Text(
                    text = medal,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            if (!avatarUrl.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Player Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.avatar_0),
                    contentDescription = "Default Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(entry.username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "ROI: +%.1f%%".format(entry.roi),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (entry.roi >= 0) Color(0xFF00C853) else Color.Red
                )
                if (!entry.description.isNullOrBlank()) {
                    Text(entry.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}