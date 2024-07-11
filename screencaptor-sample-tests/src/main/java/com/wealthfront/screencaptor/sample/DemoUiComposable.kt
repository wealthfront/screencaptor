package com.wealthfront.screencaptor.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wealthfront.screencaptor.sample.test.R

@Composable
fun DemoUI(showDialogButton: Boolean = false) {
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .testTag("DemoUIRoot")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(painter = painterResource(R.drawable.add_accounts), "")
                Text(
                    text = "Welcome to wealthfront",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Some sample data which is really long, so long that it wraps to another line and maybe even three lines",
                    style = MaterialTheme.typography.body2
                )
                Divider()
                Text(text = "Bulldog")
                Text(text = "Corgi")
                Text(text = "Mastiff")
                Text(text = "Labrador Retriever")
                if (showDialogButton) {
                    Button(onClick = { showBottomSheet.value = true }) {
                        Text("Add more")
                    }
                }
            }

            if (showBottomSheet.value) {
                ModalBottomSheet(onDismissRequest = { showBottomSheet.value = false }) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Add more types",
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(text = "German Shepherd")
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(text = "Alaskan Malamute")
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(text = "Rottweiler")
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(text = "Beagle")
                        }
                    }
                }
            }
        }
    }
}