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
            currentNode.addChild("read\n(" + currentLine.split(",")[0] + ")");
            currentLine = fileLines[++tokenIndex];
        }
    }
    private void write_stmt()
    {
        exp();
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
        String se = simple_exp();
        String comp_op = comparison_op();
        if (comp_op != null) 
        {
            se += simple_exp();
        }
    }
    private String simple_exp()
    {
        term();
        while(true)
        {
            String op = addop();
            if (op == null)break;
            term();
        }
    }
    private void term()
    {
        factor();
        while(true)
        {
            String op = mulop();
            if (op == null)break;
            factor();
        }
    }
    private String factor()
    {
        currentLine = fileLines[++tokenIndex];
        String fact = currentLine.split(",")[1];
        if (fact.equals("PARENTHESEOPEN")) 
        {
            exp();
        }
        else if(fact.equals("NUMBER"))
        {
            return currentLine.split(",")[0];
        }
         else if(fact.equals("ID"))
        {
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
        String op = currentLine.split(",")[0];
        if (op.equals("*") || op.equals("/")) {
            return op;
        }
        return null;
    }
    
    
    
}
