/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSystem;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        this.children = new HashMap<>();
        this.info = new Descriptor(address, date);
        this.key = address;
    }

    Node(String filename, Node parent, String contents, Date date) {
        this.parent = parent;
        this.info = new Descriptor(filename, contents, date);

    }

    public void listChildren() {
        children.entrySet().stream().map((entry) -> (Node) children.get(entry.getKey())).forEach((child) -> {
            String isDirectory = "File";

            if (child.info.isDirectory) {
                isDirectory = "Folder";
            }

            System.out.println(child.info.getFilename() + "\t\t" + isDirectory + "\t" + child.info.getDateCreated() + "\t" + child.info.getDateLastModified());
        });
    }
}

class Descriptor {

    private String filename;
    private String contents;
    private Date dateCreated;
    private Date dateLastModified;
    boolean isDirectory;

    Descriptor(String filename, String contents, Date date) {
        this.filename = filename;
        this.contents = contents;
        this.dateCreated = date;
        this.dateLastModified = date;
        this.isDirectory = false;
    }

    Descriptor(String filename, Date date) {
        this.filename = filename;
        this.contents = "";
        this.dateCreated = date;
        this.dateLastModified = date;
        this.isDirectory = true;
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
            return getPath(path.trim(), currNode.parent);
        }
    }

    public void makeDirectory(String[] key, Date date) {
        String name = key[key.length - 1].substring(key[key.length - 1].lastIndexOf(">") + 1).trim();
        if (key.length == 1) {
            Node newNode = new Node(key[0].trim(), currentNode, date);
            currentNode.children.put(key[0].trim(), newNode);
        } else {

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currentNode = changeDirectory(key[i].trim());
                }
            }
            Node newNode = new Node(name, currentNode, date);
            
            if(currentNode == null) {
                return;
            }
            
            currentNode.children.put(name, newNode);
        }
    }

    public void removeDirectory(String[] key) {

        if (key.length == 1) {
            currentNode.children.remove(key[0].trim());
        } else {

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currentNode = changeDirectory(key[i].trim());
                }
            }
            
            if(currentNode == null) {
                return;
            }
            
            currentNode.children.remove(key[key.length - 1].trim());
        }
    }

    public void makeFile(String[] key, String contents, Date date) {
        String name = key[key.length - 1].substring(key[key.length - 1].lastIndexOf(">") + 1).trim();

        if (key.length == 1) {
            Node newNode = new Node(name, currentNode, contents, date);
            currentNode.children.put(name, newNode);
        } else {
            Node currNode = root;

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currNode = changeDirectory(key[i].trim());
                }
            }
            Node newNode = new Node(name, currNode, date);
            currNode.children.put(name, newNode);
        }
    }

    public void updateFile(String[] key, String contents, Date date) {
        String name = key[key.length - 1].substring(key[key.length - 1].lastIndexOf(">") + 1).trim();
        if (key.length == 1) {
            currentNode.children.get(name).info.setContents(contents);
            currentNode.children.get(name).info.setDateLastModified(date);

        } else {
            Node currNode = root;

            for (int i = 0; i < key.length - 1; i++) {
                if (!key[i].equals("")) {
                    currNode = changeDirectory(key[i].trim());
                }
            }
            currNode.children.get(name).info.setContents(contents);
            currNode.children.get(name).info.setDateLastModified(date);
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
                System.out.println("'" + key + "' is not a directory");
                return null;
            }
        } else if (key.equals("root")) {
            return root;
        } else {
            System.out.println("'" + key + "' directory not found");
            return null;
        }
    }

    private Node search(String[] key) {
        Node searchNode = root;

        for (String key1 : key) {
            searchNode = searchNode.children.get(key1);
        }

        return searchNode;

    }
}

class Simulation {

    String hostname;
    String path;
    String prompt;
    Tree tree;
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
        command = command.trim();
        if (command.contains(" ")) {
            cmd = command.substring(0, command.indexOf(" "));
            args = command.substring(command.indexOf(" ") + 1, command.length());
        } else {
            cmd = command;
        }

        switch (cmd) {
            case "ext":
                this.exit = true;
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
            case "cat":
                createFile(args);
                break;
            case "show":
                showFile(args);
                break;
            case "":
                break;
            default:
                System.out.println(cmd + ": command not found");
                break;
        }
    }

    private void makeDirectory(String args) {
        if (!args.isEmpty()) {
            String[] address = args.split("/");
            Date date = new Date();
            Node preNode = tree.currentNode;
            
            if (address[0].equals("root")) {
                tree.currentNode = tree.root;
            }
            
            tree.makeDirectory(address, date);
            tree.currentNode = preNode;
        } else {
            System.out.println("invalid: empty filename");
        }
    }

    private void removeDirectory(String args) {
        if (!args.isEmpty()) {
            String[] address = args.split("/");
            Node preNode = tree.currentNode;
            
            if (address[0].equals("root")) {
                tree.currentNode = tree.root;
            }
            
            tree.removeDirectory(address);
            tree.currentNode = preNode;
        } else {
            System.out.println("invalid: empty filename");
        }
    }

    private void changeDirectory(String args) {
        if (!args.isEmpty()) {
            String[] address = args.split("/");
            Node preNode = tree.currentNode;

            if (address[0].equals("root")) {
                tree.currentNode = tree.root;
            }

            for (String addres : address) {
                if (!addres.equals("")) {
                    Node toChange = tree.changeDirectory(addres);
                    if (toChange != null) {
                        tree.currentNode = toChange;
                    } else {
                        tree.currentNode = preNode;
                        break;
                    }
                }
            }
        } else {
            System.out.println("invalid: empty filename");
        }

    }

    private void listChildren(String args) {
        if (args.isEmpty()) {
            tree.currentNode.listChildren();
        } else {
            String[] address = args.split("/");
            Node preNode = tree.currentNode;
            
            if (address[0].equals("root")) {
                tree.currentNode = tree.root;
            }

            for (String addres : address) {
                if (!addres.equals("")) {
                    tree.currentNode = tree.changeDirectory(addres);
                }
            }

            if (tree.currentNode != null) {
                tree.currentNode.listChildren();
            }
            
            tree.currentNode = preNode;
        }
    }

    private void createFile(String args) {
        String[] address = args.split("/");
        String key = args.substring(args.lastIndexOf(">") + 1).trim();
        Node preNode = tree.currentNode;

        if (args.contains(">")) {
            JFrame frame = new JFrame(key);
            JTextArea txtarFile = new JTextArea(20, 40);
            JScrollPane scrlpaneFile = new JScrollPane(txtarFile);
            JPanel mainPanel = new JPanel();
            JButton save = new JButton("Save");

            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            frame.setLayout(new GridLayout(1, 0));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainPanel.add(scrlpaneFile);
            frame.add(mainPanel);
            mainPanel.add(save);

            if (args.contains(">>")) {
                txtarFile.setText(tree.currentNode.children.get(key).info.getContents());

                save.addActionListener((ActionEvent e) -> {
                    saveFileContents(address, txtarFile, frame, key, false);
                });

                frame.pack();
                frame.setVisible(true);
            } else {

                save.addActionListener((ActionEvent e) -> {
                    saveFileContents(address, txtarFile, frame, key, true);
                });

                frame.pack();
                frame.setVisible(true);
            }
        } else {
            System.out.println("Invalid: arguments (missing '>' or '>>')");
        }
    }

    private void saveFileContents(String[] key, JTextArea txtar, Window frame, String filename, boolean isNewFile) {
        Date date = new Date();
        String contents = "";
        contents = txtar.getText();

        if (isNewFile) {
            tree.makeFile(key, contents, date);
        } else {
            tree.updateFile(key, contents, date);
        }

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void showFile(String args) {
        String[] address = args.split("/");
        String key = args.substring(args.lastIndexOf(">") + 1).trim();
        Node preNode = tree.currentNode;
        JFrame frame = new JFrame(key);
        JTextArea txtarFile = new JTextArea(20, 40);
        JScrollPane scrlpaneFile = new JScrollPane(txtarFile);
        JPanel mainPanel = new JPanel();

        if (!args.contains("/")) {
            txtarFile.setText(tree.currentNode.children.get(key).info.getContents());
        } else {
            for (int i = 0; i < address.length; i++) {

            }
        }

        txtarFile.setEditable(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.setLayout(new GridLayout(1, 0));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel.add(scrlpaneFile);
        frame.add(mainPanel);

        frame.pack();
        frame.setVisible(true);
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
