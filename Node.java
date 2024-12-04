
package Comp2112.Project2;

import java.util.*;

class Node {
    String name;
    String type; 
    int size;
    Date lastModified;
    String accessLevel;
    List<Node> children;

    public Node(String name, String type, int size, Date lastModified, String accessLevel) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.lastModified = lastModified;
        this.accessLevel = accessLevel;
        this.children = new ArrayList<>();
    }
}