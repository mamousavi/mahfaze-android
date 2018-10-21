package com.miramir.mahfaze.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.miramir.mahfaze.MahfazeApp
import com.miramir.mahfaze.data.repository.NoteRepository
import javax.inject.Inject

class SyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
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