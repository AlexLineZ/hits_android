package com.example.hits_android

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun FunctionSelection(vm: ReorderListViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.08f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items(vm.functionsList) { item ->
            FunctionListItem(vm, item)
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    vm.addFunction()

                    vm.setCurrentScreenId(vm.functionsList.size - 1)

                    Log.d("a", "${vm.functionsList}")
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "addButton",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun FunctionListItem(vm: ReorderListViewModel, item: FunctionClass) {

    when (item.id) {
        0 -> {
            Button(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    vm.setCurrentScreenId(item.id)
                }
            ) {
                Text(
                    text = "main",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        else -> {
            Button(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    vm.setCurrentScreenId(item.id)
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "function",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "closeFunIcon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .clickable {
                                vm.setCurrentScreenId(0)

                                vm.functionsList.forEach{
                                    Log.d("a", "${it.id}")
                                }
                                Log.d("a", "${item.id}")



                                vm.functionsList =
                                    vm.functionsList.toMutableList().apply {
                                        removeIf { it.id == item.id }
                                    }
                            }
                    )
                }
            }
        }
    }
}