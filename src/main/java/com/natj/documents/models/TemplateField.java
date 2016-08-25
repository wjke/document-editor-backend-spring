package com.natj.documents.models;

import com.fasterxml.jackson.annotation.*;
import com.natj.documents.enums.InputType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"template_id", "ordering"})) //Запрещает два одинаковых "сортировки" поля для одного шаблона
public class TemplateField {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Enumerated
    private InputType type;

    @NotEmpty
    private String title;

    private int ordering = 0;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @JsonIgnore
    @OneToMany(mappedBy = "templateField", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DocumentTemplateField> documentTemplateFields = new ArrayList<>(0);

    public TemplateField() {
    }

    public TemplateField(String title, InputType type) {
        this.title = title;
        this.type = type;
    }

    public TemplateField(String title, InputType type, Template template) {
        this.title = title;
        this.type = type;
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

    public InputType getType() {
        return type;
    }

    public void setType(InputType type) {
        this.type = type;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public List<DocumentTemplateField> getDocumentTemplateFields() {
        return documentTemplateFields;
    }

    public void setDocumentTemplateFields(List<DocumentTemplateField> documentTemplateFields) {
        this.documentTemplateFields = documentTemplateFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateField that = (TemplateField) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "TemplateField{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", ordering=" + ordering +
                ", template=" + template +
                '}';
    }
}
