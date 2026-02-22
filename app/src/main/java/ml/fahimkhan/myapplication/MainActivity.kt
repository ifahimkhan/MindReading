package ml.fahimkhan.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ml.fahimkhan.myapplication.ui.GoldButton
import ml.fahimkhan.myapplication.ui.theme.AncientGold
import ml.fahimkhan.myapplication.ui.theme.CrystalMist
import ml.fahimkhan.myapplication.ui.theme.DeepVoid
import ml.fahimkhan.myapplication.ui.theme.FadedParchment
import ml.fahimkhan.myapplication.ui.theme.MindReaderTheme
import ml.fahimkhan.myapplication.ui.theme.NightSurface
import ml.fahimkhan.myapplication.ui.theme.OnVioletContainer
import ml.fahimkhan.myapplication.ui.theme.ParchmentWhite
import ml.fahimkhan.myapplication.ui.theme.TwilightCard
import ml.fahimkhan.myapplication.viewmodel.GridItem
import ml.fahimkhan.myapplication.viewmodel.GridUiState
import ml.fahimkhan.myapplication.viewmodel.GridViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindReaderTheme {
                GridScreen(
                    onReveal = { magicIndex, images, hasSelection ->
                        startActivity(
                            Intent(this, AnswerActivity::class.java)
                                .putExtra("random", magicIndex)
                                .putExtra("images", images)
                                .putExtra("show", hasSelection)
                        )
                    }
                )
            }
        }
    }
}

// ── Stateful entry point (ViewModel-connected) ────────────────────────────────

@Composable
private fun GridScreen(
    onReveal: (magicIndex: Int, images: IntArray, hasSelection: Boolean) -> Unit,
    gridViewModel: GridViewModel = viewModel()
) {
    val uiState by gridViewModel.uiState.collectAsStateWithLifecycle()

    // Shuffle every time the screen resumes — mirrors original onResume behaviour
    LifecycleResumeEffect(Unit) {
        gridViewModel.shuffle()
        onPauseOrDispose { /* nothing */ }
    }

    GridScreenContent(
        uiState      = uiState,
        onItemSelect = { gridViewModel.selectItem(it) },
        onReveal     = {
            onReveal(
                gridViewModel.magicIndex,
                GridViewModel.IMAGE_RES,
                uiState.selectedIndex != null
            )
        }
    )
}

// ── Pure stateless UI – also used by the preview ──────────────────────────────

@Composable
private fun GridScreenContent(
    uiState: GridUiState,
    onItemSelect: (Int) -> Unit,
    onReveal: () -> Unit
) {
    val isRevealEnabled = uiState.selectedIndex != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text      = "CHOOSE YOUR SYMBOL",
                style     = MaterialTheme.typography.headlineSmall,
                color     = AncientGold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text      = "Tap the symbol at your result number",
                style     = MaterialTheme.typography.bodyMedium,
                color     = FadedParchment,
                textAlign = TextAlign.Center
            )
        }

        LazyVerticalGrid(
            columns             = GridCells.Fixed(7),
            modifier            = Modifier.weight(1f),
            contentPadding      = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalArrangement   = Arrangement.spacedBy(3.dp)
        ) {
            items(
                count = uiState.items.size,
                key   = { index -> uiState.items[index].index }   // stable keys
            ) { index ->
                val item = uiState.items[index]
                GridCell(
                    item       = item,
                    isSelected = uiState.selectedIndex == item.index,
                    onSelect   = { onItemSelect(item.index) }
                )
            }
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            GoldButton(
                text    = if (isRevealEnabled) "REVEAL  MY  SYMBOL"
                          else "SELECT  A  SYMBOL  FIRST",
                onClick = onReveal,
                enabled = isRevealEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Grid cell ─────────────────────────────────────────────────────────────────

@Composable
private fun GridCell(
    item: GridItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue   = if (isSelected) 1.10f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 400f),
        label         = "cellScale_${item.index}"
    )
    val borderColor by animateColorAsState(
        targetValue   = if (isSelected) AncientGold else Color.Transparent,
        animationSpec = tween(200),
        label         = "borderColor_${item.index}"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(MaterialTheme.shapes.extraSmall)
            .background(if (isSelected) ParchmentWhite else NightSurface)
            .border(1.5.dp, borderColor, MaterialTheme.shapes.extraSmall)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isSelected) {
                Image(
                    painter            = painterResource(item.imageRes),
                    contentDescription = "Symbol ${item.index}",
                    modifier           = Modifier.size(26.dp)
                )
            } else {
                Icon(
                    painter            = painterResource(R.drawable.ic_action_name),
                    contentDescription = null,
                    modifier           = Modifier.size(20.dp),
                    tint               = ParchmentWhite
                )
            }
            Text(
                text     = "${item.index}",
                style    = MaterialTheme.typography.labelSmall,
                color    = if (isSelected) AncientGold else FadedParchment,
                maxLines = 1
            )
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(
    name            = "Grid – nothing selected",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun GridPreviewEmpty() {
    MindReaderTheme {
        GridScreenContent(
            uiState = GridUiState(
                items = List(GridViewModel.GRID_SIZE) { i ->
                    GridItem(index = i, imageRes = R.mipmap.image0)
                },
                selectedIndex = null
            ),
            onItemSelect = {},
            onReveal = {}
        )
    }
}

@Preview(
    name            = "Grid – item 27 selected",
    showSystemUi    = true,
    showBackground  = true,
    backgroundColor = 0xFF080812
)
@Composable
private fun GridPreviewSelected() {
    MindReaderTheme {
        GridScreenContent(
            uiState = GridUiState(
                items = List(GridViewModel.GRID_SIZE) { i ->
                    GridItem(
                        index    = i,
                        imageRes = if (i % 9 == 0) R.mipmap.image3 else R.mipmap.image0
                    )
                },
                selectedIndex = 27
            ),
            onItemSelect = {},
            onReveal = {}
        )
    }
}
