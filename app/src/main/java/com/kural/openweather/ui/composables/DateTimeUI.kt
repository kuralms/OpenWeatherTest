package com.kural.openweather.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

var dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
var timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss z", Locale.getDefault())

// Format current date and time
var currentDateString: String? = dateFormat.format(Date())
var currentTimeString: String? = timeFormat.format(Date())

@Composable
fun TimeStampView(){
    Column (
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Last Check-in $currentDateString",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = "$currentTimeString",
            style = MaterialTheme.typography.bodySmall
        )
    }
}