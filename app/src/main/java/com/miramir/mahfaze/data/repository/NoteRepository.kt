package com.miramir.mahfaze.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.miramir.mahfaze.data.local.NoteDao
import com.miramir.mahfaze.data.model.Note
import com.miramir.mahfaze.data.model.Resource
import com.miramir.mahfaze.data.remote.NoteBatchQueryRequest
import com.miramir.mahfaze.data.remote.NoteService
import com.miramir.mahfaze.util.AppExecutors
import com.miramir.mahfaze.util.executeAndUnwrap
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteService: NoteService, private val noteDao: NoteDao) {
    fun create(note: Note) {
        AppExecutors.IO_EXECUTOR.execute { noteDao.save(note) }
    }

    fun get(id: String) = noteDao.load(id)

    fun getAll(search: String) = noteDao.loadAll(search)

    fun refresh(): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        result.value = Resource.loading(null)
        AppExecutors.IO_EXECUTOR.execute {
            try {
                refreshSync()
                AppExecutors.MAIN_EXECUTOR.execute { result.value = Resource.success(null) }
            } catch (e: Exception) {
                AppExecutors.MAIN_EXECUTOR.execute { result.value = Resource.error(e, null) }
            }
        }
        return result
    }

    fun refreshSync() {
        val remoteNotes = noteService.getAll().executeAndUnwrap()
        val remoteInserts = mutableListOf<Note>()
        val remoteUpdates = mutableListOf<Note>()
        val localNotes = noteDao.loadAllSync()
        val localInserts = mutableListOf<Note>()
        remoteNotes.forEach { remoteNote ->
            val localNote = localNotes.find { it.id == remoteNote.id }
            if (localNote == null) {
                localInserts.add(remoteNote)
            } else {
                if (remoteNote.deletedAt != null) {
                    if (localNote.deletedAt == null) {
                        if (Instant.parse(remoteNote.deletedAt) > Instant.parse(localNote.updatedAt)) {
                            localInserts.add(remoteNote)
                        } else {
                            remoteUpdates.add(localNote)
                        }
                    }
                } else {
                    if (localNote.deletedAt != null) {
                        if (Instant.parse(remoteNote.updatedAt) > Instant.parse(localNote.deletedAt)) {
                            localInserts.add(remoteNote)
                        } else {
                            remoteUpdates.add(localNote)
                        }
                    } else {
                        if (Instant.parse(remoteNote.updatedAt) > Instant.parse(localNote.updatedAt)) {
                            localInserts.add(remoteNote)
                        } else if (Instant.parse(remoteNote.updatedAt) < Instant.parse(localNote.updatedAt)) {
                            remoteUpdates.add(localNote)
                        }
                    }
                }
            }
        }
        localNotes.forEach { localNote ->
            val remoteNote = remoteNotes.find { it.id == localNote.id }
            if (remoteNote == null) {
                remoteInserts.add(localNote)
            }
        }
        noteService.batchQuery(NoteBatchQueryRequest(remoteInserts, remoteUpdates)).executeAndUnwrap()
        noteDao.saveAll(localInserts)
    }
}