package com.sampleproductapp.detaildesk.ui.screens.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.network.HttpException
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val detailDeskRepository: DetailDeskRepository
) : ViewModel() {
    var currentPage = 1
    val pageSize = 10
    var productUiState: ProductUiState by mutableStateOf(ProductUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            getProductsFromApi()
        }
    }

    fun getProductsFromApi() {
        viewModelScope.launch {
            productUiState = ProductUiState.Loading
            try {
                val result = detailDeskRepository.getAllProducts()
                val filteredProducts = result.getOrThrow()
                productUiState = ProductUiState.Success(filteredProducts)
                Log.d("products list", "success ${result.getOrThrow().size}")
            } catch (exception: Exception) {
                Log.e("HomeScreenViewModel", "Error: ${exception.localizedMessage}")
                productUiState = when (exception) {
                    is IOException -> ProductUiState.Error("Network error occurred. Please check your connection.")
                    is HttpException -> ProductUiState.Error("Server error occurred. Please try again later.")
                    else -> ProductUiState.Error("An unknown error occurred. Please try again.")
                }
            }
        }
    }
}

sealed interface ProductUiState {
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val error: String) : ProductUiState
    object Loading : ProductUiState
}
