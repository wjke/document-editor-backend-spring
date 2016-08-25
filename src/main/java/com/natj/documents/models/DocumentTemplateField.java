package com.natj.documents.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "template_field_id"}))
public class DocumentTemplateField implements Comparable<DocumentTemplateField> {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String data = "";

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "template_field_id")
    private TemplateField templateField;

    public DocumentTemplateField() {
    }

    public DocumentTemplateField(String data) {
        this.data = data;
    }

    public DocumentTemplateField(String data, Document document) {
        this.data = data;
        this.document = document;
    }

    public DocumentTemplateField(String data, Document document, TemplateField templateField) {
        this.data = data;
        this.document = document;
        this.templateField = templateField;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public TemplateField getTemplateField() {
        return templateField;
    }

    public void setTemplateField(TemplateField templateField) {
        this.templateField = templateField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentTemplateField that = (DocumentTemplateField) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "DocumentTemplateField{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", document=" + document +
                ", templateField=" + templateField +
                '}';
    }

    @Override
    public int compareTo(DocumentTemplateField o) {
        return this.getTemplateField().getOrdering() - o.getTemplateField().getOrdering();
    }
}
