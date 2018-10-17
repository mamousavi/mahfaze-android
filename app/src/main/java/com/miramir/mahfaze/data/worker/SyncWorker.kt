package com.miramir.mahfaze.data.worker

import androidx.work.Worker
import com.miramir.mahfaze.MahfazeApp
import com.miramir.mahfaze.data.repository.NoteRepository
import javax.inject.Inject

class SyncWorker : Worker() {
    @Inject lateinit var noteRepository: NoteRepository

    override fun doWork(): Result {
        (applicationContext as MahfazeApp)
                .workComponent
                .inject(this)

        try {
            noteRepository.refreshSync()
        } catch (e: Exception) {
            return Result.FAILURE
        }

        return Result.SUCCESS
    }
}