package com.miramir.mahfaze.ui.main.notedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.miramir.mahfaze.data.model.Note
import com.miramir.mahfaze.data.repository.NoteRepository
import javax.inject.Inject

class NoteDetailViewModel @Inject constructor(private val noteRepo: NoteRepository) : ViewModel() {
    private val noteId = MutableLiveData<String>()

    val note: LiveData<Note> = Transformations.switchMap(noteId) { noteRepo.get(it) }

    fun setId(id: String) {
        if (noteId.value == id) {
            return
        }
        noteId.value = id
    }

    fun save(note: Note) {
        noteRepo.create(note)
    }
}