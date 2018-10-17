package com.miramir.mahfaze.di

import com.miramir.mahfaze.ui.login.LoginActivity
import com.miramir.mahfaze.ui.main.MainActivity
import com.miramir.mahfaze.ui.main.notedetail.NoteDetailFragment
import com.miramir.mahfaze.ui.main.notes.NotesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeNotesFragment(): NotesFragment

    @ContributesAndroidInjector
    abstract fun contributeNoteDetailFragment(): NoteDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity
}