package Comp2112.Project2;

import java.util.*;

public class FileProperty {

    private final String name;
    private final String extension;
    private Date lastModified;
    private final int size;
    private final String accessLevel;

    public FileProperty(String name, String extension, Date lastModified, int size, String accessLevel) {
        this.name = name;
        this.extension = extension;
        this.lastModified = lastModified;
        this.size = size;
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public int getSize() {
        return size;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void updateLastModified(Date newDate) {
        this.lastModified = newDate;
    }

    public void displayDetails() {
        System.out.println("File Name: " + name);
        System.out.println("Extension: " + extension);
        System.out.println("Last Modified: " + lastModified);
        System.out.println("Size: " + size + " bytes");
        System.out.println("Access Level: " + accessLevel);
    }
}
