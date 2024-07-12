package com.ezhilan.cine.presentation.screens.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import com.ezhilan.cine.R
import com.ezhilan.cine.presentation.config.spacing
import com.ezhilan.cine.presentation.config.textStyle

@Composable
fun HorizontalListContainer(
    modifier: Modifier = Modifier,
    label: String,
    onViewAllClick: () -> Unit,
    listView: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.spacing.grid1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.textStyle.defaultTitle)
        TextButton(onClick = onViewAllClick) {
            Text(
                text = "View all",
                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
            )
            Spacer(modifier = modifier.width(MaterialTheme.spacing.grid05))
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
            )
        }
    }
    listView()
    Spacer(modifier = modifier.height(MaterialTheme.spacing.grid1))
}