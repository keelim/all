package com.keelim.composeutil.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import com.keelim.composeutil.component.kui.KuiExtendedFloatingActionButton
import com.keelim.composeutil.component.kui.KuiFloatingActionButton
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiSmallFloatingActionButton
import com.keelim.composeutil.component.kui.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space8

@Composable
fun FabScreen() {
    Row {
        SmallFAB()
        Spacer(modifier = Modifier.width(space8))
        MediumFAB()
        Spacer(modifier = Modifier.width(space8))
        ExtendedFAB()
    }
}

@Composable
fun MediumFAB() {
    KuiFloatingActionButton(onClick = { /*TODO*/ }) {
        KuiIcon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add FAB",
            tint = Color.White,
        )
    }
}

@Composable
fun SmallFAB() {
    KuiSmallFloatingActionButton(
        onClick = { },
        containerColor = Color.Black,
        shape = RoundedCornerShape(space12),
    ) {
        KuiIcon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Location FAB",
            tint = Color.White,
        )
    }
}

@Composable
fun ExtendedFAB() {
    KuiExtendedFloatingActionButton(
        text = {
            KuiText(text = "Navigate", color = Color.White)
        },
        icon = {
            KuiIcon(
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
