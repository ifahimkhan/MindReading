package ml.fahimkhan.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ml.fahimkhan.myapplication.ui.GoldButton
import ml.fahimkhan.myapplication.ui.theme.AncientGold
import ml.fahimkhan.myapplication.ui.theme.AncientGoldDim
import ml.fahimkhan.myapplication.ui.theme.DeepVoid
import ml.fahimkhan.myapplication.ui.theme.FadedParchment
import ml.fahimkhan.myapplication.ui.theme.MindReaderTheme
import ml.fahimkhan.myapplication.ui.theme.MysticViolet
import ml.fahimkhan.myapplication.ui.theme.ParchmentWhite

class AnswerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val magicIndex   = intent?.getIntExtra("random", 0) ?: 0
        val images       = intent?.getIntArrayExtra("images")
        val hasSelection = intent?.getBooleanExtra("show", false) ?: false

        setContent {
            MindReaderTheme {
                AnswerContent(
                    magicIndex   = magicIndex,
                    images       = images,
                    hasSelection = hasSelection,
                    onExit       = { finishAffinity() },
                    onPlayAgain  = { finish() }
                )
            }
        }
    }
}

@Composable
private fun AnswerContent(
    magicIndex: Int,
    images: IntArray?,
    hasSelection: Boolean,
    onExit: () -> Unit,
    onPlayAgain: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepVoid),
        contentAlignment = Alignment.Center
    ) {
        if (!hasSelection || images == null) {
            ConfusedState(onPlayAgain = onPlayAgain, onExit = onExit)
        } else {
            RevealState(
                imageRes    = images[magicIndex],
                onPlayAgain = onPlayAgain,
                onExit      = onExit
            )
        }
    }
}

// ── Reveal state ──────────────────────────────────────────────────────────────

@Composable
private fun RevealState(
    imageRes: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    // In preview: jump straight to the revealed state
    var showWaiting by remember { mutableStateOf(!isPreview) }
    var showAnswer  by remember { mutableStateOf(isPreview) }

    LaunchedEffect(Unit) {
        if (!isPreview) {
            delay(1800)
            showWaiting = false
            delay(300)
            showAnswer = true
        }
    }

    val glowScale by animateFloatAsState(
        targetValue   = if (showAnswer) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = 120f
        ),
        label = "glowScale"
    )

    val glowBrush = remember {
        Brush.radialGradient(
            colors = listOf(
                MysticViolet.copy(alpha = 0.45f),
                AncientGoldDim.copy(alpha = 0.15f),
                DeepVoid.copy(alpha = 0f)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = showWaiting,
            enter   = fadeIn(tween(600)),
            exit    = fadeOut(tween(500))
        ) {
            Text(
                text  = "FOCUS ON YOUR SYMBOL…",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily    = FontFamily.Serif,
                    letterSpacing = 3.sp
                ),
                color     = FadedParchment,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(
            visible = showAnswer,
            enter   = scaleIn(spring(dampingRatio = 0.6f, stiffness = 150f)) +
                      fadeIn(tween(500))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text  = "YOUR SYMBOL IS…",
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 3.sp),
                    color     = AncientGold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(ParchmentWhite)
                        .graphicsLayer { scaleX = glowScale; scaleY = glowScale }
                        .drawBehind { drawRect(brush = glowBrush) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter            = painterResource(imageRes),
                        contentDescription = "Your revealed symbol",
                        modifier           = Modifier
                            .size(140.dp)
                            .background(ParchmentWhite)
                    )
                }

                Spacer(Modifier.height(48.dp))

                Text(
                    text  = "✨  Is this your symbol?  ✨",
                    style = MaterialTheme.typography.titleMedium,
                    color     = ParchmentWhite,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (showAnswer) {
            Spacer(Modifier.height(48.dp))

            GoldButton(
                text     = "PLAY  AGAIN",
                onClick  = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text  = "EXIT",
                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                color = FadedParchment,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(onClick = onExit)
                    .padding(16.dp)
            )
        }
    }
}

// ── Confused state ────────────────────────────────────────────────────────────

@Composable
private fun ConfusedState(
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text  = "🤔",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text  = "Hmm, are you confused?",
            style = MaterialTheme.typography.headlineSmall,
            color     = AncientGold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text  = "Go back and tap a symbol from the grid, then press Reveal.",
            style = MaterialTheme.typography.bodyMedium,
            color     = FadedParchment,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(40.dp))
        GoldButton(
            text     = "TRY  AGAIN",
            onClick  = onPlayAgain,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text  = "EXIT",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
            color = FadedParchment,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(onClick = onExit)
                .padding(16.dp)
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(
    name            = "Answer – symbol revealed",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun AnswerRevealPreview() {
    MindReaderTheme {
        AnswerContent(
            magicIndex   = 3,
            images       = intArrayOf(
                R.mipmap.image0, R.mipmap.image1, R.mipmap.image2,
                R.mipmap.image3, R.mipmap.image4, R.mipmap.image5
            ),
            hasSelection = true,
            onExit       = {},
            onPlayAgain  = {}
        )
    }
}

@Preview(
    name            = "Answer – confused (no selection)",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun AnswerConfusedPreview() {
    MindReaderTheme {
        AnswerContent(
            magicIndex   = 0,
            images       = null,
            hasSelection = false,
            onExit       = {},
            onPlayAgain  = {}
        )
    }
}
