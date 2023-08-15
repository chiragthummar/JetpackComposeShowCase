package com.cb.myshowcase.presentation.ai_art

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.cb.myshowcase.R
import com.cb.myshowcase.common.Screen
import com.cb.myshowcase.common.bounceClickable

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    state: ImageScreenState,
    onEvent: (event: ImageEvent) -> Unit,
    navigateTo: (route: String) -> Unit
) {

    val searchTextFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Home")
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )
    }) {


        Box(
            modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colorScheme.primary)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(24.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 10.dp)
                ) {
                    BasicTextField(
                        value = searchTextFieldValue.value,
                        onValueChange = { newText ->
                            searchTextFieldValue.value = newText
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onTertiaryContainer),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            onEvent(ImageEvent.SearchImage(searchTextFieldValue.value.text))
                        }),
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                        decorationBox = { innerTextField ->
                            TextFieldDefaults.TextFieldDecorationBox(colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                            ), value = searchTextFieldValue.value.text,
                                visualTransformation = VisualTransformation.None,
                                innerTextField = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp)
                                    ) {
                                        innerTextField()
                                    }
                                },
                                trailingIcon = {
                                    Icon(
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "search",
                                        modifier = Modifier.clickable {
                                            if (searchTextFieldValue.value.text.isNotEmpty()) {
                                                focusManager.clearFocus()
                                                onEvent(
                                                    ImageEvent.SearchImage(
                                                        searchTextFieldValue.value.text
                                                    )
                                                )
                                            }
                                        }
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.search_image),
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth(),
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        )
                                    )
                                },
                                singleLine = true,
                                enabled = true,
                                contentPadding = PaddingValues(4.dp),
                                interactionSource = remember { MutableInteractionSource() })
                        },

                        )

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val aiImagesList = state.images
                    if (state.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        if (aiImagesList.isNotEmpty()) {
                            LazyVerticalStaggeredGrid(
                                state = rememberLazyStaggeredGridState(),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                columns = StaggeredGridCells.Fixed(2),
                                verticalItemSpacing = 10.dp,
                                horizontalArrangement = Arrangement.spacedBy(10.dp), content = {
                                    itemsIndexed(aiImagesList, key = { _, item ->
                                        item.src
                                    }) { index, message ->
                                        Card(
                                            modifier = Modifier
                                                .bounceClickable(
                                                    dampingRatio = 0.95f,
                                                    onClick = {
                                                        navigateTo(
                                                            Screen.ImageFullScreen.createRoute(
                                                                index
                                                            )
                                                        )
                                                    },
                                                    shape = RoundedCornerShape(12.dp),
                                                )
                                                .fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                        ) {

                                            SubcomposeAsyncImage(
                                                model = message.srcSmall,
                                                loading = {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(120.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            color = MaterialTheme.colorScheme.primary,
                                                            strokeWidth = 1.dp,
                                                            modifier = Modifier.size(30.dp)
                                                        )
                                                    }
                                                },
                                                contentDescription = "",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                            )
                                        }
                                    }
                                }
                            )
                        } else {
                            if (state.error.isNotEmpty()) {
                                Text(text = state.error, color = Color.White)
                            } else {
                                Text(text = "No Records Found !! ", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}