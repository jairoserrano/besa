/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.lookandfeel;

import eu.hansolo.fx.heatmap.ColorMapping;
import eu.hansolo.fx.heatmap.HeatMap;
import eu.hansolo.fx.heatmap.OpacityDistribution;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import SIMALL.modelo.util.Arco;
import SIMALL.modelo.util.Grafo;
import javafx.scene.Node;

/**
 *
 * @author dsvalencia
 */
public class Lienzo extends Pane{
    
    Rotate lienzoRX=new Rotate(0,Rotate.X_AXIS);
    Rotate lienzoRY=new Rotate(0,Rotate.Y_AXIS);
    Rotate lienzoRZ=new Rotate(0,Rotate.Z_AXIS);
    
    private volatile boolean isPicking=false;
    private Point3D vecIni, vecPos;
    private double distance;
    
    private boolean edificiosVisibles=true;
    private boolean unionesVisibles=true;
    private boolean presentacionUnionesVisibles=true;
    private boolean mapaCalorVentasVisible=true;
    private boolean mapaCalorTransitoVisible=true;
    
    private Node nodoIntersectado=null;
    
    private boolean infoEdificios=false;
    
    private ArrayList<Edificio> edificios;
    private ArrayList<Union> uniones;
    
    
    private ArrayList<LabelEdificio> labelEdificios;
    
    
    private final PerspectiveCamera camara=new PerspectiveCamera(true);
    
    
    private MapaEstructural mapa=MapaEstructural.getInstance();
    
    private HeatMap mapaDeCalorVentas;
    private HeatMap mapaDeCalorTransito;
    
    
    
    public Lienzo(){
        super();
        edificios=new ArrayList();
        uniones=new ArrayList();
        labelEdificios=new ArrayList();
    }

    private void configurarComponentes() {
        configurarCamara();
        configurarLienzo();
        configurarLugares();
        configurarMapaDeCalorVentas();
        configurarMapaDeCalorTransito();
        this.setUnionesVisibles(true);
        this.setEdificiosVisibles(true);
        this.setMapaCalorVentasVisible(false);
        this.setMapaCalorTransitoVisible(false);
    }

    private void configurarEventos() {
        this.getScene().setOnScroll((ScrollEvent event) -> {
            onScrollEvent(event);
        });
        
        this.getScene().setOnMousePressed((MouseEvent event) -> {
            onMousePressedEvent(event);
        });
        
        this.getScene().setOnMouseDragged((MouseEvent event) -> {
            onMouseDraggedEvent(event);
        });
        
        this.getScene().setOnMouseReleased((MouseEvent event) -> {
            onMouseReleasedEvent(event);
        });
        
        this.getScene().setOnMouseMoved((MouseEvent event) -> {
            onMouseMovedEvent(event);
        });
        this.getScene().setOnKeyPressed((KeyEvent event) -> {
            OnKeyPressedEvent(event);
        });
        this.getScene().setOnKeyReleased((KeyEvent event) -> {
            OnKeyReleasedEvent(event);
        });
    }

    private void configurarLugares() {        
               
        mapa.getAlmacenes().entrySet().stream().map(almacenes -> almacenes.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.COMERCIO,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        mapa.getEntradas().entrySet().stream().map(entradas -> entradas.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.ENTRADA,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        mapa.getMuros().entrySet().stream().map(muros -> muros.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.MURO,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        mapa.getPasillos().entrySet().stream().map(pasillos -> pasillos.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.PASILLO,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        mapa.getPuntosDeInformacion().entrySet().stream().map(puntosDeInformacion -> puntosDeInformacion.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.PUNTO_DE_INFORMACION,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        mapa.getSanitarios().entrySet().stream().map(sanitarios -> sanitarios.getValue()).map(lugar -> {
            Edificio edificio=new Edificio(lugar, LugarState.SANITARIO,mapa.getAnchoUnidad());
            edificio.setLayoutX(lugar.getX());
            edificio.setLayoutY(lugar.getY());
            edificio.setRotate(lugar.getAngulo());
            return edificio;
        }).map(edificio -> {
            this.getChildren().add(edificio);
            return edificio;
        }).forEachOrdered(edificio -> {
            edificios.add(edificio);
        });
        
        configurarArcos();
        
    }

    private void configurarCamara() {
        camara.setVerticalFieldOfView(false);
        camara.getTransforms().add(new Translate(mapa.getOffSet(),mapa.getOffSet(),-((mapa.getOffSet()*2)/Math.tan(Math.toDegrees(35)))*5));
        camara.setNearClip(0.1);
        camara.setFarClip(mapa.getOffSet()*20);
        camara.setFieldOfView(35);
        this.getScene().setCamera(camara);
        this.getTransforms().addAll(lienzoRX,lienzoRY,lienzoRZ);
    }

    private void configurarLienzo() {
        this.getTransforms().add(new Translate(0,0,0));
        this.setHeight(mapa.getOffSet()*2);
        this.setWidth(mapa.getOffSet()*2);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        //this.getChildren().add(crearGrilla(40));
    }
    
    public Canvas crearGrilla(int ancho){
        Canvas canvas=new Canvas();
        canvas.setWidth(mapa.getOffSet()*2);
        canvas.setHeight(mapa.getOffSet()*2);
        GraphicsContext gc=canvas.getGraphicsContext2D();
        for(int i=0;i<=(mapa.getOffSet()*2)/ancho;i++){
            for(int j=0;j<=(mapa.getOffSet()*2)/ancho;j++){
                gc.strokeLine(ancho*i, ancho*j,ancho*i,(ancho*j)+(mapa.getOffSet()*2));
                gc.strokeLine(ancho*i, ancho*j,(ancho*i)+(mapa.getOffSet()*2),ancho*j);
            }
        }
        gc.strokeLine(0,mapa.getOffSet()*2,mapa.getOffSet()*2,mapa.getOffSet()*2);
        gc.strokeLine(mapa.getOffSet()*2,0,mapa.getOffSet()*2,mapa.getOffSet()*2);
        return canvas;
    }

    public void configurar() {
        configurarComponentes();
        configurarEventos();
    }

    private void onScrollEvent(ScrollEvent event) {
        lienzoRX.setPivotX(camara.getBoundsInParent().getMaxX());
        lienzoRX.setPivotY(camara.getBoundsInParent().getMaxY());
        lienzoRX.setPivotZ(camara.getBoundsInParent().getMaxZ());
        lienzoRY.setPivotX(camara.getBoundsInParent().getMaxX());
        lienzoRY.setPivotY(camara.getBoundsInParent().getMaxY());
        lienzoRY.setPivotZ(camara.getBoundsInParent().getMaxZ());
        lienzoRZ.setPivotX(camara.getBoundsInParent().getMaxX());
        lienzoRZ.setPivotY(camara.getBoundsInParent().getMaxY());
        lienzoRZ.setPivotZ(camara.getBoundsInParent().getMaxZ());

        if(event.isAltDown()){
            if(event.getDeltaY()<0){
                lienzoRY.setAngle(lienzoRY.getAngle()-1);
            }
            else{
                lienzoRY.setAngle(lienzoRY.getAngle()+1);
            }
        }else{
            if(event.isShiftDown()){
                if(event.getDeltaX()>0){
                    lienzoRX.setAngle(lienzoRX.getAngle()-1);
                }
                else{
                    lienzoRX.setAngle(lienzoRX.getAngle()+1);
                }
            }
            else{
                if(event.isControlDown()){
                    if(event.getDeltaY()<0){
                        lienzoRZ.setAngle(lienzoRZ.getAngle()+1);
                    }
                    else{
                        lienzoRZ.setAngle(lienzoRZ.getAngle()-1);
                    }
                }
                else{
                    if(event.getDeltaY()<0){
                        camara.setTranslateZ(camara.getTranslateZ()+mapa.getAnchoUnidad()*2);
                    }
                    else{
                        camara.setTranslateZ(camara.getTranslateZ()-mapa.getAnchoUnidad()*2);
                    }
                }
            }
        }
        event.consume();
    }

    private void onMousePressedEvent(MouseEvent event) {
        if( !event.isPrimaryButtonDown())
            return;
        
        PickResult pr = event.getPickResult();
        if(pr!=null && pr.getIntersectedNode() != null){
            distance=pr.getIntersectedDistance();
            isPicking=true;
            vecIni = unProjectDirection(event.getSceneX(), event.getSceneY(), this.getScene().getWidth(),this.getScene().getHeight());
        }

        event.consume();
    }

    private void onMouseDraggedEvent(MouseEvent event) {
        // left mouse button => dragging
        if( !event.isPrimaryButtonDown())
            return;
        if(isPicking){
            PickResult pr = event.getPickResult();
            if(pr!=null && pr.getIntersectedNode() != null){
                vecPos = unProjectDirection(event.getSceneX(), event.getSceneY(), this.getScene().getWidth(),this.getScene().getHeight());
                Point3D p=vecPos.subtract(vecIni).multiply(distance);
                vecIni=vecPos;
                this.setTranslateX(p.getX()+this.getTranslateX());
                this.setTranslateY(p.getY()+this.getTranslateY());
                this.setTranslateZ(p.getZ()+this.getTranslateZ());
                distance=pr.getIntersectedDistance();
            } else {
                isPicking=false;
            }
        }
        event.consume();
    }
    
    public Point3D unProjectDirection(double sceneX, double sceneY, double sWidth, double sHeight) {
        double tanHFov = Math.tan(Math.toRadians(camara.getFieldOfView()) * 0.5f);
        Point3D vMouse = new Point3D(tanHFov*(2*sceneX/sWidth-1), tanHFov*(2*sceneY/sWidth-sHeight/sWidth), 1);

        Point3D result = localToSceneDirection(vMouse);
        return result.normalize();
    }

    @Override
    public Point3D localToScene(Point3D pt) {
        Point3D res = camara.localToParentTransformProperty().get().transform(pt);
        if (camara.getParent() != null) {
            res = camara.getParent().localToSceneTransformProperty().get().transform(res);
        }
        return res;
    }

    public Point3D localToSceneDirection(Point3D dir) {
        Point3D res = localToScene(dir);
        return res.subtract(localToScene(new Point3D(0, 0, 0)));
    }

    private void onMouseReleasedEvent(MouseEvent event) {
        if(isPicking){
            isPicking=false;
        }
        event.consume();
    }

    private void onMouseMovedEvent(MouseEvent event) {
        if(infoEdificios){
            PickResult pr=event.getPickResult();
            if(pr!=null && pr.getIntersectedNode() != null && pr.getIntersectedNode().getClass().getName().equals(Edificio.class.getName())){
                if(nodoIntersectado==null){
                    nodoIntersectado=pr.getIntersectedNode();
                }else{
                    if(!pr.getIntersectedNode().getClass().getName().equals(LabelEdificio.class.getName())){
                        if(nodoIntersectado.equals(pr.getIntersectedNode())){
                            return; 
                        }
                    }
                }
                for(int i=0;i<labelEdificios.size();i++){
                    this.getChildren().remove(labelEdificios.get(i));
                }
                labelEdificios.removeAll(labelEdificios);
                Edificio edificio=(Edificio)pr.getIntersectedNode();
                LabelEdificio label=new LabelEdificio(edificio.getLugar());
                label.getTransforms().addAll(lienzoRX,lienzoRY,lienzoRZ);
                label.setTranslateX(pr.getIntersectedPoint().getX());
                label.setTranslateY(pr.getIntersectedPoint().getY());
                label.setTranslateZ(pr.getIntersectedPoint().getZ());
                /*try{
                    label.getTransforms().addAll(lienzoRX.createInverse(),lienzoRY.createInverse(),lienzoRZ.createInverse());
                }catch(Exception e){
                    System.err.println("Error tratando de invertir la transformación:"+e.getMessage());
                }*/

                label.setScaleX(pr.getIntersectedDistance()/1500);
                label.setScaleY(pr.getIntersectedDistance()/1500);

                this.getChildren().add(label);
                labelEdificios.add(label);
            }

        }
        else{
            for(int i=0;i<labelEdificios.size();i++){
                this.getChildren().remove(labelEdificios.get(i));
            }
            labelEdificios.removeAll(labelEdificios);
        }
        event.consume();
    }

    private void OnKeyPressedEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.E){
            if(edificiosVisibles){
                setEdificiosVisibles(false);
            }
            else{
                setEdificiosVisibles(true);
            }
        }
        if(event.getCode() == KeyCode.U){
            if(unionesVisibles){
                setUnionesVisibles(false);
            }
            else{
                setUnionesVisibles(true);
            }
        }
        if(event.getCode() == KeyCode.V){
            if(mapaCalorVentasVisible){
                setMapaCalorVentasVisible(false);
            }
            else{
                setMapaCalorVentasVisible(true);
            }
        }
        if(event.getCode() == KeyCode.T){
            if(mapaCalorTransitoVisible){
                setMapaCalorTransitoVisible(false);
            }
            else{
                setMapaCalorTransitoVisible(true);
            }
        }
        if(event.getCode() == KeyCode.P){
            if(presentacionUnionesVisibles){
                setPresentacionUnionesVisibles(false);
            }
            else{
                setPresentacionUnionesVisibles(true);
            }
        }
        if(event.getCode() == KeyCode.I){
            infoEdificios=true;
        }
    }
    
    private void OnKeyReleasedEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.I){
            infoEdificios=false;
        }
    }
    
    private void setEdificiosVisibles(boolean visible){
        for(int i=0;i<edificios.size();i++){
            edificios.get(i).setVisible(visible);
        }
        edificiosVisibles=visible;
    }
    
    private void setPresentacionUnionesVisibles(boolean visible){
        if(!visible){
            setEdificiosVisibles(false);
            setUnionesVisibles(false);
            for(int i=0;i<uniones.size();i++){
                uniones.get(i).setVisible(true);
                try{Thread.sleep(500);}catch(InterruptedException e){}
                uniones.get(i).setVisible(false);
            }
            presentacionUnionesVisibles=true;
        }else{
            setEdificiosVisibles(true);
            setUnionesVisibles(true);
            presentacionUnionesVisibles=false;
        }
    }

    private void setUnionesVisibles(boolean visible){
        for(int i=0;i<uniones.size();i++){
            uniones.get(i).setVisible(visible);
        }
        unionesVisibles=visible;
    }
    
    private void configurarArcos() {
        MapaEstructural mapa=MapaEstructural.getInstance();
        Grafo grafo=mapa.getGrafoLugares();
        ArrayList<Arco> arcos=grafo.getArcos();
        for(int i=0;i<arcos.size();i++){
            Arco arco=arcos.get(i);
            LugarState origen=(LugarState)arco.getOrigen();
            LugarState destino=(LugarState)arco.getDestino();
            Union union=new Union(origen.getX(),origen.getY(),destino.getX(),destino.getY());
            this.getChildren().add(union);
            uniones.add(union);
        }
    }

    private void configurarMapaDeCalorVentas() {
        mapaDeCalorVentas=new HeatMap(this.getWidth(), this.getHeight(), ColorMapping.BLUE_BLACK_RED, mapa.getAnchoUnidad());
        mapaDeCalorVentas.setOpacityDistribution(OpacityDistribution.LINEAR);
        mapaDeCalorVentas.setOpacity(1);
        ArrayList<Point2D> eventos=new ArrayList();
        int maxVentas=-1;
        int minVentas=-1;
        for (HashMap.Entry<String, LugarState> almacen : mapa.getAlmacenes().entrySet()) {
            int ventas=((ComercioState)almacen.getValue()).getTotalCantidadVentasRealizadas();
            if(maxVentas==-1){
                maxVentas=ventas;
                minVentas=ventas;
            }
            if(maxVentas<ventas){
                maxVentas=ventas;
            }
            if(minVentas>ventas){
                minVentas=ventas;
            }
        }
        for (HashMap.Entry<String, LugarState> almacen : mapa.getAlmacenes().entrySet()) {
            int ventas=((ComercioState)almacen.getValue()).getTotalCantidadVentasRealizadas();
            int intensidad=0;
            if(maxVentas>0){
                intensidad=(ventas-minVentas)*100/maxVentas;
            }
            for(int i=0;i<intensidad;i++){
                eventos.add(new Point2D(almacen.getValue().getX(),almacen.getValue().getY()));
            }
        }
        
        mapaDeCalorVentas.addEvents(eventos);
        this.getChildren().add(mapaDeCalorVentas);
    }
    
    private void configurarMapaDeCalorTransito() {
        mapaDeCalorTransito=new HeatMap(this.getWidth(), this.getHeight(), ColorMapping.LIME_YELLOW_RED, mapa.getAnchoUnidad());
        mapaDeCalorTransito.setOpacityDistribution(OpacityDistribution.LINEAR);
        mapaDeCalorTransito.setOpacity(1);
        int minimoTransito=0;
        int maximoTransito=-1;
        int i=0;
        for (HashMap.Entry<String, LugarState> pasillo : mapa.getPasillos().entrySet()) {
            if(i==0){
                minimoTransito=pasillo.getValue().getTransito();
                maximoTransito=pasillo.getValue().getTransito();
            }else{
                if(minimoTransito>=pasillo.getValue().getTransito()){
                    minimoTransito=pasillo.getValue().getTransito();
                }
                if(maximoTransito<=pasillo.getValue().getTransito()){
                    maximoTransito=pasillo.getValue().getTransito();
                }
            }
            i++;
        }
        ArrayList<Point2D> eventos=new ArrayList();
        for (HashMap.Entry<String, LugarState> pasillo : mapa.getPasillos().entrySet()) {
            int transito=pasillo.getValue().getTransito();
            int intensidad=0;
            if(maximoTransito>0){
                intensidad=intensidad=(transito-minimoTransito)*10/maximoTransito;
            }
            for(int j=0;j<intensidad;j++){
                eventos.add(new Point2D(pasillo.getValue().getX(),pasillo.getValue().getY()));
            }
        }
        mapaDeCalorTransito.addEvents(eventos);
        this.getChildren().add(mapaDeCalorTransito);
    }

    private void setMapaCalorVentasVisible(boolean visible) {
        mapaDeCalorVentas.setVisible(visible);
        mapaCalorVentasVisible=visible;
    }
    
    private void setMapaCalorTransitoVisible(boolean visible) {
        mapaDeCalorTransito.setVisible(visible);
        mapaCalorTransitoVisible=visible;
    }
    
    
}

