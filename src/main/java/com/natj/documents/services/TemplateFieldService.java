package com.natj.documents.services;

import com.natj.documents.models.TemplateField;

import java.util.List;

public interface TemplateFieldService {
    List<TemplateField> listAllTemplateFields();
    TemplateField getTemplateFieldById(long id);
    TemplateField saveTemplateField(TemplateField templateField);
    TemplateField updateTemplateField(TemplateField templateField);
    void deleteTemplateField(TemplateField templateField);
    void deleteTemplateField(long id);
    boolean isTemplateFieldExist(long id);
    List<TemplateField> getTemplateFieldsByTitle(String title);
    int findMaxOrdering(long templateId);
    TemplateField upOrDownTemplateField(long id, boolean down);
}
