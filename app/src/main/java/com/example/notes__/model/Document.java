package com.example.notes__.model;

import java.util.Date;

public class Document {

    private String name;
    private String content;
    private Date createDate;
    private Integer number;
    private PriorityType priorityType = PriorityType.ORDINARY;

    public Document(String name, String content, Date createDate, PriorityType priorityType) {
        super();
        this.name = name;
        this.content = content;
        this.createDate = createDate;
        this.priorityType =priorityType;
    }

    public Document() {
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return name;
    }

    public PriorityType getPriorityType() {
        return priorityType;
    }

    public boolean equals(Object o) {
        if (o instanceof Document) {
            if (this.content == ((Document) o).getContent()) return true;
        }
        return false;
    }

    public void setPriorityType(PriorityType priorityType) {
        this.priorityType = priorityType;
    }
}
