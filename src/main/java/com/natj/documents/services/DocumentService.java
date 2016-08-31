package com.natj.documents.services;

import com.natj.documents.models.Document;
import com.natj.documents.models.Template;

import java.util.List;

public interface DocumentService {
    List<Document> listAllDocuments();
    Document getDocumentById(long id);
    Document saveDocument(Document document);
    Document updateDocument(Document document);
    void deleteDocument(Document document);
    void deleteDocument(long id);
    boolean isDocumentExist(long id);
    List<Document> getDocumentsByTitle(String title);
    Document getDocumentByTitle(String title);
    Document getDocumentByTitleAndTemplate(String title, Template template);
}
