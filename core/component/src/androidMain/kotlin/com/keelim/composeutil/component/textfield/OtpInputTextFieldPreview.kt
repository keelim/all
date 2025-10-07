package com.keelim.composeutil.component.textfield

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun PreviewOtpInputTextField() {
    OtpInputTextField(otp = Otp(value = "alienum"), onOtpValueChange = {})
}

