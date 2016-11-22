/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author deneir-uy
 */
class Node {
    Node parent;
    HashMap<Node, String> children;
    boolean isDirectory;
    File file;
    
    Node() {
        this.isDirectory = true;
    }
    
    Node(File file) {
        this.isDirectory = false;
        this.file = file;
    }

}

class File {
    String filename;
    String contents;
    
    File(String filename , String contents) {
        this.filename = filename;
        this.contents = contents;
    }
}

class Tree {
    
    private Node root;

    Tree(Node root) {
        this.root = root;
    }

    public void insert(Node node) {
        
    }

    public void delete(String key) {

    }

    public void search(String key) {

    }
}

class Simulation {
    String hostname;
    String currentDirectory;
    String prompt;
    Tree fileTree;
    boolean exit;

    Simulation(Tree fileTree) {
        this.fileTree = fileTree;
        this.hostname = "user@hostname: ";
        this.currentDirectory = "/";
        this.prompt = hostname + currentDirectory + " $ ";
        this.exit = false;
    }
    
    public void showPrompt() {
        System.out.print(prompt);
    }
    
    public void takeCommand(String command) {
        String cmd = command.substring(0, command.indexOf(" "));
        String args = command.substring(command.indexOf(" ") + 1, command.length());
        
        switch(cmd){
            case "print":
                print(args);
                break;
        }
    }
    
    private void print(String args) {
        System.out.println(args);
    }
    
}

public class File_system {

    public static void main(String[] args) {
        Node root = new Node();
        Tree fileTree = new Tree(root);
        Simulation simulate = new Simulation(fileTree);
        
        do{
            Scanner scan = new Scanner(System.in);
            String command;
            simulate.showPrompt();
            
            command = scan.nextLine();
            simulate.takeCommand(command);
            
        } while (!simulate.exit);
    }

}
