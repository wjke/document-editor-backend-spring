package com.natj.documents.controllers.rest;

import com.natj.documents.enums.InputType;
import com.natj.documents.exceptions.NotFoundException;
import com.natj.documents.models.Document;
import com.natj.documents.models.DocumentTemplateField;
import com.natj.documents.services.DocumentService;
import com.natj.documents.services.DocumentTemplateFieldService;
import com.natj.documents.services.TemplateFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/datafields")
public class DocumentTemplateFieldController {
    @Autowired
    private DocumentTemplateFieldService documentTemplateFieldService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TemplateFieldService templateFieldService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DocumentTemplateField>> listAllDocumentTemplateFields() {
        return new ResponseEntity<>(documentTemplateFieldService.listAllDocumentTemplateFields(), HttpStatus.OK);
    }

    @RequestMapping(value = "{dataFieldId}", method = RequestMethod.GET)
    public ResponseEntity<DocumentTemplateField> getDocumentTemplateField(@PathVariable long dataFieldId) {
        DocumentTemplateField documentTemplateField = documentTemplateFieldService.getDocumentTemplateFieldById(dataFieldId);
        if(documentTemplateField == null)
            throw new NotFoundException("dataField not found");

        return new ResponseEntity<>(documentTemplateField, HttpStatus.OK);
    }

//    @RequestMapping(value = "{documentId}/{fieldId}", method = RequestMethod.POST)
//    public ResponseEntity<DocumentTemplateField> createDocumentTemplateField(@PathVariable long documentId, @PathVariable long fieldId, @RequestBody @Valid DocumentTemplateField documentTemplateField, BindingResult result) {
//        Document document;
//        TemplateField templateField;
//        if(result.hasErrors() || (document = documentService.getDocumentById(documentId)) == null || (templateField = templateFieldService.getTemplateFieldById(fieldId)) == null || document.getTemplate().getId() != templateField.getTemplate().getId())
//            throw new NotFoundException("dataField validation error or document/templateField not found");
//
//        documentTemplateField.setDocument(document);
//        documentTemplateField.setTemplateField(templateField);
//        documentTemplateField = documentTemplateFieldService.saveDocumentTemplateField(documentTemplateField);
//
//        return new ResponseEntity<>(documentTemplateField, HttpStatus.OK);
//    }

    @RequestMapping(value = "{dataFieldId}", method = RequestMethod.PUT)
    public ResponseEntity<DocumentTemplateField> updateDocumentTemplateField(@PathVariable long dataFieldId, @RequestBody @Valid DocumentTemplateField documentTemplateField, BindingResult result) {
        DocumentTemplateField existDocumentTemplateField;
        if(result.hasErrors() || (existDocumentTemplateField = documentTemplateFieldService.getDocumentTemplateFieldById(dataFieldId)) == null)
            throw new NotFoundException("dataField validation error or not found");

        existDocumentTemplateField.setData(documentTemplateField.getData());
        existDocumentTemplateField = documentTemplateFieldService.saveDocumentTemplateField(existDocumentTemplateField);

        return new ResponseEntity<>(existDocumentTemplateField, HttpStatus.OK);
    }

//    @RequestMapping(value = "{dataFieldId}", method = RequestMethod.DELETE)
//    public ResponseEntity<Void> deleteDocumentTemplateField(@PathVariable long dataFieldId) {
//        DocumentTemplateField documentTemplateField = documentTemplateFieldService.getDocumentTemplateFieldById(dataFieldId);
//        if(documentTemplateField == null)
//            throw new NotFoundException("dataField not found");
//
//        documentTemplateFieldService.deleteDocumentTemplateField(dataFieldId);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.POST)
    public ResponseEntity<List<DocumentTemplateField>> updateTemplateFields(@PathVariable long documentId, @RequestBody List<FieldUpdate> updateFields) {
        Document document = documentService.getDocumentById(documentId);
        if(document == null)
            throw new NotFoundException("document not found");

        List<DocumentTemplateField> fields = document.getDocumentTemplateFields();
        for(DocumentTemplateField field : fields) {
            //Не отмеченный чекбокс не передаётся как параметр POST
            if(field.getTemplateField().getType() == InputType.CHECKBOX && !updateFields.contains(new FieldUpdate(field.getId())))
                field.setData("");
            else {
                int fieldIndex = updateFields.indexOf(new FieldUpdate(field.getId()));
                if(fieldIndex >= 0)
                    field.setData(updateFields.get(fieldIndex).data);
            }

            documentTemplateFieldService.saveDocumentTemplateField(field);
        }

        return new ResponseEntity<>(document.getDocumentTemplateFields(), HttpStatus.OK);
    }

    private static class FieldUpdate {
        public long id;
        public String data;

        public FieldUpdate() { }

        public FieldUpdate(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FieldUpdate that = (FieldUpdate) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return (int)id;
        }
    }
}
