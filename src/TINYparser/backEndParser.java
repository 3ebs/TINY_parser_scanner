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
    private TreeNode currentNode;
    private boolean flagFactor;
    private boolean flagTerm;
    private boolean parentheseFlag;
    public backEndParser(String lines)
    {
        fileLines = lines.split("\r\n");
        currentLine = fileLines[0];
        tokenIndex = 0;
        currentNode = new TreeNode("root");
        program();
    }
    private void program()
    {
        stmt_sequence();
    }
    private void stmt_sequence()
    {
        while(true)
        {
            statement();
        }
    }
    private void statement()
    {
        String token = currentLine.split(",")[0];
        if (token.equals("read")) 
        {
            read_stmt();
        }
        else if (token.equals("write"))
        {
            write_stmt();
        }
    }
    private void read_stmt()
    {
        currentLine = fileLines[++tokenIndex];
        if (currentLine.split(",")[1].equals("ID")) 
        {
            currentNode = currentNode.addChild("read\n(" + currentLine.split(",")[0] + ")");
            currentLine = fileLines[++tokenIndex];
        }
    }
    private void write_stmt()
    {
        //currentLine = fileLines[++tokenIndex];
        currentNode = currentNode.addChild("write");
        String e = exp();
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
    private String exp()
    {
        currentLine = fileLines[++tokenIndex];
        String se = simple_exp();
        /*String comp_op = comparison_op();
        if (comp_op != null) 
        {
            se += simple_exp();
        }*/
        return se;
    }
    private String simple_exp()
    {
        //if (!parentheseFlag) currentNode = currentNode.addChild();
        currentNode = currentNode.addChild();
        int counter = 0;
        flagTerm = false;
        String t = term();
        while(true)
        {
            String op = addop();
            if (op == null)break;
            //currentNode = currentNode.getParent();
            currentNode.setData("OP\n(" + currentLine.split(",")[0] + ")");
            currentLine = fileLines[++tokenIndex];
            //currentNode = currentNode.addChild();
            counter++;
            t = term();
        }
        for(int i = 0; i <= counter && !flagFactor; i++)
        {
            currentNode = currentNode.getParent();
        }
        return t;
    }
    private String term()
    {
        currentNode = currentNode.addChild();
        int count = 0;
        flagFactor = false;
        String f = factor();
        while(true)
        {
            String op = mulop();
            if (op == null)break;
            currentNode = currentNode.getParent();
            currentNode.setData("OP\n(" + currentLine.split(",")[0] + ")");
            currentLine = fileLines[++tokenIndex];
            currentNode = currentNode.addChild();
            count++;
            f = factor();
        }
        for(int i = 0; i <= count && !flagFactor; i++)
        {
            currentNode = currentNode.getParent();
        }
        return f;
    }
    private String factor()
    {
        String fact = currentLine.split(",")[1];
        if (fact.equals("PARENTHESEOPEN")) 
        {
            //currentNode = currentNode.addChild();
            //parentheseFlag = true;
            String st = "(" + exp() + ")";
            //parentheseFlag = false;
            flagFactor = true;
            return st;

        }
        else if(fact.equals("NUMBER"))
        {
            currentNode.setData("Const\n(" + currentLine.split(",")[0] + ")");
            return currentLine.split(",")[0];
        }
         else if(fact.equals("ID"))
        {
            currentNode.setData("ID\n(" + currentLine.split(",")[0] + ")");
            return currentLine.split(",")[0];
        }
        return null;
    }
    private String comparison_op()
    {
        String op = currentLine.split(",")[0];
        if (op.equals("<") || op.equals("=")) {
            return op;
        }
        return null;
    }
    private String addop()
    {
        String op = currentLine.split(",")[0];
        if (op.equals("+") || op.equals("-")) {
            return op;
        }
        return null;
    }
    private String mulop()
    {
        if(tokenIndex != fileLines.length-1) currentLine = fileLines[++tokenIndex];
        String op = currentLine.split(",")[0];
        if (op.equals("*") || op.equals("/")) {
            return op;
        }
        return null;
    }
    
    
    
}
