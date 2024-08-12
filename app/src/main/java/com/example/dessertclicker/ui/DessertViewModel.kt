package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    fun showTheNextDessert(){
        val updatedRevenue = _uiState.value.revenue + _uiState.value.currentDessertPrice
        val updatedDessertSold = _uiState.value.dessertSold + 1
        val dessertToShow = determineDessertToShow(dessertList, updatedDessertSold)
        val updatedCurrentDessertImageId = dessertToShow.imageId
        val updatedCurrentDessertPrice = dessertToShow.price

        updateDessertUiState(
            updatedRevenue,
            updatedDessertSold,
            updatedCurrentDessertPrice,
            updatedCurrentDessertImageId
        )
    }

    private fun updateDessertUiState(
        updatedRevenue: Int,
        updatedDessertSold: Int,
        updatedCurrentDessertPrice: Int,
        updatedCurrentDessertImageId: Int
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                revenue = updatedRevenue,
                dessertSold = updatedDessertSold,
                currentDessertPrice = updatedCurrentDessertPrice,
                currentDessertImageId = updatedCurrentDessertImageId
            )
        }
    }

    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }
}