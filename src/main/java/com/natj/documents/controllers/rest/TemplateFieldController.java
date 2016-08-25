package com.natj.documents.controllers.rest;

import com.natj.documents.exceptions.NotFoundException;
import com.natj.documents.models.Template;
import com.natj.documents.models.TemplateField;
import com.natj.documents.services.TemplateFieldService;
import com.natj.documents.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class TemplateFieldController {
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TemplateFieldService templateFieldService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TemplateField>> listAllTemplateFields() {
        return new ResponseEntity<>(templateFieldService.listAllTemplateFields(), HttpStatus.OK);
    }

    @RequestMapping(value = "{fieldId}", method = RequestMethod.GET)
    public ResponseEntity<TemplateField> getTemplateField(@PathVariable long fieldId) {
        TemplateField templateField = templateFieldService.getTemplateFieldById(fieldId);
        if(templateField == null)
            throw new NotFoundException("templateField not found");

        return new ResponseEntity<>(templateField, HttpStatus.OK);
    }

    @RequestMapping(value = "{templateId}", method = RequestMethod.POST)
    public ResponseEntity<TemplateField> createTemplateField(@PathVariable long templateId, @RequestBody @Valid TemplateField templateField, BindingResult result) {
        Template template;
        if(result.hasErrors() || (template = templateService.getTemplateById(templateId)) == null)
            throw new NotFoundException("templateField validation error or template not found");

        templateField.setTemplate(template);
        templateField = templateFieldService.saveTemplateField(templateField);

        return new ResponseEntity<>(templateField, HttpStatus.OK);
    }

    @RequestMapping(value = "{fieldId}", method = RequestMethod.PUT)
    public ResponseEntity<TemplateField> updateTemplateField(@PathVariable long fieldId, @RequestBody @Valid TemplateField templateField, BindingResult result) {
        TemplateField existTemplateField;
        if(result.hasErrors() || (existTemplateField = templateFieldService.getTemplateFieldById(fieldId)) == null)
            throw new NotFoundException("templateField validation error or not found");

        existTemplateField.setTitle(templateField.getTitle());
        existTemplateField.setType(templateField.getType());
        existTemplateField = templateFieldService.updateTemplateField(existTemplateField);

        return new ResponseEntity<>(existTemplateField, HttpStatus.OK);
    }

    @RequestMapping(value = "{fieldId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTemplateField(@PathVariable long fieldId) {
        TemplateField templateField = templateFieldService.getTemplateFieldById(fieldId);
        if(templateField == null)
            throw new NotFoundException("templateField not found");

        templateFieldService.deleteTemplateField(fieldId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "{fieldId}/up", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<List<TemplateField>> upTemplateField(@PathVariable long fieldId) {
        return upOrDownTemplateField(fieldId, true);
    }

    @RequestMapping(value = "{fieldId}/down", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<List<TemplateField>> downTemplateField(@PathVariable long fieldId) {
        return upOrDownTemplateField(fieldId, false);
    }

    private ResponseEntity<List<TemplateField>> upOrDownTemplateField(long fieldId, boolean up) {
        TemplateField templateField = templateFieldService.upOrDownTemplateField(fieldId, up);
        if(templateField == null)
            throw new NotFoundException("templateField not found");

        return new ResponseEntity<>(templateService.getTemplateById(templateField.getTemplate().getId()).getTemplateFields(), HttpStatus.OK);
    }
}
