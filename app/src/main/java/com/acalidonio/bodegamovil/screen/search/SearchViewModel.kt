package com.acalidonio.bodegamovil.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.di.AppContainer
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.ProductCategory
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val categories: Set<ProductCategory> = emptySet(),
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
    private val _selectedCategories = MutableStateFlow<Set<ProductCategory>>(emptySet())

    init {
        val searchFlow = combine(
            _searchQuery.debounce(300),
            _selectedCategories
        ) { query, categories ->
            Pair(query, categories)
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

        viewModelScope.launch {
            searchFlow
                .flatMapLatest { (query, categories) -> repository.searchProducts(query, categories) }
                .collect { products ->
                    _uiState.update { it.copy(results = products) }
                }
        }

        viewModelScope.launch {
            searchFlow
                .onEach { _uiState.update { state -> state.copy(isLoading = true) } }
                .debounce(1000)
                .collect { (query, categories) ->
                    repository.syncProducts(query, categories)
                    _uiState.update { state -> state.copy(isLoading = false) }
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        _searchQuery.value = newQuery
    }
    
    fun toggleCategory(category: ProductCategory?) {
        val currentCategories = _selectedCategories.value.toMutableSet()
        if (category == null) {
            currentCategories.clear()
        } else {
            if (currentCategories.contains(category)) {
                currentCategories.remove(category)
            } else {
                currentCategories.add(category)
            }
        }
        val newCategories = currentCategories.toSet()
        _uiState.update { it.copy(categories = newCategories) }
        _selectedCategories.value = newCategories
    }
}

