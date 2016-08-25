package com.natj.documents.repositories;

import com.natj.documents.models.Document;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findByTitle(String title);
    Document findByTitleIgnoreCase(String title);
}
