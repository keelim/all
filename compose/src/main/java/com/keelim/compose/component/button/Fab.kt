package com.keelim.compose.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FabScreen() {
    Row {
        SmallFAB()
        Spacer(modifier = Modifier.width(8.dp))
        MediumFAB()
        Spacer(modifier = Modifier.width(8.dp))
        ExtendedFAB()
    }
}

@Composable
fun MediumFAB() {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add FAB",
            tint = Color.White,
        )
    }
}

@Composable
fun SmallFAB() {
    SmallFloatingActionButton(
        onClick = { },
        containerColor = Color.Black,
        shape = RoundedCornerShape(12.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Location FAB",
            tint = Color.White,
        )
    }
}

@Composable
fun ExtendedFAB() {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "Navigate", color = Color.White)
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Navigate FAB",
                tint = Color.White,
            )
        },
        onClick = { },
        expanded = true,
        containerColor = Color.Green,
    )
}

@Preview(showBackground = true)
@Composable
private fun FabScreenPreview() {
    FabScreen()
}
