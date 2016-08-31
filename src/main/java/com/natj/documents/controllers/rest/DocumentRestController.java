package com.natj.documents.controllers.rest;

import com.natj.documents.exceptions.NotFoundException;
import com.natj.documents.models.Document;
import com.natj.documents.models.DocumentTemplateField;
import com.natj.documents.models.Template;
import com.natj.documents.services.DocumentService;
import com.natj.documents.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentRestController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TemplateService templateService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Document>> listAllDocuments() {
        return new ResponseEntity<>(documentService.listAllDocuments(), HttpStatus.OK);
    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.GET)
    public ResponseEntity<Document> getDocument(@PathVariable long documentId) {
        Document document = documentService.getDocumentById(documentId);
        if(document == null)
            throw new NotFoundException("document not found");

        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}", method = RequestMethod.POST)
    public ResponseEntity<Document> createDocument(@PathVariable long templateId, @RequestBody @Valid Document document, BindingResult result) {
        Template template;
        if(result.hasErrors() || (template = templateService.getTemplateById(templateId)) == null || documentService.getDocumentByTitleAndTemplate(document.getTitle(), template) != null)
            throw new NotFoundException("document validation error or template not found or title not unique");

        document.setTemplate(template);
        document = documentService.saveDocument(document);

        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.PUT)
    public ResponseEntity<Document> updateDocument(@PathVariable long documentId, @RequestBody @Valid Document document, BindingResult result) {
        Document existDocument;
        if(result.hasErrors() || (existDocument = documentService.getDocumentById(documentId)) == null)
            throw new NotFoundException("document validation error or not found");

        Document checkTitleDocument = documentService.getDocumentByTitleAndTemplate(document.getTitle(), existDocument.getTemplate());
        if(checkTitleDocument != null && checkTitleDocument.getId() != documentId)
            throw new NotFoundException("title is not unique");

        existDocument.setTitle(document.getTitle());
        existDocument = documentService.updateDocument(existDocument);

        return new ResponseEntity<>(existDocument, HttpStatus.OK);
    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDocument(@PathVariable long documentId, HttpServletRequest request) {
        Document document = documentService.getDocumentById(documentId);
        if(document == null)
            throw new NotFoundException("document not found");

        if(request.isUserInRole("ROLE_ADMIN"))
            documentService.deleteDocument(documentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "{documentId}/datafields", method = RequestMethod.GET)
    public ResponseEntity<List<DocumentTemplateField>> listAllTemplateFields(@PathVariable long documentId) {
        Document document = documentService.getDocumentById(documentId);
        if(document == null)
            throw new NotFoundException("document not found");

        return new ResponseEntity<>(document.getDocumentTemplateFields(), HttpStatus.OK);
    }
}
