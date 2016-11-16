/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSystem;

import java.util.List;

/**
 *
 * @author deneir-uy
 */

/*
 *
 * Mao pa jud ni, sir. I was sick.
 *
 */
class Node {
    Node parent;
    List<Node> children;
    boolean isDirectory;
    
}

class Tree {
    private Node root;
    
    Tree(Node root) {
        this.root = root;
    }
    
    public void insert() {
        
    }
    
    public void delete() {
        
    }
    
    public void search() {
        
    }
}

public class File_system {
    
    public static void main(String[] args) {
        Node root = new Node();
        Tree fileSystem = new Tree(root);
    }
    
    
}
