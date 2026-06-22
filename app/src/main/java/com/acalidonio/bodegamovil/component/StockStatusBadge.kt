package com.acalidonio.bodegamovil.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acalidonio.bodegamovil.model.StockStatus

@Composable
fun StockStatusBadge(status: StockStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        StockStatus.AVAILABLE -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Disponible")
        StockStatus.LOW_STOCK -> Triple(Color(0xFFFFF8E1), Color(0xFFF57F17), "Poco Stock")
        StockStatus.OUT_OF_STOCK -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Agotado")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}