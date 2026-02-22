package ml.fahimkhan.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MysticDarkColorScheme = darkColorScheme(
    primary                 = AncientGold,
    onPrimary               = OnGoldContainer,
    primaryContainer        = GoldContainer,
    onPrimaryContainer      = OnGoldContainer,

    secondary               = MysticViolet,
    onSecondary             = OnVioletContainer,
    secondaryContainer      = VioletContainer,
    onSecondaryContainer    = OnVioletContainer,

    tertiary                = EtherealTeal,
    onTertiary              = OnTealContainer,
    tertiaryContainer       = TealContainer,
    onTertiaryContainer     = OnTealContainer,

    background              = DeepVoid,
    onBackground            = ParchmentWhite,

    surface                 = NightSurface,
    onSurface               = ParchmentWhite,
    surfaceVariant          = TwilightCard,
    onSurfaceVariant        = FadedParchment,

    outline                 = CrystalMist,
    outlineVariant          = CrystalMist.copy(alpha = 0.5f),

    error                   = MysticError,
    onError                 = OnError,
    errorContainer          = MysticErrorContainer,
    onErrorContainer        = MysticError
)

@Composable
fun MindReaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MysticDarkColorScheme,
        typography  = MindReaderTypography,
        shapes      = MindReaderShapes,
        content     = content
    )
}
