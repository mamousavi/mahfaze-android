package com.miramir.mahfaze.ui.main.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.miramir.mahfaze.data.model.Note
import com.miramir.mahfaze.data.model.Resource
import com.miramir.mahfaze.data.repository.NoteRepository
import javax.inject.Inject

class NotesViewModel @Inject constructor(noteRepo: NoteRepository) : ViewModel() {
    private val searchQuery = MutableLiveData<String>()
    private val syncTrigger = MutableLiveData<Unit>()

    val notes: LiveData<List<Note>> = Transformations.switchMap(searchQuery) { noteRepo.getAll(it) }

    val syncResult: LiveData<Resource<Unit>> = Transformations.switchMap(syncTrigger) { noteRepo.refresh() }

    init {
        searchQuery.value = ""
    }

    fun search(query: String) {
        searchQuery.value = query
    }

    fun sync() {
        syncTrigger.value = null
    }
}