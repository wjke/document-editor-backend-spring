package com.natj.documents.services;

import com.natj.documents.models.Template;
import com.natj.documents.repositories.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public List<Template> listAllTemplates() {
        List<Template> templates = new ArrayList<>();
        templateRepository.findAll().forEach(template -> templates.add(template));

        return templates;
    }

    @Override
    public Template getTemplateById(long id) {
        return templateRepository.findOne(id);
    }

    @Override
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }

    @Override
    public void deleteTemplate(Template template) {
        templateRepository.delete(template);
    }

    @Override
    public void deleteTemplate(long id) {
        templateRepository.delete(id);
    }

    @Override
    public boolean isTemplateExist(long id) {
        return templateRepository.exists(id);
    }

    @Override
    public Template getTemplateByTitle(String title) {
        return templateRepository.findByTitleIgnoreCase(title);
    }
}
