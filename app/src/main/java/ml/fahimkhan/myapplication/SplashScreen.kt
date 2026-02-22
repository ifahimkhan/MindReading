package ml.fahimkhan.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ml.fahimkhan.myapplication.ui.theme.AncientGold
import ml.fahimkhan.myapplication.ui.theme.CrystalMist
import ml.fahimkhan.myapplication.ui.theme.DeepVoid
import ml.fahimkhan.myapplication.ui.theme.FadedParchment
import ml.fahimkhan.myapplication.ui.theme.MindReaderTheme
import ml.fahimkhan.myapplication.ui.theme.MysticViolet
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindReaderTheme {
                SplashContent(
                    onNavigate = {
                        startActivity(Intent(this, QuestionActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

// Compact holder – allocated once inside remember {}
private class StarData(val x: Float, val y: Float, val radius: Float, val phase: Float)

@Composable
private fun SplashContent(onNavigate: () -> Unit) {
    // In preview: start in the fully-visible state so the screen is useful
    val isPreview = LocalInspectionMode.current
    var started by remember { mutableStateOf(isPreview) }

    LaunchedEffect(Unit) {
        if (!isPreview) {
            started = true
            delay(2800)
            onNavigate()
        }
    }

    // ── Logo animations ──────────────────────────────────────────────────────
    val logoAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "logoAlpha"
    )
    val logoScale by animateFloatAsState(
        targetValue = if (started) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 180f),
        label = "logoScale"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "titleAlpha"
    )

    // ── Credits blink ────────────────────────────────────────────────────────
    val blink = rememberInfiniteTransition(label = "creditsBlink")
    val blinkAlpha by blink.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "blinkAlpha"
    )

    // ── Starfield – allocated once, never reallocated ────────────────────────
    val stars = remember {
        List(80) {
            StarData(
                x      = Random.nextFloat(),
                y      = Random.nextFloat(),
                radius = Random.nextFloat() * 1.4f + 0.4f,
                phase  = Random.nextFloat() * (2f * PI.toFloat())
            )
        }
    }
    val twinkleTx = rememberInfiniteTransition(label = "twinkle")
    val twinklePhase by twinkleTx.animateFloat(
        initialValue = 0f,
        targetValue  = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "twinklePhase"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepVoid),
        contentAlignment = Alignment.Center
    ) {
        // Starfield canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                val a = (sin((twinklePhase + star.phase).toDouble()) * 0.35 + 0.65)
                    .toFloat().coerceIn(0.2f, 1f)
                drawCircle(
                    color  = Color.White.copy(alpha = a * 0.55f),
                    radius = star.radius * density,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        // Violet radial glow behind the logo
        Canvas(
            modifier = Modifier
                .size(280.dp)
                .graphicsLayer { alpha = logoAlpha }
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(MysticViolet.copy(alpha = 0.22f), Color.Transparent),
                    center = center,
                    radius = size.minDimension / 2f
                )
            )
        }

        // Logo + title
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha  = logoAlpha
                scaleX = logoScale
                scaleY = logoScale
            }
        ) {
            Image(
                painter            = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = "Mind Reader logo",
                modifier           = Modifier.size(130.dp)
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text  = "MIND READER",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily    = FontFamily.Serif,
                    letterSpacing = 8.sp
                ),
                color = AncientGold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text  = "C A N  Y O U  K E E P  A  S E C R E T ?",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                color = FadedParchment,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
        }

        // Developer credit – bottom centre, blinking
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            HorizontalDivider(modifier = Modifier.width(36.dp), color = CrystalMist)
            Spacer(Modifier.height(8.dp))
            Text(
                text      = "Developed by\nMohammed Fahim Khan",
                style     = MaterialTheme.typography.labelSmall,
                color     = FadedParchment.copy(alpha = blinkAlpha),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(
    name            = "Splash Screen",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun SplashPreview() {
    MindReaderTheme {
        SplashContent(onNavigate = {})
    }
}
