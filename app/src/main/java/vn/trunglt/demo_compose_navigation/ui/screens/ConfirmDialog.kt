package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    btnConfirm: String,
    btnCancel: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(text = title, style = TextStyle(fontSize = TextUnit(20f, TextUnitType.Sp)))
        Text(text = content)
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                onCancel.invoke()
            }, modifier = Modifier.weight(1f)) {
                Text(text = btnCancel)
            }
            Button(
                onClick = {
                    onConfirm.invoke()
                }, modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(text = btnConfirm)
            }
        }
    }
}