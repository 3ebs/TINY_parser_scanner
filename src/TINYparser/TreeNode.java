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
        for (int i = 0; i < nodes.length; i++) {
            acc += "subgraph cluster" + i + " {" + nodes[i] + "}";
        }
        acc += drawToParent(node,"");
        Graphviz gv = new Graphviz();  
        gv.addln(gv.start_graph());
        gv.addln(acc);
        gv.addln("overlap=false");
        gv.addln(gv.end_graph());  
        System.out.println(gv.getDotSource());  

        File out = new File("out.gif");
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
    }
    
    public String drawToParent(TreeNode tmp, String draw)
    {
        int i = 0;
        String x;
        boolean ifFlag = false;
        TreeNode child = new TreeNode(0);
        TreeNode next_child = new TreeNode(0);
        TreeNode first_child = tmp;
        if (tmp.isLeaf())
            return draw;
        if(first_child.getData().endsWith("(=)") || first_child.getData().endsWith("(<)") || first_child.getData().endsWith("(+)") || first_child.getData().endsWith("(-)") || first_child.getData().endsWith("(*)") || first_child.getData().endsWith("(/)"))
        {
            for (int j = 0; j < 2; j++) 
            {
                child = tmp;
                draw += "\"" + child.getData() + child.getParent().getData() + child.getLevel() + "\"";
                draw += "->";
                next_child = tmp.getChildren().get(j);
                draw += "\"" + next_child.getData() + next_child.getParent().getData() + next_child.getLevel() + "\"";
                draw += "[constraint=false];\n";
                //x = tmp.getChildren().get(i).getData();
            }
            if(first_child.getData().endsWith("(=)") || first_child.getData().endsWith("(<)") )
            {
                child = first_child.getParent();
                draw += "\"" + child.getData() + child.getParent().getData() + child.getLevel() + "\"";
                draw += "->";
                if (first_child == child.getChildren().get(0))
                    next_child = child.getChildren().get(1);
                else
                    next_child = first_child;
                draw += "\"" + next_child.getData() + next_child.getParent().getData() + next_child.getLevel() + "\"";
                draw += "[constraint=false];\n";
            }
            return draw;
        }
        if(!tmp.getData().startsWith("read"))
            first_child = tmp.getChildren().get(i);
        else
            return draw;        
        if(!tmp.getData().equals("root") && !tmp.getData().startsWith("read"))
        {
            draw += "\"" + tmp.getData() + tmp.getParent().getData() + tmp.getLevel() + "\"";
            draw += "->";
            first_child = tmp.getChildren().get(i);
            draw += "\"" + first_child.getData() + first_child.getParent().getData() + first_child.getLevel() + "\"[constraint=false];\n";
        }
        if(!tmp.getData().equals("root"))
            if(tmp.getParent().getChildren().get(1) == tmp && tmp.getParent().getData().equals("IF"))
            {
                x = tmp.getData();
                tmp = tmp.getParent();
                ifFlag = true;
                i++;
            }
            else
                x = tmp.getChildren().get(i).getData();
        else
            x = tmp.getChildren().get(i).getData();
        while((x.startsWith("read") || x.startsWith("write") || x.startsWith("IF") || x.startsWith("repeat") || x.startsWith("assign")) && i < tmp.countChildren()-1)
        {
            if(tmp.getChildren().get(i+1).getData().startsWith("OP")) break;
            child = tmp.getChildren().get(i);
            draw += "\"" + child.getData() + child.getParent().getData() + child.getLevel() + "\"";
            draw += "->";
            next_child = tmp.getChildren().get(++i);
            draw += "\"" + next_child.getData() + next_child.getParent().getData() + next_child.getLevel() + "\"";
            draw += ";\n";
            x = tmp.getChildren().get(i).getData();
        }
        if(ifFlag)
        {
            tmp = tmp.getChildren().get(1);
            ifFlag = false;
        }
        ListIterator<TreeNode> listIterator = tmp.children.listIterator();
        while (listIterator.hasNext()) 
        {
            first_child = listIterator.next();
            draw = drawToParent(first_child,draw);
            System.out.println(draw);
        }
        return draw;
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
        int count;
        String[] levelData = new String[levels];
        Arrays.fill(levelData, "");
        for (int i = 1; i < levels; i++) {
            draw = "";
            count = 0;
            for (int j = 0; j < TreeNode[i].length; j++) {
                if (j != 0) 
                    draw += " -> \"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() + "\"";
                else
                    draw += "\"" + TreeNode[i][j].getData() + TreeNode[i][j].getParent().getData() + TreeNode[i][j].getLevel() + "\"";
                count++;

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
            if (count > 1)
                levelData[i] += draw + "[style=invis];";
            else
                levelData[i] += draw;
                
        }
        return levelData;
    } 
}

