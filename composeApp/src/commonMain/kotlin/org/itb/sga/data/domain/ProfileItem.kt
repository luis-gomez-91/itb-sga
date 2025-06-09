package org.itb.sga.data.domain

import androidx.compose.ui.graphics.Color

data class ProfileItem(
    val text: String,
    val onclik: () -> Unit,
    val bgColor: Color,
    val textColor: Color
)