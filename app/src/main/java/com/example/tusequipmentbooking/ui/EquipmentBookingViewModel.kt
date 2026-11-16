package com.example.tusequipmentbooking.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusequipmentbooking.data.EquipmentRepository
import com.example.tusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentBookingUiState(
    val equipment: List<Equipment> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val actionMessage: String? = null,
    val isAdding: Boolean = false
)

class EquipmentBookingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EquipmentRepository(
        EquipmentBookingDatabase.getInstance(application)
    )

    private val mutableUiState = MutableStateFlow(EquipmentBookingUiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.seedIfEmpty()
                combine(repository.equipment, repository.categories) { equipment, categories ->
                    equipment to categories
                }.collect { (equipment, categories) ->
                    mutableUiState.update {
                        it.copy(
                            equipment = equipment, categories = categories,
                            isLoading = false, loadError = null
                        )
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(
                        isLoading = false,
                        loadError = error.message ?: "Unable to load equipment"
                    )
                }
            }
        }
    }

    fun addTestEquipment() {
        val current = mutableUiState.value
        val categoryId = current.categories.firstOrNull()?.id ?: return
        if (current.isLoading || current.isAdding || current.loadError != null) return

        mutableUiState.update { it.copy(isAdding = true, actionMessage = null) }

        viewModelScope.launch {
            try {
                val id = repository.addEquipment(categoryId)
                mutableUiState.update {
                    it.copy(actionMessage = "Saved equipment with ID $id")
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(actionMessage = error.message ?: "Unable to save equipment")
                }
            } finally {
                mutableUiState.update { it.copy(isAdding = false) }
            }
        }
    }
}
