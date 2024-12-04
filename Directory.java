package Comp2112.Project2;

import java.util.*;

class Directory {

    private String name;
    private List<Directory> subDirectories;
    private List<FileProperty> files;
    private Date lastModified;
    private String accessLevel;

    public Directory() {
    }

    public Directory(String name, String accessLevel) {
        this.name = name;
        this.subDirectories = new ArrayList<>();
        this.files = new ArrayList<>();
        this.accessLevel = accessLevel != null ? accessLevel : "USER";
        this.lastModified = new Date();
    }

    public String getName() {
        return name;
    }

    public List<Directory> getSubDirectories() {
        return subDirectories;
    }

    public List<FileProperty> getFiles() {
        return files;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void addSubDirectory(Directory directory) {
        subDirectories.add(directory);
        updateLastModified();
    }

    public void addFile(FileProperty file) {
        files.add(file);
        updateLastModified();
    }

    public void deleteSubDirectory(Directory directory) {
        if (subDirectories.remove(directory)) {
            updateLastModified();
        }
    }

    public void deleteFile(FileProperty file) {
        if (files.remove(file)) {
            updateLastModified();
        }
    }

    public FileProperty searchFileByName(String fileName) {
        for (FileProperty file : files) {
            if (file.getName().equalsIgnoreCase(fileName)) {
                return file;
            }
        }
        for (Directory subDir : subDirectories) {
            FileProperty foundFile = subDir.searchFileByName(fileName);
            if (foundFile != null) {
                return foundFile;
            }
        }
        return null;
    }

    public Directory searchDirectoryByName(String dirName) {
        if (this.name.equalsIgnoreCase(dirName)) {
            return this;
        }
        for (Directory subDir : subDirectories) {
            Directory foundDir = subDir.searchDirectoryByName(dirName);
            if (foundDir != null) {
                return foundDir;
            }
        }
        return null;
    }

    private void updateLastModified() {
        this.lastModified = new Date();
    }

    public int calculateSize() {
        int totalSize = 0;
        for (FileProperty file : files) {
            totalSize += file.getSize();
        }
        for (Directory subDir : subDirectories) {
            totalSize += subDir.calculateSize();
        }
        return totalSize;
    }

    public void displayContents() {
        System.out.println("Directory: " + name);
        System.out.println("Access Level: " + accessLevel);
        System.out.println("Last Modified: " + lastModified);
        System.out.println("");
        System.out.println("Files:");
        for (FileProperty file : files) {
            file.displayDetails();
        }
        System.out.println("");
        System.out.println("Subdirectories:");
        for (Directory subDir : subDirectories) {
            System.out.println(" - " + subDir.getName());
        }
    }
}
