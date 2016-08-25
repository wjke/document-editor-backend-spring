package com.natj.documents.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Template {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Size(min = 2)
    @Column(unique = true)
    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "template", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("ordering")
    private List<TemplateField> templateFields = new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "template", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("id")
    private List<Document> documents = new ArrayList<>(0);

    public Template() {
    }

    public Template(String title) {
        this.title = title;
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

    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;

        return id == template.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
