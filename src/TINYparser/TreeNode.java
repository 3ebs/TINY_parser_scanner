/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TINYparser;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author MMZMM
 */
public class TreeNode
{	
    String data;
    TreeNode parent;
    LinkedList<TreeNode> children;
    public TreeNode(String data) {
        this.data = data;
        this.children = new LinkedList<TreeNode>();
    }
    public TreeNode() {
        this.children = new LinkedList<TreeNode>();
    }
    public void printTree()
    {
        print(this);
    }
    private void print(TreeNode tmp)
    {   
        if (tmp.isLeaf()) 
        {
            System.out.println(tmp.data);
            return;
        }
        else
        {
            System.out.println(tmp.data);
        }
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            print(tmp2);
        }
    }
    private boolean isLeaf()
    {
        int counter =this.countChildren();
        if (counter>0) 
        {
            return false;
        }
        return true;
    }
    private int countChildren()
    {
        int counter = 0;
        ListIterator<TreeNode> listIterator = this.children.listIterator();
        while (listIterator.hasNext()) 
        {
            counter++;
            listIterator.next();
        }
        return counter;
    }
    public TreeNode addChild(String childData) {
        TreeNode childNode = new TreeNode(childData);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    public TreeNode getParent()
    {
        return parent;
    }
    public LinkedList<TreeNode> getChildren()
    {
        return this.children;
    }
    public TreeNode setChildren(TreeNode t1, TreeNode t2)
    {
        this.children.clear();
        //this.data = d;
        this.children.add(t1);
        this.children.add(t2);
        return this;
    }
    public TreeNode setChildren(TreeNode t1)
    {
        this.children.clear();
        this.children.add(t1);
        return this;
    }
    public TreeNode addChild() {
        TreeNode childNode = new TreeNode();
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    public void setData(String d)
    {
        data = d;
    }
    public TreeNode setNode(String d)
    {
        this.data = d;
        this.children.clear();
        return this;
    }
    public String getData()
    {
        return this.data;
    }
}

