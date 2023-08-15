package com.cb.myshowcase.presentation.ai_art


import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.cb.myshowcase.R
import com.cb.myshowcase.common.Constants
import com.google.android.material.math.MathUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageFullScreen(
    state: ImageScreenState,
    onEvent: (event: ImageEvent) -> Unit,
    position: Int,
    application: Application,
    onBack: () -> Unit
) {

    val list = state.images
    val pagerState = rememberPagerState(
        initialPage = position,
        initialPageOffsetFraction = 0f
    ) {
        list.size
    }
    val context = LocalContext.current

    val path = File(application.externalMediaDirs[0].toString() + "/" + Constants.FOLDER_NAME)
    val imageFile =
        File(path, "${FilenameUtils.getName(list[pagerState.currentPage].id)}.jpg")
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .navigationBarsPadding()

        ) {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "AI Art",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ), navigationIcon = {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = false,
                                radius = 22.dp,
                                color = Color.White
                            )
                        ) {
                            onBack()
                        },
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "back",
                    tint = Color.White
                )
            })

            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxSize()
                    .weight(1f)
            ) {
                HorizontalPager(
                    state = pagerState, modifier = Modifier.fillMaxSize()
                ) { page ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset =
                                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                                // We animate the alpha, between 50% and 100%
                                alpha = MathUtils.lerp(
                                    0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }

                    ) {
                        val painter =
                            rememberAsyncImagePainter(model = list[page].srcSmall)
                        val imageState = painter.state

                        if (imageState is AsyncImagePainter.State.Loading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(40.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        Image(
                            painter = painter,
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxSize()
                        )

                        /*Image(
                            contentDescription = "dd",
                            painter = painterResource(R.drawable.gradient_bg),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )*/
                    }
                }
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 15.dp)
                        .align(Alignment.BottomEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp)
                            .clickable {
                                if (imageFile.exists()) {
                                    onEvent(
                                        ImageEvent.ShareImage(
                                            context, imageFile
                                        )
                                    )
                                } else {
                                    if (state.isDownloading) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Downloading..",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    } else {
                                        onEvent(
                                            ImageEvent.DownloadImage(
                                                context = context,
                                                url = list[pagerState.currentPage].src
                                            )
                                        )
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .size(60.dp)
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                        )
                        if (state.isDownloading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                painter = if (imageFile.exists()) {
                                    painterResource(R.drawable.share_2)
                                } else {
                                    painterResource(R.drawable.download)
                                },
                                contentDescription = "",
                                modifier = Modifier.size(22.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}