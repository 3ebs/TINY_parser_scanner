package TINYparser;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;


public class TreeNode
{	
    String data;
    TreeNode parent;
    int level;
    LinkedList<TreeNode> children;
    public TreeNode(String data, int lvl) {
        this.data = data;
        this.level = lvl;
        this.children = new LinkedList<TreeNode>();
    }
    public TreeNode(int lvl) {
        this.level = lvl;
        this.children = new LinkedList<TreeNode>();
    }
    
    
    public void printTree()
    {
        int maxLevel = 0;
        maxLevel = getMaxLevel(this, maxLevel);
        maxLevel++;
        int [] noNodesInLevel = new int [maxLevel];
        noNodesInLevel = getNoNodesInLevel(this,maxLevel,noNodesInLevel);
        TreeNode[][] Tree = new TreeNode[maxLevel][];
        for (int i = 0; i < maxLevel; i++)
            Tree[i] = new TreeNode[noNodesInLevel[i]];
        Tree = setNodeInArray(Tree,this);
        String[] nodes = new String [maxLevel];
        nodes = levelAcc(Tree);
        createDotGraph(nodes ,this);
        
    }
    public void createDotGraph(String [] nodes, TreeNode node)
    {
        String acc = "rankdir=\"LR\";\n" +
                "splines = false;\n" +
                "style=invis;";
        for (int i = nodes.length-1; i > 0; i--) {
            acc += "subgraph cluster" + i + " {" + nodes[i] + "}";
        }
        acc += drawToParent(node,"");
        Graphviz gv = new Graphviz();  
        gv.addln(gv.start_graph());
        gv.addln(acc);
        gv.addln(gv.end_graph());  
        System.out.println(gv.getDotSource());  

        File out = new File("out.gif");
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
    }
    
    public String drawToParent(TreeNode tmp, String drawToParent)
    {
        if (tmp.getLevel() > 1) {
            drawToParent += "\"" + tmp.getData() + tmp.getParent().getData() + tmp.getLevel() 
                    + "\" -> \"" + tmp.getParent().getData() + tmp.getParent().getParent().getData() 
                    + tmp.getParent().getLevel() + "\"[constraint=false];";
        }
        if (tmp.isLeaf())
            return drawToParent;
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            drawToParent = drawToParent(tmp2,drawToParent);
        }
        return drawToParent;
    }
    public TreeNode[][] setNodeInArray(TreeNode[][] tree, TreeNode tmp)
    {
        int level = tmp.getLevel();
        tree[level][getEmptyElement(tree[level])] = tmp;
        if (tmp.isLeaf()) 
        {
            return tree;
        }
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            tree = setNodeInArray(tree,tmp2);
        }
        return tree;
    } 
    public int getEmptyElement(TreeNode[] arr)
    {
        int emptyIndex = 0;
        for (emptyIndex = 0; emptyIndex < arr.length; emptyIndex++) {
            if (arr[emptyIndex] == null) {
                return emptyIndex;
            }
        }
        return emptyIndex;
    }
    
    private int[] getNoNodesInLevel(TreeNode tmp, int maxLevel, int [] noNodesInLevel)
    {
        noNodesInLevel[tmp.getLevel()]++;
        if (tmp.isLeaf()) 
        {
            return noNodesInLevel;
        }
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            noNodesInLevel = getNoNodesInLevel(tmp2,maxLevel,noNodesInLevel);
        }
        return noNodesInLevel;
    }
    private int getMaxLevel(TreeNode tmp, int maxLevel)
    {   
        if (tmp.isLeaf()) 
        {
           System.out.println(tmp.data + "\t\t" + tmp.level);
            if ( maxLevel < tmp.level) 
                maxLevel = tmp.level;
            return maxLevel;
        }
        else
        {
            System.out.println(tmp.data + "\t\t" + tmp.level);
        }
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            maxLevel = getMaxLevel(tmp2,maxLevel);
            
        }
        return maxLevel;
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
        TreeNode childNode = new TreeNode(childData, this.level+1);
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
        TreeNode childNode = new TreeNode(this.level+1);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    public void setData(String d)
    {
        data = d;
    }
    public TreeNode setNode(TreeNode t1, boolean flag)
    {
        this.children.clear();
        if(flag) 
        {
            this.children = t1.children;
            for(int i=0; i < this.children.size(); i++)
            {
                children.get(i).setLevel(t1.level);
            }
        }
        this.data = t1.getData();
        return this;
    }
    public void setLevel(int x)
    {
        this.level = x;
    }
    public int getLevel()
    {
        return this.level;
    }
    public String getData()
    {
        return this.data;
    }
    public void createTreeGraph()
    {
        Graphviz gv = new Graphviz();
        gv.addln(gv.start_graph());
        gv.addln("graph[rankdir=LR];\n");
        
    //    gv.addln("node [shape=box];\n");
    //    gv.addln("n001;\nn001 [label=\"write\"];\n");
    //    gv.addln("n003;\nn003 [label=\"read\n(x)\"];\n");
    //    gv.addln("node [shape=oval];\n");
    //    gv.addln("n002;\nn002 [label=\"x\"];\n");
    //    gv.addln("n001->n002 [constraint = false];\n");
    //    gv.addln("n001->n003;\n");


    //     gv.addln("A -> B;");  
    //     gv.addln("A -> C;");
    //     gv.addln("A -> D;");

        gv.addln("overlap=false");
        gv.addln(gv.end_graph());  
        System.out.println(gv.getDotSource());  

        File out = new File("out.gif");  
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
    }
    
    public int getNoLevels ()
    {
        ListIterator<TreeNode> listIterator = this.children.listIterator();
        int countLevels = 0;
        while (listIterator.hasNext()) 
        {
            TreeNode tmp2 = listIterator.next();
            if (countLevels < tmp2.getLevel()) 
                countLevels = tmp2.getLevel();
        }
        return countLevels;
    }
    
    

    
    
    
    public String[] levelAcc(TreeNode [][]TreeNode)
    {
        String draw = ""; 
        int levels = TreeNode.length;
        String[] levelData = new String[levels];
        Arrays.fill(levelData, "");
        for (int i = 1; i < levels; i++) {
            draw = "";
            for (int j = 0; j < TreeNode[i].length; j++) {
                if (j != 0) 
                    draw += " -> \"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() + "\"";
                else
                    draw += "\"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() + "\"";
                if (TreeNode[i][j].getData().startsWith("IF")
                        ||TreeNode[i][j].getData().startsWith("read")
                        ||TreeNode[i][j].getData().startsWith("assign")
                        ||TreeNode[i][j].getData().startsWith("repeat")
                        ||TreeNode[i][j].getData().startsWith("write")) {
                    levelData[i] += "node[shape = box] \"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() +  "\" [label = \"" + TreeNode[i][j].getData() + "\"];";
                    //levelData[i] += "node[shape = box] \"" + i + j + "\" [label = \"" + TreeNode[i][j].getData() + "\"];";
                }
                else
                {
                    levelData[i] += "node[shape = oval] \"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() + "\" [label = \"" + TreeNode[i][j].getData() + "\"];";
                    //levelData[i] += "node[shape = oval] \"" + i + j + "\" [label = \"" + TreeNode[i][j].getData() + "\"];";
                }
            }
            
            levelData[i] += draw + "[style=invis];";
        }
        return levelData;
    }
    
    
    
    
    
}

