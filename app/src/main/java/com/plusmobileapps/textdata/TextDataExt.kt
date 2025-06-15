package com.plusmobileapps.textdata

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.plusmobileapps.text.TextData

@Composable
fun TextData.evaluate(): String = this.evaluate(LocalContext.current)