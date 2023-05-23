package com.example.hits_android

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun FunctionSelection(vm: ReorderListViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.07f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(vm.functionsList) { item ->
            Button(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    vm.setCurrentScreenId(item.id)
                }
            ) {
                Text(text = item.name, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    vm.addFunction()

                    vm.setCurrentScreenId(vm.functionsList.size - 1)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}