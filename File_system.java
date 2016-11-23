/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSystem;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author deneir-uy
 */
class Node {

    Node parent;
    HashMap<String, Node> children;
    Descriptor info;
    String key;

    Node(String address, Node parent, Date date) {
        this.parent = parent;
        this.children = new HashMap<String, Node>();
        this.info = new Descriptor(address, date, true);
        this.key = address;
    }

    Node(String filename, String contents, Date date, boolean isDirectory) {
        this.info = new Descriptor(filename, contents, date, isDirectory);
        this.parent = parent;
    }

    public void listChildren(String args) {
        Iterator iterate = children.entrySet().iterator();

        while (iterate.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) iterate.next();
            Node child = (Node) children.get(entry.getKey());
            String isDirectory = "File";
            
            if(child.info.isDirectory)
                isDirectory = "Folder";

            System.out.println(child.info.getFilename() + "\t\t" + isDirectory + "\t" +child.info.getDateCreated() + "\t" + child.info.getDateLastModified());

        }
    }
}

class Descriptor {

    private String filename;
    private String contents;
    private Date dateCreated;
    private Date dateLastModified;
    boolean isDirectory;

    Descriptor(String filename, String contents, Date date, boolean isDirectory) {
        this.filename = filename;
        this.contents = contents;
        this.isDirectory = isDirectory;
        this.dateCreated = date;
        this.dateLastModified = date;
    }

    Descriptor(String filename, Date date, boolean isDirectory) {
        this.filename = filename;
        this.contents = "";
        this.isDirectory = isDirectory;
        this.dateCreated = date;
        this.dateLastModified = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

}

class Tree {

    Node root, currentNode;

    Tree(Node root) {
        this.root = root;
        this.currentNode = this.root;
    }

    public String getPath(String path, Node currNode) {
        if (currNode.parent == null) {
            return path;
        } else {
            path = currNode.parent.key + "/" + path;
            return getPath(path, currNode.parent);
        }
    }

    public void makeDirectory(String[] key, Date date) {
        if (key.length == 1) {
            Node newNode = new Node(key[0], currentNode, date);
            currentNode.children.put(key[0], newNode);
        } else {
            Node currNode = root;

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currNode = changeDirectory(key[i]);
                }
            }
            Node newNode = new Node(key[key.length - 1], currNode, date);
            currNode.children.put(key[key.length - 1], newNode);
        }
    }

    public void removeDirectory(String[] key) {
        if (key.length == 1) {
            currentNode.children.remove(key[0]);
        } else {
            Node currNode = root;

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currNode = changeDirectory(key[i]);
                }
            }
            currNode.children.remove(key[key.length - 1]);
        }
    }

    public Node changeDirectory(String key) {
        if (key.equals("..")) {
            if (currentNode.parent != null) {
                return currentNode.parent;
            } else {
                return root;
            }
        }

        if (currentNode.children.containsKey(key)) {
            Node toChange = currentNode.children.get(key);
            if (toChange.info.isDirectory) {
                return toChange;
            } else {
                System.out.println("'" + "' is not a directory");
                return null;
            }
        } else {
            System.out.println("'" + key + "' directory not found");
            return null;
        }
    }

    private Node search(String[] key) {
        Node searchNode = root;

        for (int i = 0; i < key.length; i++) {
            searchNode = searchNode.children.get(key[i]);
        }

        return searchNode;

    }
}

class Simulation {

    String hostname;
    String path;
    String prompt;
    Tree tree;
    //Node currentDirectory;
    boolean exit;

    Simulation(Tree tree) {
        this.tree = tree;
        this.hostname = "user@hostname: ";
        this.tree.currentNode = tree.root;
        this.path = tree.currentNode.key;
        this.prompt = "/" + hostname + path + "/ $ ";
        this.exit = false;
    }

    public void showPrompt() {
        path = tree.getPath(tree.currentNode.key, tree.currentNode);
        prompt = hostname + "/" + path + "/ $ ";
        System.out.print(prompt);
    }

    public void takeCommand(String command) {
        String cmd, args = "";

        if (command.contains(" ")) {
            cmd = command.substring(0, command.indexOf(" "));
            args = command.substring(command.indexOf(" ") + 1, command.length());
        } else {
            cmd = command;
        }

        switch (cmd) {
            case "exit":
                this.exit = true;
                break;
            case "show":
                show(args);
                break;
            case "mkdir":
                makeDirectory(args);
                break;
            case "rmdir":
                removeDirectory(args);
                break;
            case "cd":
                changeDirectory(args);
                break;
            case "ls":
                listChildren(args);
                break;
            default:
                System.out.println(cmd + ": command not found");
                break;
        }
    }

    private void show(String args) {
        if (args == "") {
            return;
        }
        System.out.println(args);
    }

    private void makeDirectory(String args) {
        if (!args.isEmpty()) {
            String[] address = args.split("/");
            Date date = new Date();
            tree.makeDirectory(address, date);
        }
        else
            System.out.println("invalid: empty filename");
    }

    private void removeDirectory(String args) {
        if (!args.isEmpty()) {
            String[] address = args.split("/");
            Date date = new Date();
            tree.removeDirectory(address);
        }
        else
            System.out.println("invalid: empty filename");
    }

    private void changeDirectory(String args) {
        String[] address = args.split("/");
        Node preNode = tree.currentNode;

        for (int i = 0; i < address.length; i++) {
            if (!address[i].equals("")) {
                Node toChange = tree.changeDirectory(address[i]);

                if (toChange != null) {
                    tree.currentNode = toChange;
                } else {
                    tree.currentNode = preNode;
                    break;
                }
            }
        }

    }

    private void listChildren(String args) {
        if (!args.contains("/")) {
            tree.currentNode.listChildren(args);
        } else {
            String[] address = args.split("/");
            Node currNode = tree.root;
            
            for (int i = 0; i < address.length; i++) {
                if (!address[i].equals("")) {
                    currNode = tree.changeDirectory(address[i]);
                }
            }
            
            if(currNode != null)
                currNode.listChildren("");
        }
    }

}

public class File_system {

    public static void main(String[] args) {
        Date date = new Date();
        Node root = new Node("root", null, date);
        Tree tree = new Tree(root);
        Simulation simulate = new Simulation(tree);

        do {
            Scanner scan = new Scanner(System.in);
            String command;
            simulate.showPrompt();

            command = scan.nextLine();
            simulate.takeCommand(command);

        } while (!simulate.exit);
    }

}
