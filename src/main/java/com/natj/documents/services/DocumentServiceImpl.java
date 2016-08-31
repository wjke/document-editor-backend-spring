package com.natj.documents.services;

import com.natj.documents.models.Document;
import com.natj.documents.models.Template;
import com.natj.documents.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentTemplateFieldService documentTemplateFieldService;

    @Override
    public List<Document> listAllDocuments() {
        List<Document> documents = new ArrayList<>();
        documentRepository.findAll().forEach(document -> documents.add(document));

        return documents;
    }

    @Override
    public Document getDocumentById(long id) {
        return documentRepository.findOne(id);
    }

    @Override
    public Document saveDocument(Document document) {
        document = documentRepository.save(document);
        documentTemplateFieldService.addNewDocumentTemplateFieldsToDocument(document.getId(), document.getTemplate().getId());

        return document;
    }

    @Override
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void deleteDocument(Document document) {
        documentRepository.delete(document);
    }

    @Override
    public void deleteDocument(long id) {
        documentRepository.delete(id);
    }

    @Override
    public boolean isDocumentExist(long id) {
        return documentRepository.exists(id);
    }

    @Override
    public List<Document> getDocumentsByTitle(String title) {
        return documentRepository.findByTitle(title);
    }

    @Override
    public Document getDocumentByTitle(String title) {
        return documentRepository.findByTitleIgnoreCase(title);
    }

    @Override
    public Document getDocumentByTitleAndTemplate(String title, Template template) {
        return documentRepository.findByTitleIgnoreCaseAndTemplate(title, template);
    }
}
