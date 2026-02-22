package ml.fahimkhan.myapplication.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val MindReaderShapes = Shapes(
    extraSmall  = RoundedCornerShape(4.dp),   // grid cells, chips
    small       = RoundedCornerShape(8.dp),   // small cards, inputs
    medium      = RoundedCornerShape(12.dp),  // step cards
    large       = RoundedCornerShape(20.dp),  // primary buttons, dialogs
    extraLarge  = RoundedCornerShape(28.dp)   // bottom sheets
)
