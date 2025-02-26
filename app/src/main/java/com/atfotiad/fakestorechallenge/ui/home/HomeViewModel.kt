package com.atfotiad.fakestorechallenge.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.data.model.product.HomeData
import com.atfotiad.fakestorechallenge.data.model.product.HomeUiState
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: FakeStoreUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { HomeUiState.Loading }
            val products = useCase.getAllProducts().getOrError(context) ?: emptyList()
            val categories = useCase.getCategories().getOrError(context) ?: emptyList()

            if (products.isNotEmpty() && categories.isNotEmpty()) {
                _uiState.update {
                    HomeUiState.Success(
                        HomeData(
                            products,
                            categories,
                            allProducts = products
                        )
                    )
                }
            }
        }
    }

    fun selectCategory(category: String) {
        viewModelScope.launch {
            val currentData = (_uiState.value as? HomeUiState.Success)?.data
            if (currentData != null) {
                val newSelectedCategory = if (currentData.selectedCategory == category) {
                    null
                } else {
                    category
                }

                val filteredProducts =
                    if (newSelectedCategory == null) {
                        currentData.allProducts
                    } else {
                        currentData.allProducts.filter { it.category == newSelectedCategory }
                    }

                _uiState.update {
                    Log.d("HomeViewModel", "selectCategory: filteredProducts = $filteredProducts")
                    HomeUiState.Success(
                        currentData.copy(
                            products = filteredProducts,
                            selectedCategory = newSelectedCategory
                        )
                    )
                }
            }
        }
    }

    fun clearCategorySelection() {
        viewModelScope.launch {
            val currentData = (_uiState.value as? HomeUiState.Success)?.data
            if (currentData != null) {
                _uiState.update {
                    HomeUiState.Success(
                        currentData.copy(
                            products = if (currentData.selectedCategory != null) {
                                currentData.products
                            } else {
                                currentData.products
                            },
                            selectedCategory = null
                        )
                    )
                }
            } else {
                loadHomeData()
            }
        }
    }
}