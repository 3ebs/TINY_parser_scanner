package TINYparser;


public class backEndParser 
{
    private String[] fileLines;
    private String matchInput;
    private boolean uFlag;
    private String currentLine;
    private int tokenIndex;
    private boolean pFlag;
    private boolean sFlag;
    private boolean eFlag;
    private boolean endTree;
    public backEndParser(String lines)
    {
        fileLines = lines.split("\n");
        currentLine = fileLines[0];
        pFlag = false;
        eFlag = false;
        sFlag = false;
        endTree = false;
        matchInput = "root";
        uFlag = false;
        tokenIndex = 0;
        //program();
    }
    public TreeNode program()
    {
        TreeNode currentNode = new TreeNode("root");
        currentNode = stmt_sequence(currentNode);
        return currentNode;
    }
    private TreeNode stmt_sequence(TreeNode node)
    {
        while(true)
        {
            node = statement(node);
            if(uFlag || eFlag || sFlag || endTree) break;
        }
        return node;
    }
    private TreeNode statement(TreeNode node)
    {
        
        String token = currentLine.split(",")[0];
        if (token.equals("read"))
        {
            return read_stmt(node);
        }
        else if (token.equals("write"))
        {
            return write_stmt(node);
        }
        else if (token.equals("\n"))
        {
            endTree = true;
            return node;
        }
        else if (token.equals(";"))
        {
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            return node;
        }
        else if(token.equals(":="))
        {
            return assign_stmt(node);
        }
        else if(token.equals("if"))
        {
            return if_stmt(node);
        }
        else if(token.equals("repeat"))
        {
            return repeat_stmt(node);
        }
        if(token.equals("until"))
            uFlag = true;
        else if(token.equals("else"))
        {
            sFlag = true;
            return node;
        }
        else if(token.equals("end"))
        {
            eFlag = true;
            return node;
        }
        else
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        return node.addChild();
    }
    private TreeNode read_stmt(TreeNode node)
    {
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        if (currentLine.split(",")[1].equals("ID")) 
        {
            node = node.addChild("read\n(" + currentLine.split(",")[0] + ")");
            while(!match(node.getData())) node = node.getParent();
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            return node;
        }
        return null;
    }
    private TreeNode write_stmt(TreeNode node)
    {
        //currentLine = fileLines[++tokenIndex];
        node = node.addChild("write");
        node = node.addChild();
        node = exp(node);
        while(!match(node.getData())) node = node.getParent();
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        return node;
    }
    private TreeNode if_stmt(TreeNode node)
    {
        node = node.addChild("IF");
        node = node.addChild();
        node = exp(node);
        tokenIndex += 2;
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[tokenIndex];
        matchInput = "IF";
        while(!match(node.getData())) node = node.getParent();
        node = stmt_sequence(node);
        if(sFlag)
        {
            sFlag = false;
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            matchInput = "IF";
            while(!match(node.getData())) node = node.getParent();
            node = stmt_sequence(node);
        }
        if(eFlag)
        {
            eFlag = false;
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            node = node.getParent();
            matchInput = node.getData();
        }
        return node;
    }
    private TreeNode assign_stmt(TreeNode node)
    {
        currentLine = fileLines[tokenIndex-1];
        node.setData("assign\n(" + currentLine.split(",")[0] + ")");
        node = node.addChild();
        node = exp(node);
        while(!match(node.getData())) node = node.getParent();
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        return node;
    }
    private TreeNode repeat_stmt(TreeNode node)
    {
        node = node.addChild("repeat");
        matchInput = "repeat";
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        node = stmt_sequence(node);
        uFlag = false;
        //node = node.addChild();
        node = exp(node);
        while(!match(node.getData())) node = node.getParent();
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        node = node.getParent();
        matchInput = node.getData();
        return node;
    }
    private TreeNode exp(TreeNode node)
    {
        TreeNode leftNode = node.addChild();
        TreeNode rightNode = node.addChild();
        boolean flag = false;
        leftNode = simple_exp(leftNode);
        String comp_op = comparison_op();
        if (comp_op != null) 
        {
            node.setData("OP\n(" + currentLine.split(",")[0] + ")");
            //if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            rightNode = simple_exp(rightNode);
        }
        if(leftNode.getData().startsWith("OP")) flag = true;
        if(node.getData() == null) return node.setNode(leftNode, flag);
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode simple_exp(TreeNode node)
    {
        if(currentLine.startsWith("(")) pFlag = true;
        if(tokenIndex < fileLines.length-1 && !pFlag) currentLine = fileLines[++tokenIndex];
        boolean flag = false;
        TreeNode leftNode = node.addChild();
        TreeNode rightNode = node.addChild();
        leftNode = term(leftNode);
        while(true)
        {
            String op = addop();
            if (op == null)break;
            node.setData("OP\n(" + currentLine.split(",")[0] + ")");
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            rightNode = term(rightNode);
        }
        if(leftNode.getData().startsWith("OP")) flag = true;
        if(node.getData() == null) return node.setNode(leftNode, flag);
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode term(TreeNode node)
    {
        TreeNode leftNode = node.addChild();
        TreeNode rightNode = node.addChild();
        boolean flag = false;
        leftNode = factor(leftNode);
        while(true)
        {
            String op = mulop();
            if (op == null)break;
            node.setData("OP\n(" + currentLine.split(",")[0] + ")");
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            rightNode = factor(rightNode);
        }
        if(leftNode.getData().startsWith("OP")) flag = true;
        if(node.getData() == null) return node.setNode(leftNode, flag);
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode factor(TreeNode node)
    {
        String fact = currentLine.split(",")[1];
        if (fact.equals("PARENTHESEOPEN")) 
        {
            pFlag = false;
            currentLine = "w" + currentLine;
            node = exp(node);
            return node;

        }
        else if(fact.equals("NUM"))
        {
            node.setData("Const\n(" + currentLine.split(",")[0] + ")");
            return node;
        }
        else if(fact.equals("ID"))
        {
            node.setData("ID\n(" + currentLine.split(",")[0] + ")");
            return node;
        }
        return null;
    }
    private String comparison_op()
    {
        if(tokenIndex < fileLines.length-1) 
        {
            String op = fileLines[tokenIndex+1].split(",")[0];
            if (op.equals("<") || op.equals("=")) {
                currentLine = fileLines[++tokenIndex];
                return op;
            }
        }
        return null;
    }
    private String addop()
    {
        if(tokenIndex < fileLines.length-1) 
        {
            String op = fileLines[tokenIndex+1].split(",")[0];
            if (op.equals("+") || op.equals("-")) {
                currentLine = fileLines[++tokenIndex];
                return op;
            }
        }
        return null;
    }
    private String mulop()
    {
        if(tokenIndex < fileLines.length-1) 
        {
            String op = fileLines[tokenIndex+1].split(",")[0];
            if (op.equals("*") || op.equals("/")) {
                currentLine = fileLines[++tokenIndex];
                return op;
            }
        }
        return null;
    }
    private boolean match(String s)
    {
        if(s.equals(matchInput))
        {
            return true;
        }
        return false;
    }
}
