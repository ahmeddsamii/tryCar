package com.example.presentation.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun NoConnection(onClickRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_no_connection),
            contentDescription = "No Connection icon",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Oops! It looks like you’re offline.",
            fontSize = 22.sp,
            color = Color.Black,
        )

        Text(
            text = "Check your connection and try again",
            fontSize = 18.sp,
            color = Color.LightGray,
        )

        Button(
            onClick = onClickRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry")
        }
    }
}