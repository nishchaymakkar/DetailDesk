@file:OptIn(ExperimentalMaterial3Api::class)

package com.sampleproductapp.detaildesk.ui.screens.homescreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.ui.FAB_EXPLODE_BOUNDS_KEY


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    modifier: Modifier = Modifier,
    navigatePS: () -> Unit,
    addProduct: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    animatedVisiblityScope: AnimatedVisibilityScope
) {
    val products = viewModel.productPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(key1 = products.loadState) {
        if(products.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (products.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
Scaffold (
    topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            title = {},
            actions = {
                IconButton(
                    onClick = {navigatePS()},
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                }
            }
        )
    },
    floatingActionButton = {
        FloatingActionButton(
            onClick = { addProduct()},
            modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = FAB_EXPLODE_BOUNDS_KEY
                ),
                animatedVisibilityScope = animatedVisiblityScope
            )
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null
            )
        }
    }

){innerpadding ->
    Box(modifier.fillMaxSize().padding(innerpadding)){
        if (products.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier.align(Alignment.Center)
            )
        }else {

            LazyColumn(
                modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = products.itemCount){index ->
                    val item = products[index]
                    item?.let { ProductItem(product = it) }

                }

                item {
                    if (products.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }


        }
    }
}

}





@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier) {


val byteArray = product.productImage.toBitmap()

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(16 / 9f),
                content = {


                        Image(bitmap = byteArray!!.asImageBitmap(),
                            modifier = modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null)


                    Row(
                        modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onPrimary),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        if (product.productName != null) {
                            Text(
                                text = product.productName ?: "unknown",
                                modifier.align(Alignment.CenterVertically),
                                fontSize = 12.sp
                            )
                        }
                        if (product.createdAt != null) {
                            Text(
                                text = product.createdAt ?: "unkown",
                                modifier.align(Alignment.CenterVertically),
                            )

                        }
                    }
                })
    }
}
fun String.toByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun String.toBitmap(): Bitmap? {
    val byteArray = Base64.decode(this, Base64.DEFAULT)
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true // Check dimensions only
    }
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

    // Calculate inSampleSize for downsampling
    options.inSampleSize = calculateInSampleSize(options, 800, 800)
    options.inJustDecodeBounds = false // Decode the actual bitmap

    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
