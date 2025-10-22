package com.dovskyy.wopfun.repository;

import com.dovskyy.wopfun.model.ChildNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildNoteRepository extends JpaRepository<ChildNote, Long> {

    List<ChildNote> findByChildIdOrderByNoteDateDesc(Long childId);

    List<ChildNote> findByChildIdAndCategory(Long childId, String category);
}
