/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileWriter;
import java.util.Iterator;
import java.util.ArrayList;
import org.jgraph.graph.Edge;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


/**
 *
 * @author dsvalencia
 */
public class Grafo {
    private final UndirectedGraph graph;
    
    
    public Grafo() {
        graph=new SimpleGraph(DefaultEdge.class);    
    }
    
    public boolean agregarVertice(Object vertice){
        return graph.addVertex(vertice);
    }
    
    public Object agregarArco(Object verticeOrigen, Object verticeDestino){
        if(verticeOrigen.equals(verticeDestino)){
            return null;        
        }
        return graph.addEdge(verticeOrigen,verticeDestino);
    }
    
    
    
    @Override
    public String toString(){
        return graph.toString();
    }
    
    public ArrayList rutaMasCorta(Object verticeOrigen, Object verticeDestino){
        DijkstraShortestPath<Object,DefaultEdge> dijkstra = new DijkstraShortestPath(graph, verticeOrigen, verticeDestino);
        GraphPath graphPath;
        if((graphPath=dijkstra.getPath())==null){
            return new ArrayList();
        }
        ArrayList path = (ArrayList)Graphs.getPathVertexList(graphPath);
        if(path==null){
            return new ArrayList();
        }
        return path;
    }
            
    public ArrayList getVertices(){
        ArrayList vertices=new ArrayList();
        Iterator iteradorVertices=graph.vertexSet().iterator();
        while(iteradorVertices.hasNext()){
            vertices.add(iteradorVertices.next());
        }
        return vertices;
    }
     
    public ArrayList<Arco> getArcos(){
        ArrayList<Arco> arcos=new ArrayList();
        Iterator iteradorArcos=graph.edgeSet().iterator();
        while(iteradorArcos.hasNext()){
            DefaultEdge defaultEdge=((DefaultEdge)iteradorArcos.next());
            arcos.add(new Arco(graph.getEdgeSource(defaultEdge),graph.getEdgeTarget(defaultEdge)));
        }
        return arcos;
    }
    
    public void mostrarGrafo(){
        System.out.println("Vertices:");
        Iterator iteradorVertexSet=graph.vertexSet().iterator();
        while(iteradorVertexSet.hasNext()){
            System.out.println(iteradorVertexSet.next());
        }
        System.out.println("Arcos:");
        Iterator iteradorEdgeSet=graph.edgeSet().iterator();
        while(iteradorEdgeSet.hasNext()){
            System.out.println(iteradorEdgeSet.next());
        }

    }
    
    public void exportar(){
        try{
            DOTExporter exporter = new DOTExporter();
            exporter.export(new FileWriter("grafo.dot"), graph);
        }catch(Exception e){
            System.out.println(e);
            for(StackTraceElement stack:e.getStackTrace()){
                System.out.println(stack);
            }
        }
        
    }

    public void getVericesCircundantesRecursivo(Object v, int profundidad, ArrayList verticesCircundantes) {
        if(v!=null&&verticesCircundantes!=null){
            if(!verticesCircundantes.contains(v)){
                verticesCircundantes.add(v);
            }
            if(profundidad>0){
                Iterator iteradorEdgeSet=graph.edgesOf(v).iterator();
                ArrayList hijos=new ArrayList();
                while(iteradorEdgeSet.hasNext()){
                    Object hijo=null;
                    DefaultEdge defaultEdge=(DefaultEdge)iteradorEdgeSet.next();
                    if(graph.getEdgeSource(defaultEdge)!=v){
                        hijo=graph.getEdgeSource(defaultEdge);
                    }else{
                        hijo=graph.getEdgeTarget(defaultEdge);
                    }
                    hijos.add(hijo);
                    if(!verticesCircundantes.contains(hijo)){
                        verticesCircundantes.add(hijo);
                    }
                }
                profundidad--;
                for(int i=0;i<hijos.size();i++){
                    getVericesCircundantesRecursivo(hijos.get(i),profundidad,verticesCircundantes);
                }
            }
        }
    }
    
}
