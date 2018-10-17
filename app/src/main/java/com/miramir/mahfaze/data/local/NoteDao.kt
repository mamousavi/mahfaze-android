package com.miramir.mahfaze.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miramir.mahfaze.data.model.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(notes: List<Note>)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun load(id: String): LiveData<Note>

    @Query("SELECT * FROM notes WHERE text LIKE '%' || :search || '%' AND deletedAt IS NULL")
    fun loadAll(search: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    fun loadAllSync(): List<Note>
}