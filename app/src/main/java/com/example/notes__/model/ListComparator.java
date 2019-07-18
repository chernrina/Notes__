package com.example.notes__.model;

import java.util.Comparator;

public class ListComparator {

    private static DateComparator dateComparator;
    private static NameComparator nameComparator;
    private static PriorityComparator priorityComparator;

    public static Comparator<Document> getDateComparator() {
        if (dateComparator == null) dateComparator = new DateComparator();
        return dateComparator;
    }

    public static Comparator<Document> getNameComparator() {
        if (nameComparator  == null) nameComparator = new NameComparator();
        return nameComparator;
    }

    public static Comparator<Document> getPriorityComparator() {
        if (priorityComparator == null) priorityComparator = new PriorityComparator();
        return priorityComparator;
    }

    private static class PriorityComparator implements Comparator<Document>{
        @Override
        public int compare(Document doc, Document document) {
            int result = document.getPriorityType().compareTo(doc.getPriorityType());
            if (result == 0) result = document.getCreateDate().compareTo(doc.getCreateDate());
            return result;
        }
    }

    private static class DateComparator implements Comparator<Document> {
        @Override
        public int compare(Document doc, Document document) {
            return document.getCreateDate().compareTo(doc.getCreateDate());
        }
    }

    private static class NameComparator implements Comparator<Document> {
        @Override
        public int compare(Document doc, Document document) {
            return doc.getName().compareTo(document.getName());
        }
    }

}
