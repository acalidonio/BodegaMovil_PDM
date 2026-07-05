package com.acalidonio.bodegamovil.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acalidonio.bodegamovil.di.AppContainer

data class SearchUiState(
    val query: String = "",
    val results: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .onEach { _uiState.update { state -> state.copy(isLoading = true) } }
                .flatMapLatest { query -> repository.searchProducts(query) }
                .collect { products ->
                    _uiState.update { it.copy(results = products, isLoading = false) }
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        _searchQuery.value = newQuery
    }
}
