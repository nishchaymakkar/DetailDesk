package com.sampleproductapp.detaildesk.ui.screens.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import coil.network.HttpException
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.local.ProductEntity
import com.sampleproductapp.detaildesk.modal.mappers.toProduct
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    pager: Pager<Int, ProductEntity>
) : ViewModel() {
    val productPagingFlow = pager
        .flow
        .map {pagingData ->
            pagingData.map { it.toProduct() }
        }
        .cachedIn(viewModelScope)
}


