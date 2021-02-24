/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern;

import java.util.ArrayList;

/**
 *
 * @author fabianjose
 */
public class Tree {

    private int id;
    private int port;
    private String add;
    private ArrayList<Tree> children;

    public Tree(int id) {
        this.id = id;
    }

    
    public Tree(int id, String add, int port) {
        this.id = id;
        this.add = add;
        this.port = port;
    }

    public Tree(int id, ArrayList<Tree> children) {
        this.id = id;
        this.children = children;
    }

    public ArrayList<Tree> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Tree> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd() {
        return add;
    }

    public int getPort() {
        return port;
    }
}
