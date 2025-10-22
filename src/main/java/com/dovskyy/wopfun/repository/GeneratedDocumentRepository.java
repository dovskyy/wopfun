package com.dovskyy.wopfun.repository;

import com.dovskyy.wopfun.model.GeneratedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedDocumentRepository extends JpaRepository<GeneratedDocument, Long> {

    List<GeneratedDocument> findByChildIdOrderByGeneratedAtDesc(Long childId);

    List<GeneratedDocument> findByChildIdAndDocType(Long childId, String docType);

    List<GeneratedDocument> findByStatus(String status);
}
