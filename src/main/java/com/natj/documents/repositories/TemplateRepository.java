package com.natj.documents.repositories;

import com.natj.documents.models.Template;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<Template, Long> {
    Template findByTitleIgnoreCase(String title);
}
