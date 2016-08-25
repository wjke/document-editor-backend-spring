package com.natj.documents.controllers.rest;

import com.natj.documents.exceptions.NotFoundException;
import com.natj.documents.models.Document;
import com.natj.documents.models.Template;
import com.natj.documents.models.TemplateField;
import com.natj.documents.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateRestController {
    @Autowired
    private TemplateService templateService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Template>> listAllTemplates() {
        return new ResponseEntity<>(templateService.listAllTemplates(), HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}", method = RequestMethod.GET)
    public ResponseEntity<Template> getTemplate(@PathVariable long templateId) throws InterruptedException {
        Template template = templateService.getTemplateById(templateId);
        if(template == null)
            throw new NotFoundException("template not found");

        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Template> createTemplate(@RequestBody @Valid Template template, BindingResult result) {
        if(result.hasErrors() || templateService.getTemplateByTitle(template.getTitle()) != null)
            throw new NotFoundException("template validation error or title is not unique");

        template = templateService.saveTemplate(template);

        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}", method = RequestMethod.PUT)
    public ResponseEntity<Template> updateTemplate(@PathVariable long templateId, @RequestBody @Valid Template template, BindingResult result) {
        if(result.hasErrors() || !templateService.isTemplateExist(templateId))
            throw new NotFoundException("template validation error or not found");

        //Проверка на уникальность названия, если такое название уже есть и оно принадлежит этому шаблону, то тогда разраешаем редактировать шаблон
        Template checkTitleTemplate = templateService.getTemplateByTitle(template.getTitle());
        if(checkTitleTemplate != null && checkTitleTemplate.getId() != templateId)
            throw new NotFoundException("title is not unique");

        template.setId(templateId);
        template = templateService.saveTemplate(template);

        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTemplate(@PathVariable long templateId) {
        if(!templateService.isTemplateExist(templateId))
            throw new NotFoundException("template not found");

        templateService.deleteTemplate(templateId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}/fields", method = RequestMethod.GET)
    public ResponseEntity<List<TemplateField>> listAllTemplateFields(@PathVariable long templateId) {
        Template template = templateService.getTemplateById(templateId);
        if(template == null)
            throw new NotFoundException("template not found");

        return new ResponseEntity<>(template.getTemplateFields(), HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}/documents", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> listAllDocuments(@PathVariable long templateId) {
        Template template = templateService.getTemplateById(templateId);
        if(template == null)
            throw new NotFoundException("template not found");

        return new ResponseEntity<>(template.getDocuments(), HttpStatus.OK);
    }
}
