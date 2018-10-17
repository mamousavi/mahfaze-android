package com.miramir.mahfaze.ui.main.notedetail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.miramir.mahfaze.R
import com.miramir.mahfaze.data.model.Note
import com.miramir.mahfaze.databinding.FragmentNoteDetailBinding
import com.miramir.mahfaze.util.PreferencesManager
import com.miramir.mahfaze.util.hideSoftKeyboard
import com.miramir.mahfaze.util.showSoftKeyboard
import dagger.android.support.DaggerFragment
import org.threeten.bp.Instant
import java.util.*
import javax.inject.Inject

class NoteDetailFragment : DaggerFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var binding: FragmentNoteDetailBinding

    private var shouldSave = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoteDetailBinding.inflate(layoutInflater, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[NoteDetailViewModel::class.java]

        val noteId = NoteDetailFragmentArgs.fromBundle(arguments).noteId
        if (noteId != null) {
            viewModel.setId(noteId)
        } else {
            context?.showSoftKeyboard(binding.text)
        }

        binding.viewmodel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_note_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.action_delete -> {
                    val note = viewModel.note.value
                    if (note != null) {
                        note.deletedAt = Instant.now().toString()
                        viewModel.save(note)
                    }
                    shouldSave = false
                    findNavController().navigateUp()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onPause() {
        super.onPause()

        if (isRemoving) {
            context?.hideSoftKeyboard(binding.text)

            if (shouldSave) {
                val note = viewModel.note.value
                if (note != null) {
                    if (note.text != binding.text.text.toString()) {
                        with(note) {
                            text = binding.text.text.toString()
                            updatedAt = Instant.now().toString()
                        }
                        viewModel.save(note)
                    }
                } else {
                    val newNote = Note(
                            UUID.randomUUID().toString(),
                            binding.text.text.toString(),
                            Instant.now().toString(),
                            Instant.now().toString(),
                            null,
                            PreferencesManager.getUserId(context!!)
                    )
                    viewModel.save(newNote)
                }
            }
        }
    }
}