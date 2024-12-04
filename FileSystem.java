package Comp2112.Project2;

import java.io.*;
import java.util.*;

public class FileSystem {

    private final Directory root;

    public FileSystem() {
        root = new Directory("root", "USER");
 
    }

    public void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("C:\\Users\\Ahmet Eren\\Desktop\\myfiles.txt"))) {
            Stack<Directory> directoryStack = new Stack<>();
            directoryStack.push(root);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int indentLevel = countIndentation(line);
                String content = line.trim();

                if (!content.isEmpty() && content.charAt(0) == '\\') {
                    // Dizin tespiti
                    String dirName = content.substring(1).trim();
                    Directory dir = new Directory(dirName, "USER");
                    while (directoryStack.size() > indentLevel + 1) {
                        directoryStack.pop();
                    }
                    directoryStack.peek().addSubDirectory(dir);
                    directoryStack.push(dir);

                } else if (!content.isEmpty() && content.contains("##")) {
                    // Dosya bilgisi ayrıştırma
                    try {
                        int firstDelimiter = content.indexOf("##");
                        int secondDelimiter = content.indexOf("##", firstDelimiter + 2);
                        int thirdDelimiter = content.indexOf("##", secondDelimiter + 2);

                        String name = content.substring(0, firstDelimiter).trim();
                        String dateString = content.substring(firstDelimiter + 2, secondDelimiter).trim();
                        String[] dateParts = dateString.split("\\.");
                        int day = stringToInt(dateParts[0]);
                        int month = stringToInt(dateParts[1]) - 1; // Aylar 0 tabanlıdır
                        int year = stringToInt(dateParts[2]);
                        Date lastModified = new GregorianCalendar(year, month, day).getTime();

                        int size = stringToInt(content.substring(secondDelimiter + 2, thirdDelimiter).trim());
                        String accessLevel = content.substring(thirdDelimiter + 2).trim();

                        // FileProperty nesnesi oluştur
                        FileProperty file = new FileProperty(name, getExtension(name), lastModified, size, accessLevel);
                        while (directoryStack.size() > indentLevel + 1) {
                            directoryStack.pop();
                        }
                        directoryStack.peek().addFile(file);

                    } catch (Exception e) {
                        System.err.println("Error processing file line: " + content + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private int stringToInt(String number) {
        int result = 0;
        for (int i = 0; i < number.length(); i++) {
            result = result * 10 + (number.charAt(i) - '0');
        }
        return result;
    }

    private int countIndentation(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == '\t') {
            count++;
        }
        return count;
    }

    private String getExtension(String fileName) {
        String extension = "";
        for (int i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                extension = fileName.substring(i + 1);
                break;
            }
        }
        return extension;
    }

    public void addDirectory(String parentDirectoryName, String newDirectoryName, String accessLevel) {
        Directory parentDir = root.searchDirectoryByName(parentDirectoryName);
        if (parentDir != null && parentDir.getAccessLevel().equalsIgnoreCase("USER")) {
            Directory newDir = new Directory(newDirectoryName, accessLevel);
            parentDir.addSubDirectory(newDir);
            System.out.println("Yeni dizin eklendi: " + newDirectoryName);
        } else {
            System.out.println("Dizin ekleme başarısız: Belirtilen dizin bulunamadı veya erişim seviyesi USER değil.");
        }
    }

    public void addFile(String parentDirectoryName, String fileName, int size, String accessLevel) {
        Directory parentDir = root.searchDirectoryByName(parentDirectoryName);
        if (parentDir != null && parentDir.getAccessLevel().equalsIgnoreCase("USER")) {
            FileProperty newFile = new FileProperty(fileName, getExtension(fileName), new Date(), size, accessLevel);
            parentDir.addFile(newFile);
            System.out.println("Yeni dosya eklendi: " + fileName);
        } else {
            System.out.println("Dosya ekleme başarısız: Belirtilen dizin bulunamadı veya erişim seviyesi USER değil.");
        }
    }

    public void deleteDirectory(String directoryName) {
        Directory dirToDelete = root.searchDirectoryByName(directoryName);
        if (dirToDelete != null && dirToDelete.getAccessLevel().equalsIgnoreCase("USER")) {
            Directory parentDir = findParentDirectory(root, dirToDelete);
            if (parentDir != null) {
                parentDir.deleteSubDirectory(dirToDelete);
                System.out.println("Dizin silindi: " + directoryName);
            } else {
                System.out.println("Kök dizin silinemez.");
            }
        } else {
            System.out.println("Dizin silme başarısız: Erişim seviyesi USER değil veya dizin bulunamadı.");
        }
    }

    public void deleteFile(String fileName) {
        FileProperty fileToDelete = root.searchFileByName(fileName);
        if (fileToDelete != null && fileToDelete.getAccessLevel().equalsIgnoreCase("USER")) {
            Directory parentDir = findParentDirectoryForFile(root, fileToDelete);
            if (parentDir != null) {
                parentDir.deleteFile(fileToDelete);
                System.out.println("Dosya silindi: " + fileName);
            }
        } else {
            System.out.println("Dosya silme başarısız: Erişim seviyesi USER değil veya dosya bulunamadı.");
        }
    }

    public void searchByName(String name) {
        System.out.println("Arama Sonuçları:");
        searchByNameRecursive(root, name);
    }

    private void searchByNameRecursive(Directory current, String name) {
        if (current.getName().equalsIgnoreCase(name)) {
            System.out.println("Dizin: " + current.getName());
        }
        for (FileProperty file : current.getFiles()) {
            if (file.getName().equalsIgnoreCase(name)) {
                System.out.println("Dosya: " + file.getName());
            }
        }
        for (Directory subDir : current.getSubDirectories()) {
            searchByNameRecursive(subDir, name);
        }
    }

    public void searchByExtension(String extension) {
        System.out.println("Uzantı ile Arama Sonuçları:");
        searchByExtensionRecursive(root, extension);
    }

    private void searchByExtensionRecursive(Directory current, String extension) {
        for (FileProperty file : current.getFiles()) {
            if (file.getExtension().equalsIgnoreCase(extension)) {
                System.out.println("Dosya: " + file.getName());
            }
        }
        for (Directory subDir : current.getSubDirectories()) {
            searchByExtensionRecursive(subDir, extension);
        }
    }

    public void listAllFiles() {
        System.out.println("All Files in the File System:");
        listFilesRecursive(root);
    }

    private void listFilesRecursive(Directory current) {
        for (FileProperty file : current.getFiles()) {
            file.displayDetails();
            System.out.println();
        }
        for (Directory subDir : current.getSubDirectories()) {
            listFilesRecursive(subDir);
        }
    }

    public void listAllDirectories() {
        System.out.println("Directory Structure:");
        listDirectoriesRecursive(root, 0);
    }

    private void listDirectoriesRecursive(Directory current, int level) {

        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println("\\" + current.getName());

        for (Directory subDir : current.getSubDirectories()) {
            listDirectoriesRecursive(subDir, level + 1);
        }
    }

    public void showNodeDetails(String nodeName) {
        Node treeRoot = convertToNode(root);
        Node foundNode = findNode(treeRoot, nodeName);

        if (foundNode != null) {
            System.out.println("Detaylar:");
            System.out.println("|-- " + foundNode.name + " (" + foundNode.type + ") - Size: " + foundNode.size + " bytes, Last Modified: " + foundNode.lastModified + ", Access: " + foundNode.accessLevel);
        } else {
            System.out.println("Düğüm bulunamadı: " + nodeName);
        }
    }

    public void listContents(String directoryName) {
        //directory de bulunan metoda gidiyor
        Directory dir = root.searchDirectoryByName(directoryName);
        if (dir != null && dir.getAccessLevel().equalsIgnoreCase("USER")) {
            dir.displayContents();
        } else {
            System.out.println("Dizin bulunamadı veya erişim seviyesi USER değil.");
        }
    }

    public void displayPath(String targetName) {
        String path = findPathRecursive(root, targetName, "");
        if (path != null) {
            System.out.println("Tam Yol: " + path);
        } else {
            System.out.println("Hedef bulunamadı: " + targetName);
        }
    }

    public void printEntireTree(boolean showDetails) {
        Node treeRoot = convertToNode(root);
        printTree(treeRoot, "", showDetails);
    }
    
    public void printTree(Node node, String tree, boolean showDetails) {
        if (showDetails) {

            System.out.println(tree + "|-- " + node.name + " (" + node.type + ") - Size: " + node.size + " bytes, Last Modified: " + node.lastModified + ", Access: " + node.accessLevel);
        } else {

            System.out.println(tree + "|-- " + node.name + " (" + node.type + ")");
        }

        for (Node child : node.children) {
            printTree(child, tree + "|   ", showDetails);
        }
    }

    private Node convertToNode(Directory dir) {
        Node node = new Node(dir.getName(), "directory", dir.calculateSize(), dir.getLastModified(), dir.getAccessLevel());
        for (Directory subDir : dir.getSubDirectories()) {
            node.children.add(convertToNode(subDir));
        }
        for (FileProperty file : dir.getFiles()) {
            node.children.add(new Node(file.getName(), "file", file.getSize(), file.getLastModified(), file.getAccessLevel()));
        }
        return node;
    }
    
    private Node findNode(Node current, String name) {
        if (current.name.equalsIgnoreCase(name)) {
            return current;
        }
        for (Node child : current.children) {
            Node found = findNode(child, name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private Directory findParentDirectory(Directory current, Directory target) {
        for (Directory subDir : current.getSubDirectories()) {
            if (subDir == target) {
                return current;
            }
            Directory parent = findParentDirectory(subDir, target);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    private Directory findParentDirectoryForFile(Directory current, FileProperty targetFile) {
        if (current.getFiles().contains(targetFile)) {
            return current;
        }
        for (Directory subDir : current.getSubDirectories()) {
            Directory parent = findParentDirectoryForFile(subDir, targetFile);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    private String findPathRecursive(Directory current, String targetName, String path) {
        path = path + "/" + current.getName();
        if (current.getName().equalsIgnoreCase(targetName)) {
            return path;
        }
        for (FileProperty file : current.getFiles()) {
            if (file.getName().equalsIgnoreCase(targetName)) {
                return path + "/" + file.getName();
            }
        }
        for (Directory subDir : current.getSubDirectories()) {
            String result = findPathRecursive(subDir, targetName, path);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

   


}
