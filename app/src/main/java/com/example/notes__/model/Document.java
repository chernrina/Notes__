package com.example.notes__.model;

import java.util.Date;

public class Document {

    private String name;
    private String content;
    private Date createDate;
    private Integer number;
    private PriorityType priorityType = PriorityType.ORDINARY;
    private boolean checkBox = false;

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
            if (this.createDate.getTime() == ((Document) o).getCreateDate().getTime()) {
                return true;
            }
        }
        return false;
    }

    public void setPriorityType(PriorityType priorityType) {
        this.priorityType = priorityType;
    }

    public boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean check) {this.checkBox = check;}
}
