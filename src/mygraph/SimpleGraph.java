package mygraph;
import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.Lattice2DGenerator;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.*;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.*;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
 
public class SimpleGraph extends JFrame {
    
    
    public SimpleGraph() {
        super("Mój pierwszy graf");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Graph g = getGraph();
        VisualizationViewer<Integer, String> vv =
                new VisualizationViewer<Integer, String>(new FRLayout(g),
                new Dimension(300, 300));
        
        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };
        
        
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);
        getContentPane().add(vv);

        pack();
        setSize(new Dimension(600, 600));
        setVisible(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
 
  public Graph getGraph() {
        Graph<Integer, String> g = new SparseGraph<Integer, String>();

        Random randGen = new Random();
        int numVer = 30;
        
        Integer edgeName = 1;
        for(Integer i = 1; i <= numVer; i++){
            g.addVertex(i);
            if(g.getVertexCount() == 1) continue;
            if(g.getVertexCount() == 2){
                g.addEdge(edgeName.toString(), i-1, i);
                edgeName++;
                continue;
            }
            Integer whichOne;
            do{
                whichOne= randGen.nextInt(g.getVertexCount());
            }while(whichOne.intValue() == i.intValue() || (whichOne.intValue() == 0));
            
            System.out.println("wierzcholkow: " + g.getVertexCount());
            System.out.println("wierzcholek: " + i + "  dolaczam do: " + whichOne);
            
            g.addEdge(edgeName.toString(), i, whichOne);
            edgeName++;
            
            if(i >= 0.6*numVer){
                Integer whichOne2;
                do {
                    whichOne2 = randGen.nextInt(g.getVertexCount());
                } while (whichOne2.intValue() == i.intValue() || (whichOne2.intValue() == 0)
                        || whichOne2.intValue() == whichOne.intValue());
                
                g.addEdge(edgeName.toString(), i, whichOne2);
                edgeName++;
            }
        }
        return g;
    }
  
  public Graph getRandomGraph(){
      Graph<Integer, String> g;
      
      Factory<String> edgeFactory = new Factory<String>() {
          int i = 0;
          public String create() {
              return "E" + i++;
          }
      };
      
      Factory<Integer> vertexFactory = new Factory<Integer>() {
          int i = 0;
          public Integer create() {
              return i++;
          }
      };
      Factory<Graph<Integer, String>> graphFactory = SparseGraph.getFactory();
      HashSet<Integer> seedVertices = new HashSet();
      for (int i = 0; i < 10; i++) {
          seedVertices.add(i);
      }
      
      BarabasiAlbertGenerator<Integer, String> generator;
      generator = new BarabasiAlbertGenerator<Integer, String>(
              graphFactory,
              vertexFactory,
              edgeFactory,
              seedVertices.size(), //init vertices
              5, //numEdgesToAttach
              seedVertices);
      
      g = generator.create();
      return g;
  }
}