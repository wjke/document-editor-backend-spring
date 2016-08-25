package com.natj.documents.bootstrap;

import com.natj.documents.enums.InputType;
import com.natj.documents.models.Document;
import com.natj.documents.models.DocumentTemplateField;
import com.natj.documents.models.Template;
import com.natj.documents.models.TemplateField;
import com.natj.documents.services.DocumentService;
import com.natj.documents.services.DocumentTemplateFieldService;
import com.natj.documents.services.TemplateFieldService;
import com.natj.documents.services.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TemplateFieldService templateFieldService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTemplateFieldService documentTemplateFieldService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        testData();
    }

    private void updateDocumentTemplateField(long documentTemplateFieldId, String data) {
        DocumentTemplateField documentTemplateField = documentTemplateFieldService.getDocumentTemplateFieldById(documentTemplateFieldId);
        documentTemplateField.setData(data);
        documentTemplateFieldService.updateDocumentTemplateField(documentTemplateField);
    }

    private void testData() {
        //добавляем шаблоны
        templateService.saveTemplate(new Template("Template #1"));
        templateService.saveTemplate(new Template("Template #2"));

        Template template1 = templateService.getTemplateById(1L);
        Template template2 = templateService.getTemplateById(2L);

        //добавляем документы до создания полей
        documentService.saveDocument(new Document("Document #1", template1));
        documentService.saveDocument(new Document("Document #2", template1));

        //добавляем поля шаблонов
        templateFieldService.saveTemplateField(new TemplateField("TF #1", InputType.TEXT, template1));
        templateFieldService.saveTemplateField(new TemplateField("TF #2", InputType.TEXTAREA, template1));
        templateFieldService.saveTemplateField(new TemplateField("TF #3", InputType.CHECKBOX, template1));

        templateFieldService.saveTemplateField(new TemplateField("Поле", InputType.TEXT, template2));
        templateFieldService.saveTemplateField(new TemplateField("Текст", InputType.TEXTAREA, template2));
        templateFieldService.saveTemplateField(new TemplateField("Чекбокс", InputType.CHECKBOX, template2));

        //добавляем документы после создания полей
        documentService.saveDocument(new Document("АДВ-1", template2));
        documentService.saveDocument(new Document("АДВ-3", template2));

        //заполняем поля документов
        updateDocumentTemplateField(1, "Data for field 1");
        updateDocumentTemplateField(2, "Data for field 2");
        updateDocumentTemplateField(3, "Data for field 3");
        updateDocumentTemplateField(4, "Data for field 1");
        updateDocumentTemplateField(5, "Data for field 2");
        updateDocumentTemplateField(6, "Data for field 3");
        updateDocumentTemplateField(7, "Data for field 1");
        updateDocumentTemplateField(8, "Data for field 2");
        updateDocumentTemplateField(9, "Data for field 3");
        updateDocumentTemplateField(10, "Иван");
        updateDocumentTemplateField(11, "Ленина 666\nСПБ");
        updateDocumentTemplateField(12, "1");
    }
}
