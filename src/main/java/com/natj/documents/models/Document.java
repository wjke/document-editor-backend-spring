package com.natj.documents.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"template_id", "title"})) //Запрещает два одинаковых названия документа для одного шаблона
public class Document {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Size(min = 2)
    private String title;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @JsonIgnore
    @OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DocumentTemplateField> documentTemplateFields = new ArrayList<>(0);

    public Document() {
    }

    public Document(String title) {
        this.title = title;
    }

    public Document(String title, Template template) {
        this.title = title;
        this.template = template;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<DocumentTemplateField> getDocumentTemplateFields() {
        Collections.sort(documentTemplateFields);
        return documentTemplateFields;
    }

    public void setDocumentTemplateFields(List<DocumentTemplateField> documentTemplateFields) {
        this.documentTemplateFields = documentTemplateFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        return id == document.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", template=" + template +
                '}';
    }
}
