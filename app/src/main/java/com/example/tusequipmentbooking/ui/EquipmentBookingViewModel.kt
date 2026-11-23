package com.example.tusequipmentbooking.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusequipmentbooking.data.EquipmentRepository
import com.example.tusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.tusequipmentbooking.model.BookingSummary
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentBookingUiState(
    val equipment: List<Equipment> = emptyList(),
    val categories: List<Category> = emptyList(),
    val bookedEquipmentIds: List<Int> = emptyList(),
    val isLoading: Boolean = true,
    val loadError: String? = null
)

data class BookingUiState(
    val bookings: List<BookingSummary> = emptyList(),
    val isBooking: Boolean = false,
    val message: String? = null
)

class EquipmentBookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EquipmentRepository(
        EquipmentBookingDatabase.getInstance(application)
    )
    private val mutableUiState = MutableStateFlow(EquipmentBookingUiState())
    val uiState = mutableUiState.asStateFlow()

    private val bookingAuth = FirebaseAuth.getInstance()
    private val mutableBookingState = MutableStateFlow(BookingUiState())
    val bookingUiState = mutableBookingState.asStateFlow()
    private var bookingJob: Job? = null

    private val bookingListener = FirebaseAuth.AuthStateListener { auth ->
        bookingJob?.cancel()
        mutableBookingState.update { it.copy(bookings = emptyList(), message = null) }
        val uid = auth.currentUser?.uid
        if (uid != null) {
            bookingJob = viewModelScope.launch {
                try {
                    repository.observeBookings(uid).collect { bookings ->
                        if (bookingAuth.currentUser?.uid == uid) {
                            mutableBookingState.update { it.copy(bookings = bookings) }
                        }
                    }
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (error: Exception) {
                    if (bookingAuth.currentUser?.uid == uid) {
                        mutableBookingState.update { it.copy(message = "Unable to load bookings") }
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                repository.seedIfEmpty()
                combine(repository.equipment, repository.categories,
                    repository.bookedEquipmentIds) { equipment, categories, bookedIds ->
                    Triple(equipment, categories, bookedIds)
                }.collect { (equipment, categories, bookedIds) ->
                    mutableUiState.update {
                        it.copy(equipment = equipment, categories = categories,
                            bookedEquipmentIds = bookedIds,
                            isLoading = false, loadError = null)
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(isLoading = false,
                        loadError = error.message ?: "Unable to load equipment")
                }
            }
        }
    }

    init { bookingAuth.addAuthStateListener(bookingListener) }

    fun clearMessage() {
        mutableBookingState.update { it.copy(message = null) }
    }

    fun bookEquipment(equipmentId: Int, durationDays: Int, purpose: String) {
        val userUid = bookingAuth.currentUser?.uid ?: return
        if (mutableBookingState.value.isBooking) return
        mutableBookingState.update { it.copy(isBooking = true, message = null) }
        viewModelScope.launch {
            try {
                check(bookingAuth.currentUser?.uid == userUid) { "Please sign in again" }
                repository.bookEquipment(equipmentId, userUid, durationDays, purpose)
                val name = mutableUiState.value.equipment
                    .firstOrNull { it.id == equipmentId }?.name ?: "equipment"
                if (bookingAuth.currentUser?.uid == userUid) {
                    mutableBookingState.update {
                        it.copy(message = "Booking saved: $name for $durationDays day(s). " +
                                "Purpose: ${purpose.trim()}")
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                if (bookingAuth.currentUser?.uid == userUid) {
                    mutableBookingState.update {
                        it.copy(message = error.message ?: "Unable to save booking")
                    }
                }
            } finally {
                mutableBookingState.update { it.copy(isBooking = false) }
            }
        }
    }

    fun cancelBooking(bookingId: Int) {
        val userUid = bookingAuth.currentUser?.uid ?: return
        if (mutableBookingState.value.isBooking) return
        mutableBookingState.update { it.copy(isBooking = true, message = null) }
        viewModelScope.launch {
            try {
                check(bookingAuth.currentUser?.uid == userUid) { "Please sign in again" }
                repository.cancelBooking(bookingId, userUid)
                if (bookingAuth.currentUser?.uid == userUid) {
                    mutableBookingState.update { it.copy(message = "Booking cancelled") }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                if (bookingAuth.currentUser?.uid == userUid) {
                    mutableBookingState.update {
                        it.copy(message = error.message ?: "Unable to cancel booking")
                    }
                }
            } finally {
                mutableBookingState.update { it.copy(isBooking = false) }
            }
        }
    }

    override fun onCleared() {
        bookingAuth.removeAuthStateListener(bookingListener)
        super.onCleared()
    }
}
