/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TINYparser;

/**
 *
 * @author MMZMM
 */
public class backEndParser 
{
    private String[] fileLines;
    private String currentLine;
    private int tokenIndex;
    public backEndParser(String lines)
    {
        fileLines = lines.split("\n");
        currentLine = fileLines[0];
        tokenIndex = 0;
        program();
    }
    private void program()
    {
        TreeNode currentNode = new TreeNode("root");
        stmt_sequence(currentNode);
    }
    private void stmt_sequence(TreeNode node)
    {
        while(true)
        {
            node = statement(node);
        }
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
        return null;
    }
    private TreeNode read_stmt(TreeNode node)
    {
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
        if (currentLine.split(",")[1].equals("ID")) 
        {
            node = node.addChild("read\n(" + currentLine.split(",")[0] + ")");
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
        return node;
    }
    private void if_stmt()
    {
        
    }
    private void assign_stmt()
    {
        
    }
    private void repeat_stmt()
    {
        
    }
    private TreeNode exp(TreeNode node)
    {
        TreeNode leftNode = node.addChild();
        TreeNode rightNode = node.addChild();
        leftNode = simple_exp(leftNode);
        String comp_op = comparison_op();
        if (comp_op != null) 
        {
            node.setData("OP\n(" + "<" + ")");
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            rightNode = simple_exp(rightNode);
        }
        if(node.getData() == null) return leftNode;
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode simple_exp(TreeNode node)
    {
        if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
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
        if(node.getData() == null) return leftNode;
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode term(TreeNode node)
    {
        TreeNode leftNode = node.addChild();
        TreeNode rightNode = node.addChild();
        leftNode = factor(leftNode);
        while(true)
        {
            String op = mulop();
            if (op == null)break;
            node.setData("OP\n(" + currentLine.split(",")[0] + ")");
            if(tokenIndex < fileLines.length-1) currentLine = fileLines[++tokenIndex];
            rightNode = factor(rightNode);
        }
        if(node.getData() == null) return leftNode;
        else return node.setChildren(leftNode, rightNode);
    }
    private TreeNode factor(TreeNode node)
    {
        String fact = currentLine.split(",")[1];
        if (fact.equals("PARENTHESEOPEN")) 
        {
            //String st = "(" + exp() + ")";
            //return st;

        }
        else if(fact.equals("NUMBER"))
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
    
    
    
}
