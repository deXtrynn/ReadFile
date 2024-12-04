package Comp2112.Project2;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        fileSystem.loadFromFile();
        int choice;
        do {
            System.out.println("\nDosya Sistemi Menü:");
            System.out.println("1. Add new directory");
            System.out.println("2. Add new file");
            System.out.println("3. Delete directory");
            System.out.println("4. Delete file");
            System.out.println("5. List all files");
            System.out.println("6. List all directories");
            System.out.println("7. Print tree");
            System.out.println("8. List directory contents");
            System.out.println("9. Search by name");
            System.out.println("10.Search by extension");
            System.out.println("11.View full path");
            System.out.println("12.Exit");

            System.out.print("Make your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    fileSystem.printEntireTree(false);
                    System.out.print("Which directory do you want to add it to?: ");
                    String parentDir1 = scanner.nextLine();
                    System.out.print("Name of the new directory: ");
                    String newDirName = scanner.nextLine();
                    fileSystem.addDirectory(parentDir1, newDirName, "USER");
                    break;

                case 2:
                    fileSystem.printEntireTree(false);
                    System.out.print("Which directory do you want to add it to?: ");
                    String parentDir2 = scanner.nextLine();
                    System.out.print("Name of the new file (e.g. file.txt): ");
                    String fileName = scanner.nextLine();
                    System.out.print("File size (bytes): ");
                    int size = scanner.nextInt();
                    scanner.nextLine();
                    fileSystem.addFile(parentDir2, fileName, size, "USER");
                    break;

                case 3:
                    fileSystem.printEntireTree(false);
                    System.out.print("Name of the directory to be deleted: ");
                    String dirToDelete = scanner.nextLine();
                    fileSystem.deleteDirectory(dirToDelete);
                    break;

                case 4:
                    fileSystem.printEntireTree(false);
                    System.out.print("Name of the file to be deleted: ");
                    String fileToDelete = scanner.nextLine();
                    fileSystem.deleteFile(fileToDelete);
                    break;

                case 5:
                    fileSystem.listAllFiles();

                case 6:
                    fileSystem.listAllDirectories();
                    break;

                case 7:
                    System.out.println("Tree structure:");
                    fileSystem.printEntireTree(false);

                    System.out.print("\nThe name of the node whose details you want to see: ");
                    String nodeName = scanner.nextLine();
                    fileSystem.showNodeDetails(nodeName);
                    break;

                case 8:
                    //dizinin içinde kaç tane file var kaç tane alt dizin var onu yazdırır ve özelliklerini verir
                    fileSystem.printEntireTree(false);
                    System.out.print("Which directory's contents do you want to list?: ");
                    String listDir = scanner.nextLine();
                    fileSystem.listContents(listDir);
                    break;
                case 9:
                    //ismini arattırıp dizin mi yoksa dosya mı onu buldurtma
                    fileSystem.printEntireTree(false);
                    System.out.print("Search name: ");
                    String searchName = scanner.nextLine();
                    fileSystem.searchByName(searchName);
                    break;

                case 10:
                    //txt,png,zip olarak kaç dosya varsa istediğini yazdırır
                    fileSystem.printEntireTree(false);
                    System.out.print("Extension to search (e.g. txt,png,zip): ");
                    String searchExt = scanner.nextLine();
                    fileSystem.searchByExtension(searchExt);
                    break;
                case 11:
                    //istediğin bir dosyaya ya da dizine nasıl ulaşılabileceğinin yolunu çiziyor
                    fileSystem.printEntireTree(false);
                    System.out.print("File/directory name whose path will be displayed: ");
                    String pathTarget = scanner.nextLine();
                    fileSystem.displayPath(pathTarget);
                    break;
                case 12:
                    System.out.println("Signing out");
                    break;

                default:
                    System.out.println("Invalid selection, try again.");
                    break;
            }
        } while (choice != 12);

        scanner.close();
    }
}
