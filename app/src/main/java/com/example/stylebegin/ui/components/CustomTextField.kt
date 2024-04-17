package com.example.stylebegin.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylebegin.ui.theme.ShadeWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    onValueChange: (String) -> Unit = {},
    hint: String,
    value: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textStyle = TextStyle(color = Color.White)
        ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor =  Color.White,
            unfocusedIndicatorColor =  Color.White,
        ),
        visualTransformation = (if (isPassword) PasswordVisualTransformation() else VisualTransformation.None),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)

    )
}
