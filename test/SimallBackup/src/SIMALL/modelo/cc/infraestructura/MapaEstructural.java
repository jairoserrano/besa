/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura;


import wpsMain.agents.peasant.LectorServicios;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import wpsMain.agents.peasant.Categoria;
import wpsMain.agents.peasant.ConfigComercio;
import wpsMain.agents.peasant.Demografia;
import wpsMain.agents.peasant.Producto;
import wpsMain.agents.peasant.LectorCategorias;
import wpsMain.agents.peasant.LectorComercios;
import wpsMain.agents.peasant.LectorDemografia;
import wpsMain.agents.peasant.LectorNodosFuzzy;
import java.awt.Point;
import SIMALL.modelo.log.LogAuditoria;
import wpsMain.agents.peasant.LugarState;
import SIMALL.modelo.cc.infraestructura.lugares.pasillo.agente.PasilloState;
import SIMALL.modelo.cc.infraestructura.lugares.puntoInformacion.agente.PuntoDeInformacionState;
import SIMALL.modelo.cc.infraestructura.lugares.sanitario.agente.SanitarioState;
import wpsMain.util.Aleatorio;
import wpsMain.util.Grafo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import wpsMain.agents.peasant.ComercioState;
import wpsMain.agents.peasant.EntradaState;
import SIMALL.modelo.cc.infraestructura.lugares.muro.agente.MuroState;
import SIMALL.modelo.ia.fuzzy.pares.NodoFuzzy;
import wpsMain.util.DistribucionProbabilidadTriangular;
import java.io.File;
import java.util.Arrays;
import java.util.Set;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author dsvalencia
 */
public class MapaEstructural {
    
    private static final MapaEstructural MAPA_CENTRO_COMERCIAL= new MapaEstructural();
    
    private final Grafo grafoLugares;
    
    private final HashMap<String,LugarState> entradas;
    private final HashMap<String,LugarState> pasillos;
    private final HashMap<String,LugarState> comercios;
    private final HashMap<String,LugarState> sanitarios;
    private final HashMap<String,LugarState> puntosDeInformacion;
    private final HashMap<String,LugarState> muros;
    
    private double tiempoTotalSimulacionEnHoras=0;
    private double cuadrosQueRecorreUnClienteXMinuto=0;
    private double tiempoEnRecorrerUnCuadro=0;

    HashMap<String,Color> coloresClarosPorNicho=new HashMap();
    HashMap<String,Color> coloresOscurosPorNicho=new HashMap();
    
    private final HashMap<String,ArrayList<LugarState>> categorias;
    
    private final NodoFuzzy nodoFuzzyContribucionCompra=LectorNodosFuzzy.leerNodosFuzzy();
    
    private Categoria arbolCategorias;
    private ArrayList<ConfigComercio> configComercios;
    private Demografia demografia;
    private int totalIntentosCompra=0;
    private int totalComprasExitosas=0;
    private int totalComprasFallidas=0;
    
    public Demografia getDemografia() {
        return demografia;
    }

    public void setDemografia(Demografia demografia) {
        this.demografia = demografia;
    }
    
    private int offSet=0;
    
    private HashMap<String,ArrayList<ConfigComercio>> confCategorias;
    private ArrayList<ConfigComercio> configComerciosSinCategoria;
    private ArrayList<ConfigComercio> configAnclas;
    private ArrayList<ConfSanitario> confSanitarios;
    private ArrayList<ConfEntrada> confEntradas;
    private ArrayList<ConfPuntoInformacion> confPuntosInformacion;
    
    private int maximaCantidadClientes;
    
    private boolean clusterizado;
    private boolean anclasEnEntrada;
    private int cantidadVertices;
    private int anchoUnidad;
    private int anchoSeparador;
    private int cantidadPasillosInteriores;
    
    
    
    private HashMap<String,LugarState> clientes; // [Key] Alias del Cliente, [Element] Lugar en el que se encuentra

    public Categoria getArbolCategorias() {
        return arbolCategorias;
    }

    public void setArbolCategorias(Categoria arbolCategorias) {
        this.arbolCategorias = arbolCategorias;
    }
    
    
    
    public HashMap<String,ArrayList<LugarState>> getCategorias(){
        return categorias;
    }
    
    

    public static MapaEstructural getMAPA_CENTRO_COMERCIAL() {
        return MAPA_CENTRO_COMERCIAL;
    }

    public Grafo getGrafoLugares() {
        return grafoLugares;
    }
    
    public int getOffSet(){
        return offSet;
    }

    public HashMap<String, LugarState> getEntradas() {
        return entradas;
    }

    public HashMap<String, LugarState> getPasillos() {
        return pasillos;
    }

    public HashMap<String, LugarState> getAlmacenes() {
        return comercios;
    }

    public HashMap<String, LugarState> getSanitarios() {
        return sanitarios;
    }

    public HashMap<String, LugarState> getPuntosDeInformacion() {
        return puntosDeInformacion;
    }

    public HashMap<String, LugarState> getMuros() {
        return muros;
    }
    
    public HashMap<String, ArrayList<ConfigComercio>> getConfCategorias() {
        return confCategorias;
    }

    public ArrayList getConfNegociosSinCategoria() {
        return configComerciosSinCategoria;
    }

    public ArrayList getConfSanitarios() {
        return confSanitarios;
    }

    public ArrayList getConfEntradas() {
        return confEntradas;
    }

    public ArrayList getConfPuntosInformacion() {
        return confPuntosInformacion;
    }

    public ArrayList getConfAnclas() {
        return configAnclas;
    }

    public HashMap<String, LugarState> getClientes() {
        return clientes;
    }

    public boolean isClusterizado() {
        return clusterizado;
    }

    public void setClusterizado(boolean clusterizado) {
        this.clusterizado = clusterizado;
    }

    public boolean isAnclasEnEntrada() {
        return anclasEnEntrada;
    }

    public void setAnclasEnEntrada(boolean anclasEnEntrada) {
        this.anclasEnEntrada = anclasEnEntrada;
    }

    public int getCantidadVertices() {
        return cantidadVertices;
    }

    public void setCantidadVertices(int vertices) {
        this.cantidadVertices = vertices;
    }
    
    

    public int getCantidadClientes() {
        return maximaCantidadClientes;
    }

    public void setCantidadClientes(int cantidadClientes) {
        this.maximaCantidadClientes = cantidadClientes;
    }
    
    
    public int getAnchoSeparador() {
        return anchoSeparador;
    }

    public void setAnchoSeparador(int anchoSeparador) {
        this.anchoSeparador = anchoSeparador;
    }
    
    
    public int getAnchoUnidad() {
        return anchoUnidad;
    }

    public void setAnchoUnidad(int anchoUnidad) {
        this.anchoUnidad = anchoUnidad;
    }


    public int getCantidadPasillosInteriores() {
        return cantidadPasillosInteriores;
    }

    public void setCantidadPasillosInteriores(int pasillosInteriores) {
        this.cantidadPasillosInteriores = pasillosInteriores;
    }
    
      
    private MapaEstructural(){
        this.grafoLugares = new Grafo();
        this.entradas=new HashMap();
        this.pasillos=new HashMap();
        this.comercios=new HashMap();
        this.sanitarios=new HashMap();
        this.puntosDeInformacion=new HashMap();
        this.muros=new HashMap();
        this.clientes=new HashMap();
        this.confCategorias=new HashMap();
        this.configComerciosSinCategoria=new ArrayList();
        this.confSanitarios=new ArrayList();
        this.confEntradas=new ArrayList();
        this.confPuntosInformacion=new ArrayList();
        this.configAnclas=new ArrayList();
        this.clusterizado=false;
        this.anclasEnEntrada=false;
        this.categorias=new HashMap();
        this.maximaCantidadClientes=0;
        this.tiempoTotalSimulacionEnHoras=0;
        configurarLugares();
    }
    
    public static MapaEstructural getInstance(){
        return MAPA_CENTRO_COMERCIAL;
    }
    
    public LugarState obtenerLugarAleatorio(int tipo){
        HashMap<String,LugarState> lugares=new HashMap();
        switch(tipo){
            case LugarState.ENTRADA:                lugares=entradas;
                                                    break;
            case LugarState.PASILLO:                lugares=pasillos;
                                                    break;
            case LugarState.COMERCIO:               lugares=comercios;
                                                    break;
            case LugarState.SANITARIO:lugares=      sanitarios;
                                                    break;
            case LugarState.PUNTO_DE_INFORMACION:   lugares=puntosDeInformacion;
                                                    break;
            case LugarState.MURO:                   lugares=muros;
                                                    break;
        }
        
        LugarState l=null;
        Iterator i=lugares.keySet().iterator();
        int j=Aleatorio.getInstance().siguienteEntero(0, lugares.keySet().size()-1);
        for(int k=0;k<=j;k++){
            l=(LugarState)lugares.get((String)i.next());
        }
        return l;
    }
    
    private boolean agregarLugar(LugarState lugar){
        if(!categorias.containsKey(lugar.getNicho())){
            ArrayList<LugarState> lugares=new ArrayList();
            lugares.add(lugar);
            categorias.put(lugar.getNicho(), lugares);
        }
        else{
            ArrayList<LugarState> lugares=categorias.get(lugar.getNicho());
            if(!lugares.contains(lugar)){
                lugares.add(lugar);
                categorias.put(lugar.getNicho(), lugares);
            }
        }
        if(lugar.getClass().getName().equals(PasilloState.class.getName())){
            if(!pasillos.containsKey(lugar.getAlias())){
                pasillos.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        
        if(lugar.getClass().getName().equals(EntradaState.class.getName())){
            if(!entradas.containsKey(lugar.getAlias())){
                entradas.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        if(lugar.getClass().getName().equals(ComercioState.class.getName())){
            if(!comercios.containsKey(lugar.getAlias())){
                comercios.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        if(lugar.getClass().getName().equals(SanitarioState.class.getName())){
            if(!sanitarios.containsKey(lugar.getAlias())){
                sanitarios.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        if(lugar.getClass().getName().equals(PuntoDeInformacionState.class.getName())){
            if(!puntosDeInformacion.containsKey(lugar.getAlias())){
                puntosDeInformacion.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        if(lugar.getClass().getName().equals(MuroState.class.getName())){
            if(!muros.containsKey(lugar.getAlias())){
                muros.put(lugar.getAlias(),lugar);
                this.grafoLugares.agregarVertice(lugar);
                return true;
            }       
        }
        LogAuditoria.getInstance().escribirEvento(this.getClass().getName(), this.getClass().getName(), "No se encuentra registrado el lugar tipo "+lugar.getClass().getName());
        return false;
    }

    public ArrayList getRutaMasCortaATipoDeLugarMasCercano(String aliasCliente, int tipoDestino, String categoria) {
        LugarState lugarActual=getLugarActualDeCliente(aliasCliente);
        ArrayList rutaMasCorta=null;
        HashMap<String,LugarState> lugaresDestino=new HashMap();
        switch(tipoDestino){
            case LugarState.ENTRADA:     lugaresDestino=entradas;
                                        break;
            case LugarState.SANITARIO:  lugaresDestino=sanitarios;
                                        break;
            case LugarState.COMERCIO:    lugaresDestino=comercios;
                                        break;
            case LugarState.PASILLO:    lugaresDestino=pasillos;
                                        break;
            case LugarState.PUNTO_DE_INFORMACION:   lugaresDestino=puntosDeInformacion;
                                                    break;
            case LugarState.MURO:   lugaresDestino=muros;
                                                    break;
        }
        Iterator i=lugaresDestino.keySet().iterator();
        while(i.hasNext()){
            LugarState lugarDestino=(LugarState)lugaresDestino.get((String)i.next());
            ArrayList ruta=grafoLugares.rutaMasCorta(lugarActual, lugarDestino);
            if(ruta!=null){
                if(rutaMasCorta==null){
                    rutaMasCorta=ruta;
                }
                else{
                    if(ruta.size()<=rutaMasCorta.size()){
                        rutaMasCorta=ruta;
                    }
                }
            }
        }
        return rutaMasCorta;
    }

    

    public LugarState getLugarActualDeCliente(String aliasCliente) {
        if(clientes.containsKey(aliasCliente)){
            return clientes.get(aliasCliente);
        }
        return null;
    }

    public void moverCliente(String aliasCliente, LugarState lugarState) {
        clientes.put(aliasCliente, lugarState);
    }
    
    
    public void configurarLugares(){
        try{
            arbolCategorias=LectorCategorias.leerCategorias();
            System.out.println(arbolCategorias);
            configComercios=LectorComercios.leerComercios(ConfigComercio.TIPO_NORMAL, arbolCategorias);
            configAnclas=LectorComercios.leerComercios(ConfigComercio.TIPO_ANCLA, arbolCategorias);
            for(int i=0;i<configAnclas.size();i++){
                System.out.println(configAnclas.get(i));
            }
            for(int i=0;i<configComercios.size();i++){
                System.out.println(configComercios.get(i));
            }
            demografia=LectorDemografia.leerDemografia();
            System.out.println(demografia);
            
            confSanitarios=LectorServicios.leerSanitarios();
            confPuntosInformacion=LectorServicios.leerPuntosDeInformacion();
            confEntradas=LectorServicios.leerEntradas();
            
            leerParametrosCentroComercial();
            for(ConfigComercio configComercio:configComercios){
                if(!confCategorias.containsKey(configComercio.getNicho())){
                    confCategorias.put(configComercio.getNicho(), new ArrayList<ConfigComercio>(Arrays.asList(configComercio)));
                }
                else{
                    confCategorias.get(configComercio.getNicho()).add(configComercio);
                }
            }
            
            ArrayList arregloConfLugares=new ArrayList();
                
            if(clusterizado){
                for(String categoria:confCategorias.keySet()){
                    ArrayList configComercios=confCategorias.get(categoria);
                    for(int i=0;i<configComercios.size();i++){
                        ConfigComercio configComercio=new ConfigComercio((ConfigComercio)configComercios.get(i));
                        configComercio.setNombre(configComercio.getNombre()+"_"+configComercio.getLocales());
                        arregloConfLugares.add(configComercio);
                        if(((ConfLugar)configComercios.get(i)).getLocales()-1>0){
                            ((ConfLugar)configComercios.get(i)).setLocales(((ConfLugar)configComercios.get(i)).getLocales()-1);
                            i--;
                        }
                    }
                }
            }
            else{
                ArrayList tempConfigComercios=new ArrayList();
                for(String categoria:confCategorias.keySet()){
                    ArrayList configComercios=confCategorias.get(categoria);
                    for(int i=0;i<configComercios.size();i++){
                        ConfigComercio configComercio=new ConfigComercio((ConfigComercio)configComercios.get(i));
                        configComercio.setNombre(configComercio.getNombre()+"_"+configComercio.getLocales());
                        tempConfigComercios.add(configComercio);
                        if(((ConfLugar)configComercios.get(i)).getLocales()-1>0){
                            ((ConfLugar)configComercios.get(i)).setLocales(((ConfLugar)configComercios.get(i)).getLocales()-1);
                            i--;
                        }
                    }
                }
                while(tempConfigComercios.size()>0){
                    int indiceAleatorioCategorias=Aleatorio.getInstance().siguienteEnteroArregloLocales(0, tempConfigComercios.size()-1);
                    arregloConfLugares.add(tempConfigComercios.get(indiceAleatorioCategorias));
                    tempConfigComercios.remove(indiceAleatorioCategorias);
                }
            }
            
            int cantidadComerciosSinCategoria=0;
            for(int i=0;i<this.configComerciosSinCategoria.size();i++){
                ConfigComercio negocio=(ConfigComercio)configComerciosSinCategoria.get(i);
                cantidadComerciosSinCategoria+=negocio.getLocales();
            }
            if(cantidadComerciosSinCategoria>=0){
                int j=0;
                for(int i=0;i<configComerciosSinCategoria.size();i++){
                    int indice=arregloConfLugares.size()/(cantidadComerciosSinCategoria+1);
                    ConfigComercio configComercio=(ConfigComercio)configComerciosSinCategoria.get(i);
                    ConfigComercio configComercio_copia=new ConfigComercio((ConfigComercio)configComerciosSinCategoria.get(i));
                    configComercio_copia.setNombre(configComercio.getNombre()+"_"+configComercio.getLocales());
                    arregloConfLugares.add(indice*(j+1), configComercio_copia);
                    j++;
                    if(configComercio.getLocales()-1>0){
                        configComercio.setLocales(configComercio.getLocales()-1);
                        i--;
                    }
                }
            }
            
            
            int cantidadAnclas=0;
            for(int i=0;i<configAnclas.size();i++){
                ConfigComercio ancla=(ConfigComercio)configAnclas.get(i);
                cantidadAnclas+=ancla.getLocales();
            }
            if(cantidadAnclas>=0){
                int j=0;
                for(int i=0;i<configAnclas.size();i++){
                    int indice=arregloConfLugares.size()/(cantidadAnclas+1);
                    ConfigComercio ancla=(ConfigComercio)configAnclas.get(i);
                    ConfigComercio ancla_copia=new ConfigComercio((ConfigComercio)configAnclas.get(i));
                    ancla_copia.setNombre(ancla.getNombre()+"_"+ancla.getLocales());
                    arregloConfLugares.add(indice*(j+1), ancla_copia);
                    j++;
                    if(ancla.getLocales()-1>0){
                        ancla.setLocales(ancla.getLocales()-1);
                        i--;
                    }
                }
            }
            int cantidadSanitarios=0;
            for(int i=0;i<confSanitarios.size();i++){
                ConfSanitario sanitario=(ConfSanitario)confSanitarios.get(i);
                cantidadSanitarios+=sanitario.getLocales();
            }
            if(cantidadSanitarios>=0){
                int j=0;
                for(int i=0;i<confSanitarios.size();i++){
                    int indice=arregloConfLugares.size()/(cantidadSanitarios+1);
                    ConfSanitario sanitario=(ConfSanitario)confSanitarios.get(i);
                    ConfSanitario sanitario_copia=new ConfSanitario((ConfSanitario)confSanitarios.get(i));
                    sanitario_copia.setNombre(sanitario.getNombre()+"_"+sanitario.getLocales());
                    arregloConfLugares.add(indice*(j+1), sanitario_copia);
                    j++;
                    if(sanitario.getLocales()-1>0){
                        sanitario.setLocales(sanitario.getLocales()-1);
                        i--;
                    }
                }
            }
            int cantidadEntradas=0;
            for(int i=0;i<confEntradas.size();i++){
                ConfEntrada entrada=(ConfEntrada)confEntradas.get(i);
                cantidadEntradas+=entrada.getLocales();
            }
            if(cantidadEntradas>=0){
                int j=0;
                for(int i=0;i<confEntradas.size();i++){
                    int indice=arregloConfLugares.size()/(cantidadEntradas+1);
                    ConfEntrada entrada=(ConfEntrada)confEntradas.get(i);
                    ConfEntrada entrada_copia=new ConfEntrada((ConfEntrada)confEntradas.get(i));
                    entrada_copia.setNombre(entrada.getNombre()+"_"+entrada.getLocales());
                    arregloConfLugares.add(indice*(j+1), entrada_copia);
                    j++;
                    if(entrada.getLocales()-1>0){
                        entrada.setLocales(entrada.getLocales()-1);
                        i--;
                    }
                }
            }
            int cantidadPuntoInformacion=0;
            for(int i=0;i<confPuntosInformacion.size();i++){
                ConfPuntoInformacion puntoInformacion=(ConfPuntoInformacion)confPuntosInformacion.get(i);
                cantidadPuntoInformacion+=puntoInformacion.getLocales();
            }
            if(cantidadPuntoInformacion>=0){
                int j=0;
                for(int i=0;i<confPuntosInformacion.size();i++){
                    int indice=arregloConfLugares.size()/(cantidadPuntoInformacion+1);
                    ConfPuntoInformacion puntoInformacion=(ConfPuntoInformacion)confPuntosInformacion.get(i);
                    ConfPuntoInformacion puntoInformacion_copia=new ConfPuntoInformacion((ConfPuntoInformacion)confPuntosInformacion.get(i));
                    puntoInformacion_copia.setNombre(puntoInformacion.getNombre()+"_"+puntoInformacion.getLocales());
                    arregloConfLugares.add(indice*(j+1), puntoInformacion_copia);
                    j++;
                    if(puntoInformacion.getLocales()-1>0){
                        puntoInformacion.setLocales(puntoInformacion.getLocales()-1);
                        i--;
                    }
                }
            }
            
            int totalUnidades=0;
            for(int i=0;i<arregloConfLugares.size();i++){
                ConfLugar confLugar=(ConfLugar)arregloConfLugares.get(i);
                //System.out.println(confLugar);
                totalUnidades+=confLugar.getUnidades();
            }
            
            if(cantidadVertices>0){
                for(int i=0;i<((cantidadVertices*2)-(totalUnidades%(cantidadVertices*2)));i++){
                    ConfMuro confMuro=new ConfMuro("Muro_Independiente_"+i, "Muro", 1, 1, ConfLugar.X_DEFECTO, ConfLugar.Y_DEFECTO, false);
                    int indiceMuroAleatorio=Aleatorio.getInstance().siguienteEnteroArregloLocales(0, arregloConfLugares.size()-1);
                        arregloConfLugares.add(indiceMuroAleatorio,confMuro);
                }
                totalUnidades+=((cantidadVertices*2)-(totalUnidades%(cantidadVertices*2)));
            }
                   
            
            ArrayList<ArrayList> verticesLugares=new ArrayList();
            for(int i=0;i<cantidadVertices;i++){
                verticesLugares.add(new ArrayList());
            }
            int cantidadVerticesOcupados=0;
            int cantidadEnVerticeActual=0;
            int cantidadMurosAsociados=0;
            
            DistribucionProbabilidadTriangular tiemposDefecto=new DistribucionProbabilidadTriangular(0,0,0);
            DistribucionProbabilidadTriangular valoresProductoDefecto=new DistribucionProbabilidadTriangular(0,0,0);
            DistribucionProbabilidadTriangular ventasDefecto=new DistribucionProbabilidadTriangular(0,0,0);
            
            for(int i=0;i<arregloConfLugares.size();i++){
                ConfLugar confLugar=(ConfLugar)arregloConfLugares.get(i);
                LugarState lugarState=null;
                if(confLugar.getClass().getName().equals(ConfEntrada.class.getName())){
                    lugarState=new EntradaState(confLugar.getNombre(), confLugar.getNicho(),tiemposDefecto,valoresProductoDefecto,ventasDefecto, confLugar.getX(), confLugar.getY(), confLugar.getUnidades());
                }else{
                    if(confLugar.getClass().getName().equals(ConfigComercio.class.getName())){
                        ConfigComercio configComercio=(ConfigComercio)confLugar;
                        DistribucionProbabilidadTriangular tiempos=new DistribucionProbabilidadTriangular(configComercio.getMinTiempo(),configComercio.getAvgTiempo(),configComercio.getMaxTiempo());
                        DistribucionProbabilidadTriangular valoresProductos=new DistribucionProbabilidadTriangular(configComercio.getMinValor(),configComercio.getAvgValor(),configComercio.getMaxValor());
                        DistribucionProbabilidadTriangular ventas=new DistribucionProbabilidadTriangular(configComercio.getMinVentas(),configComercio.getAvgVentas(),configComercio.getMaxVentas());
                        lugarState=new ComercioState(configComercio.getNombre(),configComercio.getNicho(),tiempos,valoresProductos,ventas,configComercio.getX(), configComercio.getY(), confLugar.getUnidades(), configComercio.getInventario(), configComercio.getImpulsadores());
                    }
                    else{
                        if(confLugar.getClass().getName().equals(ConfPuntoInformacion.class.getName())){
                            lugarState=new PuntoDeInformacionState(confLugar.getNombre(),confLugar.getNicho(),tiemposDefecto,valoresProductoDefecto,ventasDefecto,confLugar.getX(), confLugar.getY(), confLugar.getUnidades());
                        }
                        else{
                            if(confLugar.getClass().getName().equals(ConfSanitario.class.getName())){
                                lugarState=new SanitarioState(confLugar.getNombre(),confLugar.getNicho(),tiemposDefecto,valoresProductoDefecto,ventasDefecto,confLugar.getX(), confLugar.getY(), confLugar.getUnidades());
                            }
                            else{
                                if(confLugar.getClass().getName().equals(ConfMuro.class.getName())){
                                    lugarState=new MuroState(confLugar.getNombre(),confLugar.getNicho(),tiemposDefecto,valoresProductoDefecto,ventasDefecto,confLugar.getX(), confLugar.getY(), confLugar.getUnidades(), confLugar.esVitrina());
                                }
                            }
                        }
                    }
                }
                this.agregarLugar(lugarState);
                int cantidadVitrinasRequeridas=0;
                int cantidadVitrinasInstaladas=0;
                if(confLugar.getClass().getName().equals(ConfigComercio.class.getName())){
                    cantidadVitrinasRequeridas=((ConfigComercio)confLugar).getVitrinas();
                }
                for(int j=0;j<confLugar.getUnidades();j++){
                    if(cantidadEnVerticeActual>=(totalUnidades/cantidadVertices)){
                        cantidadVerticesOcupados++;
                        cantidadEnVerticeActual=0;
                    }
                    if(j==0){
                        verticesLugares.get(cantidadVerticesOcupados).add(lugarState);
                        cantidadEnVerticeActual++;
                    }
                    else{
                        boolean esVitrina=false;
                        if(cantidadVitrinasInstaladas<=cantidadVitrinasRequeridas){
                            esVitrina=true;
                            cantidadVitrinasInstaladas++;
                        }
                        MuroState muroAsociado=new MuroState("Muro_Asociado_"+lugarState.getAlias()+"_"+cantidadMurosAsociados, "Muro", tiemposDefecto,valoresProductoDefecto,ventasDefecto, ConfLugar.X_DEFECTO, ConfLugar.Y_DEFECTO, 1, lugarState, esVitrina);
                        this.agregarLugar(muroAsociado);
                        verticesLugares.get(cantidadVerticesOcupados).add(muroAsociado);
                        cantidadEnVerticeActual++;
                        cantidadMurosAsociados++;
                    }
                }
                
            }
            int cantidadPasillos=0;
            int n=verticesLugares.size();
            int base=((anchoUnidad*2)+(anchoUnidad*cantidadPasillosInteriores*2)+(anchoSeparador*(cantidadPasillosInteriores*2+1)))/2;
            int largoCentroAVertice=Math.round(new Float(base*Math.tan(Math.toRadians((n-2)*180/n)/2)))+anchoUnidad/2+anchoSeparador/2;
            double anguloRotacion=360/n;
            offSet=((verticesLugares.get(0).size()*anchoUnidad+verticesLugares.get(0).size()*anchoSeparador)/2)+largoCentroAVertice;
            PasilloState pasilloCentral = new PasilloState("Pasillo_Central","Pasillo",tiemposDefecto,valoresProductoDefecto,ventasDefecto, offSet,offSet, 1);
            this.agregarLugar(pasilloCentral);
            ArrayList<ArrayList> pasillosCentralesBordeIzquierdo=new ArrayList();
            ArrayList<ArrayList> pasillosCentralesBordeDerecho=new ArrayList();
            ArrayList<ArrayList> pasillosCentralesBordeSuperior=new ArrayList();
            ArrayList<ArrayList> pasillosCentralesBordeInferior=new ArrayList();
            ArrayList<ArrayList> pasillosInternosBordeSuperior=new ArrayList();
            for(int i=0;i<verticesLugares.size();i++){
                ArrayList vertice=verticesLugares.get(i);
                ArrayList<ArrayList> pasillosInternos=new ArrayList();
                ArrayList<PasilloState> pasillosCentralesBordeIzquierdoVertice=new ArrayList();
                ArrayList<PasilloState> pasillosCentralesBordeDerechoVertice=new ArrayList();
                ArrayList<PasilloState> pasillosCentralesBordeInferiorVertice=new ArrayList();
                ArrayList<ArrayList> renglonesPasillosCentralesVertice=new ArrayList();
                Point puntoOrigen=new Point(0,0);
                int cantidadRenglones=(largoCentroAVertice)/(anchoUnidad+anchoSeparador);
                for(int j=0;j<cantidadRenglones;j++){
                    int anchoRenglon=Math.round(new Float((largoCentroAVertice-(anchoUnidad+anchoSeparador)-(anchoUnidad*j+anchoSeparador*j))*Math.tan(Math.toRadians(anguloRotacion/2))))*2-(anchoUnidad+anchoSeparador);
                    int cantidadPasillosEnRenglon=anchoRenglon/(anchoUnidad+anchoSeparador);
                    if(cantidadPasillosEnRenglon==0){
                        cantidadRenglones=j;
                        break;
                    }
                    ArrayList<PasilloState> renglonPasillosCentrales=new ArrayList();
                    for(int k=0;k<cantidadPasillosEnRenglon;k++){
                        PasilloState pasilloRenglon = new PasilloState("Pasillo_"+i+"_"+(j+1)+"_"+(k+1),"Pasillo",tiemposDefecto,valoresProductoDefecto,ventasDefecto, puntoOrigen.x-(anchoUnidad*cantidadPasillosEnRenglon+anchoSeparador*(cantidadPasillosEnRenglon+1))/2+((anchoUnidad/2+anchoSeparador)+anchoUnidad*k+anchoSeparador*k),puntoOrigen.y+largoCentroAVertice-(anchoUnidad+anchoSeparador)*(j+1), 1);
                        this.agregarLugar(pasilloRenglon);
                        renglonPasillosCentrales.add(pasilloRenglon);
                        Point puntoDestinoPasilloRenglon=rotarPunto(pasilloRenglon.getX(),pasilloRenglon.getY(),anguloRotacion*i,puntoOrigen);
                        pasilloRenglon.setX(puntoDestinoPasilloRenglon.x+offSet);
                        pasilloRenglon.setY(puntoDestinoPasilloRenglon.y+offSet);
                        pasilloRenglon.setAngulo(anguloRotacion*i);
                        if(k==0){
                            pasillosCentralesBordeIzquierdoVertice.add(pasilloRenglon);
                        }
                        if(k==cantidadPasillosEnRenglon-1){
                            pasillosCentralesBordeDerechoVertice.add(pasilloRenglon);
                        }
                        if(j==0){
                            pasillosCentralesBordeInferiorVertice.add(pasilloRenglon);
                        }
                    }
                    renglonesPasillosCentralesVertice.add(renglonPasillosCentrales);                
                }
                for(int j=0;j<renglonesPasillosCentralesVertice.size()-1;j++){
                    ArrayList<ArrayList> renglonPasillosCentralesVertice=renglonesPasillosCentralesVertice.get(j);
                    ArrayList<ArrayList> renglonSiguientePasillosCentralesVertice=renglonesPasillosCentralesVertice.get(j+1);
                    for(int k=0;k<renglonSiguientePasillosCentralesVertice.size();k++){
                        if((k<=renglonPasillosCentralesVertice.size()-1)&&((k+(renglonPasillosCentralesVertice.size()-renglonSiguientePasillosCentralesVertice.size())/2)<=renglonPasillosCentralesVertice.size()-1)){
                            this.grafoLugares.agregarArco(renglonPasillosCentralesVertice.get(k+(renglonPasillosCentralesVertice.size()-renglonSiguientePasillosCentralesVertice.size())/2), renglonSiguientePasillosCentralesVertice.get(k));
                        }
                    }
                }
                pasillosCentralesBordeDerecho.add(pasillosCentralesBordeDerechoVertice);
                pasillosCentralesBordeIzquierdo.add(pasillosCentralesBordeIzquierdoVertice);
                pasillosCentralesBordeSuperior.add(renglonesPasillosCentralesVertice.get(cantidadRenglones-1));
                pasillosCentralesBordeInferior.add(pasillosCentralesBordeInferiorVertice);
                for(int l=0;l<renglonesPasillosCentralesVertice.size();l++){
                    ArrayList renglonPasillosCentrales=renglonesPasillosCentralesVertice.get(l);
                    for(int m=0;m<renglonPasillosCentrales.size()-1;m++){
                        this.grafoLugares.agregarArco(renglonPasillosCentrales.get(m), renglonPasillosCentrales.get(m+1));
                    }
                }
                ArrayList<PasilloState> pasillosInternosBordeSuperiorVertice=new ArrayList();
                for(int j=0;j<vertice.size()/2;j++){
                    LugarState lugarAlaIzquierda=(LugarState)vertice.get(vertice.size()-1-j);
                    LugarState lugarAlaDerecha=(LugarState)vertice.get(j);
                    lugarAlaIzquierda.setX(puntoOrigen.x-base+anchoUnidad/2);
                    lugarAlaIzquierda.setY(puntoOrigen.y+largoCentroAVertice+(anchoUnidad*j)+(anchoSeparador*j));
                    lugarAlaDerecha.setX(puntoOrigen.x+base-anchoUnidad/2);
                    lugarAlaDerecha.setY(puntoOrigen.y+largoCentroAVertice+(anchoUnidad*j)+(anchoSeparador*j));
                    ArrayList<PasilloState> filaPasillosInternos=new ArrayList();
                    PasilloState pasilloAlaIzquierdaAnterior=null;
                    PasilloState pasilloAlaDerechaAnterior=null;
                    for(int k=0;k<cantidadPasillosInteriores;k++){
                        String nombrePasilloAlaIzquierda="Pasillo_"+cantidadPasillos+"_"+lugarAlaIzquierda.getAlias();
                        cantidadPasillos++;
                        PasilloState pasilloAlaIzquierda = new PasilloState(nombrePasilloAlaIzquierda,"Pasillo",tiemposDefecto,valoresProductoDefecto,ventasDefecto, ConfLugar.X_DEFECTO,ConfLugar.Y_DEFECTO, 1);
                        this.agregarLugar(pasilloAlaIzquierda);
                        filaPasillosInternos.add(pasilloAlaIzquierda);
                        String nombrePasilloAlaDerecha="Pasillo_"+cantidadPasillos+"_"+lugarAlaDerecha.getAlias();
                        cantidadPasillos++;
                        PasilloState pasilloAlaDerecha = new PasilloState(nombrePasilloAlaDerecha,"Pasillo",tiemposDefecto,valoresProductoDefecto,ventasDefecto, ConfLugar.X_DEFECTO,ConfLugar.Y_DEFECTO, 1);
                        this.agregarLugar(pasilloAlaDerecha);
                        filaPasillosInternos.add(pasilloAlaDerecha);
                        if(j==0){
                            pasillosInternosBordeSuperiorVertice.add(pasillosInternosBordeSuperiorVertice.size()/2,pasilloAlaDerecha);
                            pasillosInternosBordeSuperiorVertice.add(pasillosInternosBordeSuperiorVertice.size()/2,pasilloAlaIzquierda);
                        }
                        if(k==0){
                            this.grafoLugares.agregarArco(pasilloAlaIzquierda, lugarAlaIzquierda);
                            this.grafoLugares.agregarArco(pasilloAlaDerecha, lugarAlaDerecha);
                        }
                        if(k>0){
                            this.grafoLugares.agregarArco(pasilloAlaIzquierda, pasilloAlaIzquierdaAnterior);
                            this.grafoLugares.agregarArco(pasilloAlaIzquierda, pasilloAlaDerechaAnterior);
                        }
                        if(k+1==cantidadPasillosInteriores){
                            this.grafoLugares.agregarArco(pasilloAlaIzquierda, pasilloAlaDerecha);
                        }
                        pasilloAlaIzquierda.setX(lugarAlaIzquierda.getX()+(anchoUnidad+anchoSeparador)*(k+1));
                        pasilloAlaIzquierda.setY(lugarAlaIzquierda.getY());
                        pasilloAlaDerecha.setX(lugarAlaDerecha.getX()-(anchoUnidad+anchoSeparador)*(k+1));
                        pasilloAlaDerecha.setY(lugarAlaDerecha.getY());
                        Point puntoDestinoPasilloAlaIzquierda=rotarPunto(pasilloAlaIzquierda.getX(),pasilloAlaIzquierda.getY(),anguloRotacion*i,puntoOrigen);
                        Point puntoDestinoPasilloAlaDerecha=rotarPunto(pasilloAlaDerecha.getX(),pasilloAlaDerecha.getY(),anguloRotacion*i,puntoOrigen);
                        pasilloAlaDerecha.setX(puntoDestinoPasilloAlaDerecha.x);
                        pasilloAlaDerecha.setY(puntoDestinoPasilloAlaDerecha.y);
                        pasilloAlaIzquierda.setX(puntoDestinoPasilloAlaIzquierda.x);
                        pasilloAlaIzquierda.setY(puntoDestinoPasilloAlaIzquierda.y);
                        pasilloAlaDerecha.setAngulo(anguloRotacion*i);
                        pasilloAlaIzquierda.setAngulo(anguloRotacion*i);
                        pasilloAlaIzquierda.setX(pasilloAlaIzquierda.getX()+offSet);
                        pasilloAlaIzquierda.setY(pasilloAlaIzquierda.getY()+offSet);
                        pasilloAlaDerecha.setX(pasilloAlaDerecha.getX()+offSet);
                        pasilloAlaDerecha.setY(pasilloAlaDerecha.getY()+offSet);
                        pasilloAlaIzquierdaAnterior=pasilloAlaIzquierda;
                        pasilloAlaDerechaAnterior=pasilloAlaDerecha;
                    }
                    pasillosInternos.add(filaPasillosInternos);
                    Point puntoDestinoLugarAlaIzquierda=rotarPunto(lugarAlaIzquierda.getX(),lugarAlaIzquierda.getY(),anguloRotacion*i,puntoOrigen);
                    Point puntoDestinoLugarAlaDerecha=rotarPunto(lugarAlaDerecha.getX(),lugarAlaDerecha.getY(),anguloRotacion*i,puntoOrigen);
                    lugarAlaDerecha.setX(puntoDestinoLugarAlaDerecha.x);
                    lugarAlaDerecha.setY(puntoDestinoLugarAlaDerecha.y);
                    lugarAlaIzquierda.setX(puntoDestinoLugarAlaIzquierda.x);
                    lugarAlaIzquierda.setY(puntoDestinoLugarAlaIzquierda.y);
                    lugarAlaIzquierda.setX(lugarAlaIzquierda.getX()+offSet);
                    lugarAlaIzquierda.setY(lugarAlaIzquierda.getY()+offSet);
                    lugarAlaDerecha.setX(lugarAlaDerecha.getX()+offSet);
                    lugarAlaDerecha.setY(lugarAlaDerecha.getY()+offSet);
                    lugarAlaDerecha.setAngulo(anguloRotacion*i);
                    lugarAlaIzquierda.setAngulo(anguloRotacion*i);
                    
                    //System.out.print("Ix="+lugarAlaIzquierda.getX()+" Iy="+lugarAlaIzquierda.getY());
                    //for(int k=0;k<filaPasillosInternos.size();k++){
                        //System.out.print(" P"+k+"x="+filaPasillosInternos.get(k).getX());
                        //System.out.print(" P"+k+"y="+filaPasillosInternos.get(k).getY());
                    //}
                    //System.out.println(" Dx="+lugarAlaDerecha.getX()+" Dy="+lugarAlaDerecha.getY());
                    
                }
                pasillosInternosBordeSuperior.add(pasillosInternosBordeSuperiorVertice);
                if(pasillosInternos.size()>0){
                    ArrayList filaPasillosInternosAnterior=null;
                    for(int j=0;j<pasillosInternos.size();j++){
                        if(j>0){
                            for(int k=0;k<filaPasillosInternosAnterior.size();k++){
                                this.grafoLugares.agregarArco(pasillosInternos.get(j).get(k), filaPasillosInternosAnterior.get(k));                            
                            }
                        }
                        filaPasillosInternosAnterior=pasillosInternos.get(j);
                    }
                }                
            }
            for(int i=0;i<cantidadVertices;i++){
                ArrayList<PasilloState> pasillosCentralesBordeDerechoVertice=new ArrayList();
                ArrayList<PasilloState> pasillosCentralesBordeIzquierdoVerticeSiguiente=new ArrayList();;
                if(i==cantidadVertices-1){
                    pasillosCentralesBordeDerechoVertice=pasillosCentralesBordeDerecho.get(0);
                    pasillosCentralesBordeIzquierdoVerticeSiguiente=pasillosCentralesBordeIzquierdo.get(pasillosCentralesBordeIzquierdo.size()-1);
                }else{
                    pasillosCentralesBordeDerechoVertice=pasillosCentralesBordeDerecho.get(i+1);
                    pasillosCentralesBordeIzquierdoVerticeSiguiente=pasillosCentralesBordeIzquierdo.get(i);
                }
                
                for(int j=0;j<pasillosCentralesBordeDerechoVertice.size();j++){
                    this.grafoLugares.agregarArco(pasillosCentralesBordeDerechoVertice.get(j),pasillosCentralesBordeIzquierdoVerticeSiguiente.get(j));
                }
                ArrayList<PasilloState> pasillosCentralesBordeSuperiorVertice=pasillosCentralesBordeSuperior.get(i);
                for(int j=0;j<pasillosCentralesBordeSuperiorVertice.size();j++){
                    this.grafoLugares.agregarArco(pasilloCentral,pasillosCentralesBordeSuperiorVertice.get(j));
                }
            }
            for(int i=0;i<pasillosCentralesBordeSuperior.size();i++){
                ArrayList pasillosCentralesBordeSuperiorVertice=pasillosCentralesBordeSuperior.get(i);
                for(int j=0;j<pasillosCentralesBordeSuperiorVertice.size();j++){
                    this.grafoLugares.agregarArco(pasilloCentral,pasillosCentralesBordeSuperiorVertice.get(j));
                }
            }
            for(int i=0;i<pasillosCentralesBordeInferior.size();i++){
                ArrayList pasillosCentralesBordeInferiorVertice=pasillosCentralesBordeInferior.get(i);
                for(int j=0;j<pasillosCentralesBordeInferiorVertice.size();j++){
                    if(pasillosInternosBordeSuperior.get(i).size()-1>=j){
                        this.grafoLugares.agregarArco(pasillosCentralesBordeInferiorVertice.get(j),pasillosInternosBordeSuperior.get(i).get(j));
                    }
                }
            }
            ArrayList<ComercioState> comerciosInstalados=new ArrayList(comercios.values());
            for(ComercioState comercio:comerciosInstalados){
                int impulsadores=comercio.getImpulsadores();
                for(int i=0;i<impulsadores;i++){
                    ArrayList<PasilloState> pasillosInstalados=new ArrayList(pasillos.values());
                    int indice=Aleatorio.getInstance().siguienteEnteroArregloLocales(0, pasillos.size()-1);
                    PasilloState pasilloInstalado=pasillosInstalados.get(indice);
                    pasilloInstalado.agregarComercioImpulsado(comercio);
                }
            }
        }catch(Exception e){
            System.err.println(e);
            for(StackTraceElement stack:e.getStackTrace()){
                System.err.println(stack);
            }
        }
    }

    public ArrayList getRutaMasCortaALugarEspecifico(String aliasCliente, LugarState lugarDestino) {
        LugarState lugarActual=getLugarActualDeCliente(aliasCliente);
        return grafoLugares.rutaMasCorta(lugarActual, lugarDestino);
    }
    
    public void eliminarClientes() {
        try{
            for(String alias:clientes.keySet()){
                AgHandlerBESA agHandler=AdmBESA.getInstance().getHandlerByAlias(alias);
                agHandler.getAg().shutdownAgent();
            }
            clientes=new HashMap();
        }catch(Exception e){
            System.err.println("Error captuando el agente: "+e.getMessage());
        }
    }

    private Point rotarPunto(int x, int y, double anguloRotacion, Point pivote) {
        double x2 = ((x - pivote.x) * Math.cos(Math.toRadians(anguloRotacion))) - ((y - pivote.y) * Math.sin(Math.toRadians(anguloRotacion))) + pivote.x;
        double y2 = ((x - pivote.x) * Math.sin(Math.toRadians(anguloRotacion))) + ((y - pivote.y) * Math.cos(Math.toRadians(anguloRotacion))) + pivote.y;
        return new Point(Math.round(new Float(x2)),Math.round(new Float(y2)));   
    }

    private void leerParametrosCentroComercial() {
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            Element nodoRoot=(Element)doc.getElementsByTagName("Simall").item(0);
            this.clusterizado=Boolean.parseBoolean(nodoRoot.getAttribute("clusterizado"));
            this.anclasEnEntrada=Boolean.parseBoolean(nodoRoot.getAttribute("anclasEnEntrada"));
            this.cantidadVertices=Integer.parseInt(nodoRoot.getAttribute("cantidadVertices"));
            this.anchoUnidad=Integer.parseInt(nodoRoot.getAttribute("anchoUnidad"));
            this.anchoSeparador=Integer.parseInt(nodoRoot.getAttribute("anchoSeparador"));
            this.cantidadPasillosInteriores=Integer.parseInt(nodoRoot.getAttribute("cantidadPasillosInteriores"));
            this.maximaCantidadClientes=Integer.parseInt(nodoRoot.getAttribute("maximaCantidadClientes"));
            this.tiempoTotalSimulacionEnHoras=Double.parseDouble(nodoRoot.getAttribute("tiempoTotalSimulacionEnHoras"));
            this.cuadrosQueRecorreUnClienteXMinuto=Double.parseDouble(nodoRoot.getAttribute("cuadrosQueRecorreUnClienteXMinuto"));
            this.tiempoEnRecorrerUnCuadro=Double.parseDouble(nodoRoot.getAttribute("tiempoEnRecorrerUnCuadro"));

        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los parámetros del Centro Comercial: "+e.getMessage());
            System.exit(0);
        }
    }

    public ArrayList<ComercioState> getComerciosOferentes(Producto producto) {
        ArrayList<ComercioState> comerciosOferentes=new ArrayList();
        if(comercios!=null){
            Set<String> llaves=comercios.keySet();
            Iterator i=llaves.iterator();
            while(i.hasNext()){
                ComercioState comercio=(ComercioState)comercios.get((String)i.next());
                if(comercio!=null){
                   if(comercio.getInventario().contieneProducto(producto)){
                       if(!comerciosOferentes.contains(comercio)){
                           comerciosOferentes.add(comercio);
                       }
                   }
                }
            }
        }
        return comerciosOferentes;
    }
    
    public LugarState getLugarPorNombre(String nombreLugar){
        LugarState lugarCoincidente=null;
        ArrayList lugares=this.grafoLugares.getVertices();
        for(int i=0;i<lugares.size();i++){
            LugarState lugar=(LugarState)lugares.get(i);
            if(lugar.getAlias().equals(nombreLugar)){
                return lugar;
            }
        }
        return lugarCoincidente;
    }

    public NodoFuzzy getNodoFuzzyContribucionCompra() {
        return nodoFuzzyContribucionCompra;
    }

    public int getTiempoTotalSimulacion() {
        double factor=(this.tiempoEnRecorrerUnCuadro*this.cuadrosQueRecorreUnClienteXMinuto)/3600;
        return (int)Math.round(this.tiempoTotalSimulacionEnHoras*60000/factor);
    }

    public SanitarioState getSanitarioMasCercano(String aliasCliente) {
        SanitarioState sanitarioMasCercano=null;
        if(sanitarios!=null){
            if(sanitarios.size()>0){
                Set<String> nombresSanitarios=sanitarios.keySet();
                Iterator i=nombresSanitarios.iterator();
                int rutaMasCorta=-1;
                ArrayList ruta;
                while(i.hasNext()){
                    SanitarioState sanitario=(SanitarioState)sanitarios.get((String)i.next());
                    ruta=getRutaMasCortaALugarEspecifico(aliasCliente,sanitario);
                    if(ruta!=null){
                        if(rutaMasCorta==-1){
                            rutaMasCorta=ruta.size();
                            sanitarioMasCercano=sanitario;
                        }
                        else{
                            if(ruta.size()<rutaMasCorta){
                                rutaMasCorta=ruta.size();
                                sanitarioMasCercano=sanitario;
                            }
                        }
                    }
                }
                return sanitarioMasCercano;
            }
        }
        System.err.println("El centro comercial no tiene sanitarios!");
        System.exit(0);
        return sanitarioMasCercano;
    }
    
    public EntradaState getEntradaMasCercana(String aliasCliente) {
        EntradaState entradaMasCercana=null;
        if(entradas!=null){
            if(entradas.size()>0){
                Set<String> nombresEntradas=entradas.keySet();
                Iterator i=nombresEntradas.iterator();
                int rutaMasCorta=-1;
                ArrayList ruta;
                while(i.hasNext()){
                    EntradaState entrada=(EntradaState)entradas.get((String)i.next());
                    ruta=getRutaMasCortaALugarEspecifico(aliasCliente,entrada);
                    if(ruta!=null){
                        if(rutaMasCorta==-1){
                            rutaMasCorta=ruta.size();
                            entradaMasCercana=entrada;
                        }
                        else{
                            if(ruta.size()<rutaMasCorta){
                                rutaMasCorta=ruta.size();
                                entradaMasCercana=entrada;
                            }
                        }
                    }
                }
                return entradaMasCercana;
            }
        }
        System.err.println("El centro comercial no tiene entradas!");
        System.exit(0);
        return entradaMasCercana;
    }

    public ArrayList<LugarState> getLugaresRededor(LugarState lugar, int profundidad) {
        ArrayList<LugarState> lugaresRededor=new ArrayList();
        this.grafoLugares.getVericesCircundantesRecursivo(lugar,profundidad,lugaresRededor);
        return lugaresRededor;
    }

    public Color getColorComercioPorNichoClaro(String nicho) {
        if(!coloresClarosPorNicho.containsKey(nicho)){
            int r = Aleatorio.getInstance().siguienteEnteroArregloLocales(0, 155) + 100; // 128 ... 255
            int g = Aleatorio.getInstance().siguienteEnteroArregloLocales(0, 155) + 010; // 128 ... 255
            int b = Aleatorio.getInstance().siguienteEnteroArregloLocales(0, 155) + 001; // 128 ... 255
            Color color = Color.rgb(r,g,b,0.7f);
            color=color.brighter();
            coloresClarosPorNicho.put(nicho,color);
        }
        return coloresClarosPorNicho.get(nicho);
    }

    public Color getColorComercioPorNichoOscuro(String nicho) {
        Color color=coloresClarosPorNicho.get(nicho);
        return color.darker();
    }

    public void intentaComprar() {
        this.totalIntentosCompra++;
        
    }

    public void compraExitosa() {
        this.totalComprasExitosas++;
    }

    public void compraFallida() {
        this.totalComprasFallidas++;
    }
    
    public void mostrarCompras(){
        System.out.println("**************************************************");
        System.out.println("**************************************************");
        System.out.println("**************************************************");
        System.out.println("**************Resultados**************************");
        System.out.print(" Intentos de Compra:"+totalIntentosCompra);
        System.out.print(" Compras Exitosas:"+totalComprasExitosas);
        System.out.print(" Compras Fallidas:"+totalComprasFallidas);

    }
}
