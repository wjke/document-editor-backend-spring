package com.natj.documents.services;

import com.natj.documents.models.Template;

import java.util.List;

public interface TemplateService {
    List<Template> listAllTemplates();
    Template getTemplateById(long id);
    Template saveTemplate(Template template);
    void deleteTemplate(Template template);
    void deleteTemplate(long id);
    boolean isTemplateExist(long id);
    Template getTemplateByTitle(String title);
}
