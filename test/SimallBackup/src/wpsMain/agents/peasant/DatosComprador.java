/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Cotizacion;
import wpsMain.agents.peasant.Const.Semantica;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.ia.fuzzy.pares.EstructuraFuzzy;
import SIMALL.modelo.ia.fuzzy.pares.NodoFuzzy;
import wpsMain.util.Aleatorio;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dsvalencia
 */
public class DatosComprador {

    
    private static final int CRITERIO_BUSQUEDA_CERCANIA=0;
    private static final int CRITERIO_BUSQUEDA_CONVENIENCIA=1;
    private static final int CRITERIO_BUSQUEDA_INTERESES=2;
    
    
    private int tiempoDisponible; //Calculado en los minutos que tiene destinado para permanecer en el centro comercial
    private double porcentajePersuadible; //0 min, 1 max //Nivel de facilitad para cambiar de planes
    private double porcentajeImpulsivo; //0 min, 1 max //Nivel de raciociono para toma de decisiones
    private ArrayList<FacturaVenta> facturasVenta;
    
    private ComponenteEmocional componenteEmocional=null;
    
    private String aliasCliente;
    
    private HashMap<Necesidad,ArrayList<Cotizacion>> cotizaciones;
    
    private HashMap<Necesidad,ArrayList<ComercioState>> comerciosOferentesPorVisitar;
    
    private HashMap<Necesidad,ArrayList<ComercioState>> comerciosOferentesVisitados;

    private NodoFuzzy nodoFuzzyContribucionCompra;
    
    private final EstructuraFuzzy estructuraFuzzy=new EstructuraFuzzy();
    
    public ArrayList<FacturaVenta> getFacturasVenta() {
        return facturasVenta;
    }

    public void setFacturasVenta(ArrayList<FacturaVenta> facturasVenta) {
        this.facturasVenta = facturasVenta;
    }

    public HashMap<Necesidad, ArrayList<Cotizacion>> getCotizaciones() {
        return cotizaciones;
    }

    public void setCotizaciones(HashMap<Necesidad, ArrayList<Cotizacion>> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    public HashMap<Necesidad, ArrayList<ComercioState>> getComerciosOferentesPorVisitar() {
        return comerciosOferentesPorVisitar;
    }

    public void setComerciosOferentesPorVisitar(HashMap<Necesidad, ArrayList<ComercioState>> comerciosOferentesPorVisitar) {
        this.comerciosOferentesPorVisitar = comerciosOferentesPorVisitar;
    }

    public HashMap<Necesidad, ArrayList<ComercioState>> getComerciosOferentesVisitados() {
        return comerciosOferentesVisitados;
    }

    public void setComerciosOferentesVisitados(HashMap<Necesidad, ArrayList<ComercioState>> comerciosOferentesVisitados) {
        this.comerciosOferentesVisitados = comerciosOferentesVisitados;
    }


    public void registrarCotizacion(Necesidad necesidad, Cotizacion cotizacion){
        if(cotizaciones.containsKey(necesidad)){
            cotizaciones.get(necesidad).add(cotizacion);
        }
    }
    
    public DatosComprador(String aliasCliente, NodoFuzzy nodoFuzzyContribucionCompra, int tiempoDisponible, double porcentajePersuadible, double porcentajeImpulsivo) {
        this.aliasCliente=aliasCliente;
        this.nodoFuzzyContribucionCompra=nodoFuzzyContribucionCompra;
        this.tiempoDisponible = tiempoDisponible;
        this.porcentajePersuadible = porcentajePersuadible;
        this.porcentajeImpulsivo = porcentajeImpulsivo;
        facturasVenta=new ArrayList();
        cotizaciones=new HashMap();
        comerciosOferentesPorVisitar=new HashMap();
        comerciosOferentesVisitados=new HashMap();
    }
    
    public DatosComprador(DatosComprador datosComprador) {
        this.aliasCliente=datosComprador.aliasCliente;
        this.tiempoDisponible = datosComprador.tiempoDisponible;
        this.porcentajePersuadible = datosComprador.porcentajePersuadible;
        this.porcentajeImpulsivo = datosComprador.porcentajeImpulsivo;
        this.facturasVenta=null;
        if(datosComprador.facturasVenta!=null){
            this.facturasVenta=new ArrayList(datosComprador.facturasVenta);
        }
        this.cotizaciones=null;
        if(datosComprador.cotizaciones!=null){
            this.cotizaciones=new HashMap(datosComprador.cotizaciones);
        }
        this.comerciosOferentesPorVisitar=null;
        if(datosComprador.comerciosOferentesPorVisitar!=null){
            this.comerciosOferentesPorVisitar=new HashMap(datosComprador.comerciosOferentesPorVisitar);
        }
        this.comerciosOferentesVisitados=null;
        if(datosComprador.comerciosOferentesVisitados!=null){
            this.comerciosOferentesVisitados=new HashMap(datosComprador.comerciosOferentesVisitados);
        }
        this.nodoFuzzyContribucionCompra=null;
        if(datosComprador.nodoFuzzyContribucionCompra!=null){
            this.nodoFuzzyContribucionCompra=new NodoFuzzy(datosComprador.nodoFuzzyContribucionCompra);
        }
    }

    public double getTiempoDisponible() {
        return tiempoDisponible;
    }

    public void setTiempoDisponible(int tiempoDisponible) {
        this.tiempoDisponible = tiempoDisponible;
    }

    public double getPorcentajePersuadible() {
        return porcentajePersuadible;
    }

    public void setPorcentajePersuadible(double porcentajePersuadible) {
        this.porcentajePersuadible = porcentajePersuadible;
    }

    public double getPorcentajeImpulsivo() {
        return porcentajeImpulsivo;
    }

    public void setPorcentajeImpulsivo(double porcentajeImpulsivo) {
        this.porcentajeImpulsivo = porcentajeImpulsivo;
    }
    
    @Override
    public String toString(){
        
        String cadena="Datos del Comprador:\n\n";
        cadena+="\tTiempo Disponible:"+ tiempoDisponible+" minutos\n";
        cadena+="\tPersuadible: "+ porcentajePersuadible+"\n";
        cadena+="\tImpulsivo: "+ porcentajeImpulsivo+"\n";
        return cadena;
        
    }

    public ComercioState siguienteComercioACotizar(String aliasCliente, Necesidad necesidadEnCurso) {
        if(!cotizaciones.containsKey(necesidadEnCurso)){
            ArrayList<ComercioState> comerciosOferentes=MapaEstructural.getInstance().getComerciosOferentes(necesidadEnCurso.getProducto());
            this.comerciosOferentesPorVisitar.put(necesidadEnCurso,comerciosOferentes);
            cotizaciones.put(necesidadEnCurso, new ArrayList());
        }
        ArrayList<ComercioState> comerciosAEvaluar=this.comerciosOferentesPorVisitar.get(necesidadEnCurso);
        if(comerciosAEvaluar!=null){
            ComercioState comercioSeleccionado=seleccionarSiguienteComercioAVisitar(aliasCliente, comerciosAEvaluar, CRITERIO_BUSQUEDA_CERCANIA);
            if(comercioSeleccionado!=null){
                comerciosAEvaluar.remove(comercioSeleccionado);
                if(comerciosOferentesVisitados.containsKey(necesidadEnCurso)){
                    comerciosOferentesVisitados.get(necesidadEnCurso).add(comercioSeleccionado);
                }
                else{
                    ArrayList<ComercioState> listaComercios=new ArrayList();
                    comerciosOferentesVisitados.put(necesidadEnCurso,listaComercios);
                }
                return comercioSeleccionado;
            }
        }
        return null;
    }

    private ComercioState seleccionarSiguienteComercioAVisitar(String aliasCliente, ArrayList<ComercioState> comerciosAEvaluar, int criterio) {
        ComercioState siguienteComercio=null;
        if(comerciosAEvaluar!=null){
            switch(criterio){
                case CRITERIO_BUSQUEDA_CERCANIA: 
                    int rutaMasCorta=-1;
                    for(ComercioState comercio:comerciosAEvaluar){
                        if(rutaMasCorta==-1){
                            ArrayList ruta=MapaEstructural.getInstance().getRutaMasCortaALugarEspecifico(aliasCliente, comercio);
                            if(ruta!=null){
                                rutaMasCorta=ruta.size();
                                siguienteComercio=comercio;
                            }
                        }
                    }
                    break;
            }
        }
        return siguienteComercio;
    }

    public Cotizacion getMejorCotizacion(Necesidad necesidadEnCurso) {
        Cotizacion mejorCotizacion=null;
        HashMap<Cotizacion,Double> calificaciones=new HashMap();
        
        if(cotizaciones.containsKey(necesidadEnCurso)){
            ArrayList<Cotizacion> cotizacionesDeNecesidad=cotizaciones.get(necesidadEnCurso);
            double mejorCalificacion=-1;
            for(Cotizacion cotizacion:cotizacionesDeNecesidad){
                if(mejorCalificacion==-1){
                    mejorCalificacion=calificarCotizacion(cotizacion);
                    calificaciones.put(cotizacion,mejorCalificacion);
                    mejorCotizacion=cotizacion;
                }
                else{
                    double calificacionCotizacion=calificarCotizacion(cotizacion);
                    calificaciones.put(cotizacion,calificacionCotizacion);
                    if(mejorCalificacion<calificacionCotizacion){
                        mejorCalificacion=calificacionCotizacion;
                        mejorCotizacion=cotizacion;
                    }
                }
            }
            Set<Cotizacion> cotizacionesCalificadas=calificaciones.keySet();
            Iterator i=cotizacionesCalificadas.iterator();
            ArrayList<Cotizacion> mejoresCotizaciones=new ArrayList();
            while(i.hasNext()){
                Cotizacion cotizacion=(Cotizacion)i.next();
                if(calificaciones.get(cotizacion)==mejorCalificacion){
                    mejoresCotizaciones.add(cotizacion);
                }
            }
            int mejorRuta=-1;
            for(int j=0;j<mejoresCotizaciones.size();j++){
                if(mejorRuta==-1){
                    mejorRuta=MapaEstructural.getInstance().getRutaMasCortaALugarEspecifico(aliasCliente, MapaEstructural.getInstance().getLugarPorNombre(mejoresCotizaciones.get(j).getNombreComercio())).size();
                    mejorCotizacion=mejoresCotizaciones.get(j);
                }
                else{
                    int ruta=MapaEstructural.getInstance().getRutaMasCortaALugarEspecifico(aliasCliente, MapaEstructural.getInstance().getLugarPorNombre(mejoresCotizaciones.get(j).getNombreComercio())).size();
                    if(mejorRuta>ruta){
                        mejorRuta=ruta;
                        mejorCotizacion=mejoresCotizaciones.get(j);
                    }
                }
            }
        }
        return mejorCotizacion;
    }

    private double calificarCotizacion(Cotizacion cotizacion) {
        return -cotizacion.getPrecioUnitario();
    }

    public void realizarCompra(Necesidad necesidadEnCurso, ComercioState comercio, String nombreCliente, DatosEconomicos datosEconomicos) {
        MapaEstructural.getInstance().intentaComprar();
        if(necesidadEnCurso!=null){
            this.parametrizarNodosFuzzy(nodoFuzzyContribucionCompra);
            if(estructuraFuzzy.evaluar(nodoFuzzyContribucionCompra)>0.1){
                FacturaVenta factura=comercio.comprar(necesidadEnCurso, nombreCliente, datosEconomicos);
                if(factura!=null){
                    facturasVenta.add(factura);
                }
            }
            else{
                MapaEstructural.getInstance().compraFallida();
            }
        }
    }

    public void parametrizarNodosFuzzy(NodoFuzzy nodoFuzzy){
        try{
            if(nodoFuzzy.getNodosFuzzyHijos().isEmpty()){
                Object objeto=null;
                if(nodoFuzzy.getObjeto().equalsIgnoreCase("this")){
                    objeto=this;
                }
                else{
                    Field variable=this.getClass().getDeclaredField(nodoFuzzy.getObjeto());
                    objeto=variable.get(this);
                }
                Method method=objeto.getClass().getMethod(nodoFuzzy.getMetodo());
                nodoFuzzy.setValor(((Double)method.invoke(objeto)).doubleValue());
            }
            else{
                for(NodoFuzzy nodoFuzzyHijo:nodoFuzzy.getNodosFuzzyHijos()){
                    parametrizarNodosFuzzy(nodoFuzzyHijo);
                }
            }
        }catch(Exception e){
            System.err.println("Se presentó un error en la parametrización de los Nodos Fuzzy: "+e.getMessage());
            for(StackTraceElement st:e.getStackTrace()){
                System.err.println(st);
            }
            System.exit(0);
        }
    }
    
    public double getNecesidadAnalizada(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);
    }
    
    public double getEdadAnalizada(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);
    }
    
    public double getGustoAnalizado(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);
    }
    
    public double getDineroEfectivoAnaliazado(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);    
    }
    
    public double getPrecioAnalizado(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);    
    }
    
    public double getDisenoTiempoAnalizado(){
        return Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1);    
    }
    
    public double getEstadoEmocionalAnalizado(){
        if(componenteEmocional!=null){
            float valorEmcional=componenteEmocional.getEmotionAxis(Semantica.Emociones.Felicidad, Semantica.Emociones.Tristeza).getCurrentValue();
            return  ((valorEmcional+1)/2);
        }
        System.err.println("No se ha configurado el componente emocional.");
        System.exit(0);
        return -1;
    }

    public void setComponenteEmocional(ComponenteEmocional componenteEmocional) {
        this.componenteEmocional=componenteEmocional;
    }
    
    public ComponenteEmocional getComponenteEmocional(){
        return componenteEmocional;
    }
    
   
}
