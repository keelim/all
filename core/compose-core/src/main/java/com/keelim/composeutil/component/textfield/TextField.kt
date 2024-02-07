package com.keelim.composeutil.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space2

@Stable
@JvmInline
value class Otp(val value: String)

@Composable
fun OtpInputTextField(
    otp: Otp,
    onOtpValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = otp.value,
        onValueChange = onOtpValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(4) { index ->
                val number =
                    when {
                        index >= otp.value.length -> ""
                        else -> otp.value[index].toString()
                    }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(space2)
                            .background(Color.Black),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOtpInputTextField() {
    OtpInputTextField(otp = Otp(value = "alienum"), onOtpValueChange = {})
}
