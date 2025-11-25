package com.example.moontrade.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600
import kotlinx.coroutines.launch

@Composable
private fun OnboardingDots(
    total: Int,
    selected: Int
) {
    val cs = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selected) 10.dp else 6.dp)
                    .background(
                        cs.primary.copy(alpha = if (index == selected) 1f else 0.35f),
                        CircleShape
                    )
            )
        }
    }
}
@Composable
private fun OnboardingPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val dark = cs.background.luminance() < 0.5f

    val gradient = Brush.horizontalGradient(
        if (dark)
            listOf(Violet600, Violet300.copy(alpha = 0.9f), Violet600)
        else
            listOf(Violet600, Violet600.copy(alpha = 0.7f), Violet600)
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = cs.onPrimary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPagerScreen(
    onFinished: () -> Unit,
    onSkip: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val pageCount = 3
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = cs.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "MoonTrade",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Violet600,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    when (page) {
                        0 -> {
                            Text(
                                "Real-Market Trading",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Simulate live bid/ask execution\nwith zero financial risk.",
                                style = MaterialTheme.typography.titleMedium,
                                color = cs.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }

                        1 -> {
                            Text(
                                "Compete & Improve",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Join leaderboards and tournaments\n" +
                                        "to hone your trading edge.",
                                style = MaterialTheme.typography.titleMedium,
                                color = cs.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }

                        2 -> {
                            Text(
                                "Learn & Start",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Build confidence in forex, crypto and stocks\nbefore risking real capital.",
                                style = MaterialTheme.typography.titleMedium,
                                color = cs.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                OnboardingDots(total = pageCount, selected = pagerState.currentPage)

                Spacer(Modifier.height(24.dp))

                OnboardingPrimaryButton(
                    text = if (pagerState.currentPage == pageCount - 1) "Get Started" else "Next",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (pagerState.currentPage < pageCount - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinished()
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = onSkip) {
                    Text("Skip for now", color = cs.onSurface)
                }
            }
        }
    }
}
