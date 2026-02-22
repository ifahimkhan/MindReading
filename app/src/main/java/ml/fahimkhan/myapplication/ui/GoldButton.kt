package ml.fahimkhan.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ml.fahimkhan.myapplication.ui.theme.AncientGold
import ml.fahimkhan.myapplication.ui.theme.AncientGoldBright
import ml.fahimkhan.myapplication.ui.theme.AncientGoldDim
import ml.fahimkhan.myapplication.ui.theme.DeepVoid
import ml.fahimkhan.myapplication.ui.theme.FadedParchment
import ml.fahimkhan.myapplication.ui.theme.MindReaderTheme
import ml.fahimkhan.myapplication.ui.theme.TwilightCard

@Composable
fun GoldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Brushes created once – not reallocated on every recomposition
    val enabledBrush = remember {
        Brush.horizontalGradient(
            colors = listOf(AncientGoldDim, AncientGold, AncientGoldBright, AncientGold)
        )
    }
    val disabledBrush = remember {
        Brush.horizontalGradient(colors = listOf(TwilightCard, TwilightCard))
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(MaterialTheme.shapes.large)
            .background(if (enabled) enabledBrush else disabledBrush)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 3.sp),
            color = if (enabled) DeepVoid else FadedParchment
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "GoldButton states", showBackground = true, backgroundColor = 0xFF080812)
@Composable
private fun GoldButtonPreview() {
    MindReaderTheme {
        Column(
            modifier            = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoldButton(
                text     = "I'M  READY",
                onClick  = {},
                modifier = Modifier.fillMaxWidth()
            )
            GoldButton(
                text     = "SELECT  A  SYMBOL  FIRST",
                onClick  = {},
                enabled  = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
