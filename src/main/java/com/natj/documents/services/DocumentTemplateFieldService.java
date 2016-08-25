package com.natj.documents.services;

import com.natj.documents.models.DocumentTemplateField;

import java.util.List;

public interface DocumentTemplateFieldService {
    List<DocumentTemplateField> listAllDocumentTemplateFields();
    DocumentTemplateField getDocumentTemplateFieldById(long id);
    DocumentTemplateField saveDocumentTemplateField(DocumentTemplateField documentTemplateField);
    DocumentTemplateField updateDocumentTemplateField(DocumentTemplateField documentTemplateField);
    void deleteDocumentTemplateField(DocumentTemplateField documentTemplateField);
    void deleteDocumentTemplateField(long id);
    void addNewDocumentTemplateFieldToDocuments(long templateFieldId, long templateId);
    void addNewDocumentTemplateFieldsToDocument(long documentId, long templateId);
}
