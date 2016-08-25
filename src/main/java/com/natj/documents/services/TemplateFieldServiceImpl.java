package com.natj.documents.services;

import com.natj.documents.models.TemplateField;
import com.natj.documents.repositories.TemplateFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateFieldServiceImpl implements TemplateFieldService {
    @Autowired
    private TemplateFieldRepository templateFieldRepository;
    @Autowired
    private DocumentTemplateFieldService documentTemplateFieldService;

    @Override
    public List<TemplateField> listAllTemplateFields() {
        List<TemplateField> templateFields = new ArrayList<>();
        templateFieldRepository.findAll().forEach(templateField -> templateFields.add(templateField));

        return templateFields;
    }

    @Override
    public TemplateField getTemplateFieldById(long id) {
        return templateFieldRepository.findOne(id);
    }

    @Override
    public TemplateField saveTemplateField(TemplateField templateField) {
        templateField.setOrdering(findMaxOrdering(templateField.getTemplate().getId()) + 1);
        templateField = templateFieldRepository.save(templateField);

        documentTemplateFieldService.addNewDocumentTemplateFieldToDocuments(templateField.getId(), templateField.getTemplate().getId());

        return templateField;
    }

    @Override
    public TemplateField updateTemplateField(TemplateField templateField) {
        return templateFieldRepository.save(templateField);
    }

    @Override
    public void deleteTemplateField(TemplateField templateField) {
        templateFieldRepository.delete(templateField);
        templateFieldRepository.updateOrderingAfterDelete(templateField.getTemplate().getId(), templateField.getOrdering());
    }

    @Override
    public void deleteTemplateField(long id) {
        TemplateField templateField = getTemplateFieldById(id);
        templateFieldRepository.delete(id);
        templateFieldRepository.updateOrderingAfterDelete(templateField.getTemplate().getId(), templateField.getOrdering());
    }

    @Override
    public boolean isTemplateFieldExist(long id) {
        return templateFieldRepository.exists(id);
    }

    @Override
    public List<TemplateField> getTemplateFieldsByTitle(String title) {
        return templateFieldRepository.findByTitle(title);
    }

    @Override
    public int findMaxOrdering(long templateId) {
        //конвертация из Integer в int нужна, чтобы не было исключений
        Integer result = templateFieldRepository.findMaxOrdering(templateId);
        return result == null ? 0 : result;
    }

    @Override
    public TemplateField upOrDownTemplateField(long id, boolean up) {
        TemplateField templateField = getTemplateFieldById(id);

        if(templateField != null && ((!up && templateField.getOrdering() != findMaxOrdering(templateField.getTemplate().getId())) || (up && templateField.getOrdering() > 1))) {
            int upOrDown = up ? -1 : 1;
            templateFieldRepository.upOrDownOrderingStart(templateField.getTemplate().getId(), templateField.getOrdering(), upOrDown);
            templateFieldRepository.upOrDownOrderingEnd(templateField.getTemplate().getId(), templateField.getOrdering(), upOrDown);
        }

        return templateField;
    }
}
