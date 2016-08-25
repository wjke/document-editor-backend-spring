package com.natj.documents.repositories;

import com.natj.documents.models.DocumentTemplateField;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DocumentTemplateFieldRepository extends CrudRepository<DocumentTemplateField, Long> {
    /**
     * Вставляет записи данных(DocumentTemplateField) для существующих документОВ, например: при создании нового
     * поля шаблона(TemplateField) или при создании нового документа
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO document_template_field (data, document_id, template_field_id) SELECT '' as data, id as document_id, :templateFieldId as template_field_id FROM Document WHERE template_id = :templateId", nativeQuery = true)
    void addNewDocumentTemplateFieldToDocuments(@Param("templateFieldId") long templateFieldId, @Param("templateId") long templateId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO document_template_field (data, document_id, template_field_id) SELECT '' as data, :documentId as document_id, id as template_field_id FROM template_field WHERE template_id = :templateId", nativeQuery = true)
    void addNewDocumentTemplateFieldsToDocument(@Param("documentId") long documentId, @Param("templateId") long templateId);
}
