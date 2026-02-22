package ml.fahimkhan.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.fahimkhan.myapplication.ui.GoldButton
import ml.fahimkhan.myapplication.ui.theme.AncientGold
import ml.fahimkhan.myapplication.ui.theme.CrystalMist
import ml.fahimkhan.myapplication.ui.theme.DeepVoid
import ml.fahimkhan.myapplication.ui.theme.FadedParchment
import ml.fahimkhan.myapplication.ui.theme.GoldContainer
import ml.fahimkhan.myapplication.ui.theme.MindReaderTheme
import ml.fahimkhan.myapplication.ui.theme.MysticViolet
import ml.fahimkhan.myapplication.ui.theme.NightSurface
import ml.fahimkhan.myapplication.ui.theme.ParchmentWhite

class QuestionActivity : ComponentActivity() {

    private lateinit var updateHelper: UpdateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateHelper = UpdateHelper(this)
        updateHelper.checkForUpdate()

        setContent {
            MindReaderTheme {
                QuestionContent(
                    onReady = { startActivity(Intent(this, MainActivity::class.java)) }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateHelper.resumeUpdateIfInterrupted()
    }
}

@Composable
private fun QuestionContent(onReady: () -> Unit) {
    val context     = LocalContext.current
    val scope       = rememberCoroutineScope()
    val isPreview   = LocalInspectionMode.current

    var backPressedOnce by remember { mutableStateOf(false) }

    BackHandler {
        if (backPressedOnce) {
            (context as? ComponentActivity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            scope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    // In preview: start all cards visible; at runtime: stagger via LaunchedEffect
    var showCard1  by remember { mutableStateOf(isPreview) }
    var showCard2  by remember { mutableStateOf(isPreview) }
    var showCard3  by remember { mutableStateOf(isPreview) }
    var showButton by remember { mutableStateOf(isPreview) }

    LaunchedEffect(Unit) {
        if (!isPreview) {
            showCard1 = true
            delay(400); showCard2 = true
            delay(400); showCard3 = true
            delay(300); showButton = true
        }
    }

    val slideEnter = fadeIn(tween(500)) +
            slideInHorizontally(tween(500)) { -it }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text      = "HOW TO PLAY",
                style     = MaterialTheme.typography.headlineMedium,
                color     = AncientGold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text      = "Follow the steps below, then let us read your mind.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = FadedParchment,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            AnimatedVisibility(visible = showCard1, enter = slideEnter) {
                StepCard(
                    step        = 1,
                    title       = "Think of a two-digit number",
                    body        = "Pick any number from 10 to 99 and hold it clearly in your mind.",
                    accentColor = AncientGold
                )
            }
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(visible = showCard2, enter = slideEnter) {
                StepCard(
                    step        = 2,
                    title       = "Do the calculation",
                    body        = "Add both digits together, then subtract that sum from your original number.\n\nExample:  32  →  3+2 = 5  →  32−5 = 27",
                    accentColor = MysticViolet
                )
            }
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(visible = showCard3, enter = slideEnter) {
                StepCard(
                    step        = 3,
                    title       = "Find your symbol",
                    body        = "Look up your result on the symbol grid and focus on that symbol.",
                    accentColor = AncientGold
                )
            }

            Spacer(Modifier.height(40.dp))

            AnimatedVisibility(visible = showButton, enter = fadeIn(tween(600))) {
                GoldButton(
                    text     = "I'M  READY",
                    onClick  = onReady,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun StepCard(
    step: Int,
    title: String,
    body: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(NightSurface)
            .border(1.dp, CrystalMist, MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(GoldContainer)
                .border(1.5.dp, accentColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = "$step",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize      = 14.sp,
                    letterSpacing = 0.sp
                ),
                color = accentColor
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleMedium,
                color = ParchmentWhite
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text  = body,
                style = MaterialTheme.typography.bodyMedium,
                color = FadedParchment
            )
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(
    name            = "Question Screen",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun QuestionPreview() {
    MindReaderTheme {
        QuestionContent(onReady = {})
    }
}
