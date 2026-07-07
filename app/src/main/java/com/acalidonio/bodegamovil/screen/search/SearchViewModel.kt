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
import kotlinx.coroutines.flow.collectLatest
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
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val isDropdownExpanded: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private var currentPage = 0
    private var isSyncingPage = false

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
                .collectLatest { (query, categories) ->
                    _uiState.update { it.copy(isLoading = true) }
                    currentPage = 0
                    _uiState.update { it.copy(isLastPage = false) }
                    
                    val count = repository.syncProducts(query, categories, currentPage)
                    if (count < 20) {
                        _uiState.update { it.copy(isLastPage = true) }
                    }
                    
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLastPage || state.isLoading || state.isLoadingMore || isSyncingPage) return

        isSyncingPage = true
        _uiState.update { it.copy(isLoadingMore = true) }

        viewModelScope.launch {
            currentPage++
            val count = repository.syncProducts(state.query, state.categories, currentPage)
            
            if (count < 20) {
                _uiState.update { it.copy(isLastPage = true) }
            }
            
            _uiState.update { it.copy(isLoadingMore = false) }
            isSyncingPage = false
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        _searchQuery.value = newQuery
    }
    
    fun toggleCategory(category: ProductCategory?) {
        val currentCategories = _uiState.value.categories.toMutableSet()
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
        _uiState.update { state -> state.copy(categories = newCategories) }
        _selectedCategories.value = newCategories

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            currentPage = 0
            _uiState.update { it.copy(isLastPage = false) }
            val count = repository.syncProducts(_uiState.value.query, _uiState.value.categories, currentPage)
            if (count < 20) {
                _uiState.update { it.copy(isLastPage = true) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
