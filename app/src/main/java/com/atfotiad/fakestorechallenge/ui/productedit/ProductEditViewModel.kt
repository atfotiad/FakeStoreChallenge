package com.atfotiad.fakestorechallenge.ui.productedit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val useCase: FakeStoreUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductEditUiState())
    val uiState: StateFlow<ProductEditUiState> = _uiState.asStateFlow()

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            useCase.updateProduct(product).getOrError(context)
            _uiState.value = _uiState.value.copy(product = product)
        }
    }
}

data class ProductEditUiState(
    val product: Product? = null
)