package com.dovskyy.wopfun.service;

import com.dovskyy.wopfun.model.Child;
import com.dovskyy.wopfun.model.ChildNote;
import com.dovskyy.wopfun.repository.ChildNoteRepository;
import com.dovskyy.wopfun.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildNoteService {

    private final ChildNoteRepository childNoteRepository;
    private final ChildRepository childRepository;

    @Transactional(readOnly = true)
    public List<ChildNote> getNotesByChildId(Long childId) {
        return childNoteRepository.findByChildIdOrderByNoteDateDesc(childId);
    }

    @Transactional
    public ChildNote saveNote(ChildNote note) {
        return childNoteRepository.save(note);
    }

    @Transactional
    public ChildNote addNoteToChild(Long childId, ChildNote note) {
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            throw new RuntimeException("Nie znaleziono dziecka o ID: " + childId);
        }
        note.setChild(childOpt.get());
        return childNoteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long noteId) {
        childNoteRepository.deleteById(noteId);
    }

    @Transactional(readOnly = true)
    public List<ChildNote> getNotesByCategory(Long childId, String category) {
        return childNoteRepository.findByChildIdAndCategory(childId, category);
    }
}
