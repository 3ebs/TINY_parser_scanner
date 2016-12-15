package TINYparser;

import java.io.File;


public class TestGraphics {
    public static void createDotGraph()
{
    Graphviz gv = new Graphviz();  
    gv.addln(gv.start_graph());
    gv.addln("graph[rankdir=LR];\n");
    gv.addln("node [shape=box];\n");
    gv.addln("n001;\nn001 [label=\"write\"];\n");
    gv.addln("n003;\nn003 [label=\"read\n(x)\"];\n");
    gv.addln("node [shape=oval];\n");
    gv.addln("n002;\nn002 [label=\"x\"];\n");
    gv.addln("n001->n002 [constraint = false];\n");
    gv.addln("n001->n003;\n");


//     gv.addln("A -> B;");  
//     gv.addln("A -> C;");
//     gv.addln("A -> D;");

    gv.addln("overlap=false");
    gv.addln(gv.end_graph());  
    System.out.println(gv.getDotSource());  
  
    File out = new File("out.gif");  
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
}

    public static void main(String[] args) throws Exception {
        createDotGraph();
 }
    
}
