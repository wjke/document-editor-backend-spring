package com.natj.documents.services;

import com.natj.documents.models.DocumentTemplateField;
import com.natj.documents.repositories.DocumentTemplateFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentTemplateFieldServiceImpl implements DocumentTemplateFieldService {
    @Autowired
    private DocumentTemplateFieldRepository documentTemplateFieldRepository;

    @Override
    public List<DocumentTemplateField> listAllDocumentTemplateFields() {
        List<DocumentTemplateField> documentTemplateFields = new ArrayList<>();
        documentTemplateFieldRepository.findAll().forEach(documentTemplateField -> documentTemplateFields.add(documentTemplateField));

        return documentTemplateFields;
    }

    @Override
    public DocumentTemplateField getDocumentTemplateFieldById(long id) {
        return documentTemplateFieldRepository.findOne(id);
    }

    @Override
    public DocumentTemplateField saveDocumentTemplateField(DocumentTemplateField documentTemplateField) {
        return documentTemplateFieldRepository.save(documentTemplateField);
    }

    @Override
    public DocumentTemplateField updateDocumentTemplateField(DocumentTemplateField documentTemplateField) {
        return documentTemplateFieldRepository.save(documentTemplateField);
    }

    @Override
    public void deleteDocumentTemplateField(DocumentTemplateField documentTemplateField) {
        documentTemplateFieldRepository.delete(documentTemplateField);
    }

    @Override
    public void deleteDocumentTemplateField(long id) {
        documentTemplateFieldRepository.delete(id);
    }

    @Override
    public void addNewDocumentTemplateFieldToDocuments(long templateFieldId, long templateId) {
        documentTemplateFieldRepository.addNewDocumentTemplateFieldToDocuments(templateFieldId, templateId);
    }

    @Override
    public void addNewDocumentTemplateFieldsToDocument(long documentId, long templateId) {
        documentTemplateFieldRepository.addNewDocumentTemplateFieldsToDocument(documentId, templateId);
    }
}
