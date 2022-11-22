/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import SIMALL.modelo.cc.agente.Producto;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.Const;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.Const.Semantica;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.EmotionalEvent;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.Personality;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.SemanticDictionary;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.IniciarLogroDeMetasGuard;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.MensajePromocional;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.PaqueteMensajesPromocionales;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.obligacion.SatisfacerNecesidadesGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.ComprarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.SalirGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.CotizarNecesidadGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.supervivencia.AlimentarseGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.supervivencia.IrAlSanitarioGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.util.Aleatorio;
import java.util.ArrayList;
import java.util.HashMap;
import rational.data.InfoData;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class ClienteBDIBelieves implements Believes {

    private String alias;
    private ManejadorCliente manejadorCliente;
    
    private ArrayList<Interes> intereses;
    private DatosComprador datosComprador;
    private DatosDemograficos datosDemograficos;
    private DatosEconomicos datosEconomicos;
    
    private ArrayList<Necesidad> necesidadesSinSatisfacer;
    
    private ArrayList<Necesidad> necesidadesDeAlimentacion;
    private Necesidad necesidadAlimentacionPriorizada;

    public Necesidad getNecesidadAlimentacionPriorizada() {
        return necesidadAlimentacionPriorizada;
    }

    public void setNecesidadAlimentacionPriorizada(Necesidad necesidadAlimentacionPriorizada) {
        this.necesidadAlimentacionPriorizada = necesidadAlimentacionPriorizada;
    }

    public ArrayList<Necesidad> getNecesidadesDeAlimentacion() {
        return necesidadesDeAlimentacion;
    }

    public void setNecesidadesDeAlimentacion(ArrayList<Necesidad> necesidadesDeAlimentacion) {
        this.necesidadesDeAlimentacion = necesidadesDeAlimentacion;
    }
    
    private ArrayList<Necesidad> necesidadesSatisfechas;
    private ArrayList<Necesidad> necesidadesInsatisfizas;
    
    private Necesidad necesidadEnCurso;

    private ComponenteEmocional componenteEmocional;
    
    
    public HashMap<String, Integer> getEstadoMetas() {
        return estadoMetas;
    }

    public void setEstadoMetas(HashMap<String, Integer> estadoMetas) {
        this.estadoMetas = estadoMetas;
    }

   
    private HashMap<String,Integer> estadoMetas;
    
    public void setEstadoMeta(String meta, int estado){
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=getNombreYEstadoDeMeta(meta,estado);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        System.out.println("Cambia Estado Meta "+alias+" "+nombreMeta+" "+nombreEstado);
        estadoMetas.put(meta, estado);
        //LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), getAlias(), "Cambia Estado Meta "+nombreMeta+" A "+nombreEstado);
        
        /*Set<String> metas=estadoMetas.keySet();
        Iterator i=metas.iterator();
        while(i.hasNext()){
            meta=(String)i.next();
            estado=estadoMetas.get(meta);
            nombreYEstado=getNombreYEstadoDeMeta(meta,estado);
            if(nombreYEstado!=null){
                if(nombreYEstado.size()==2){
                    nombreMeta=nombreYEstado.get(0);
                    nombreEstado=nombreYEstado.get(1);
                }
            }
            System.out.println(nombreMeta+"\t|"+nombreEstado);
        }*/
    }
    
    public ArrayList<String> getNombreYEstadoDeMeta(String meta, int estado){
        ArrayList<String> nombreYEsyadoMeta=new ArrayList();
        if(meta.equals(SatisfacerNecesidadesGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("SatisfacerNecesidadesGoalBDI");
            switch(estado){
                case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_ASIGNANDO_NECESIDAD");
                    break;
                case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA");
                    break;
                case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COTIZACIONES:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_EVALUAR_COTIZACIONES");
                    break;
                case SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COMPRA:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_REALIZAR_COMPRA");
                    break;
                case SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COTIZACIONES:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_REALIZAR_COTIZACIONES");
                    break;
                case SatisfacerNecesidadesGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
            }
        }
        if(meta.equals(CotizarNecesidadGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("CotizarNecesidadGoalBDI");
            switch(estado){
                case CotizarNecesidadGoalBDI.ESTADO_EJECUCION_COTIZANDO_NECESIDAD:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_COTIZANDO_NECESIDAD");
                    break;
                case CotizarNecesidadGoalBDI.ESTADO_EJECUCION_OBTENIENDO_COTIZACION:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_OBTENIENDO_COTIZACION");
                    break;
                case CotizarNecesidadGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_TRASLADO_CLIENTE");
                    break;
                case CotizarNecesidadGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case CotizarNecesidadGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        if(meta.equals(TrasladarseALugarGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("TrasladarseALugarGoalBDI");
            switch(estado){
                case TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_TRASLADO_EN_CURSO");
                    break;
                case TrasladarseALugarGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        if(meta.equals(IrAlSanitarioGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("IrAlSanitarioGoalBDI");
            switch(estado){
                case IrAlSanitarioGoalBDI.ESTADO_EJECUCION_USAR_SANITARIO:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_USAR_SANITARIO");
                    break;
                case IrAlSanitarioGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_TRASLADO_CLIENTE");
                    break;
                case IrAlSanitarioGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case IrAlSanitarioGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        if(meta.equals(AlimentarseGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("AlimentarseGoalBDI");
            switch(estado){
                case AlimentarseGoalBDI.ESTADO_EJECUCION_PRIORIZA_ALIMENTACION:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_PRIORIZA_ALIMENTACION");
                    break;
                case AlimentarseGoalBDI.ESTADO_ESPERA_ALIMENTARSE:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_ALIMENTARSE");
                    break;
                case AlimentarseGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case AlimentarseGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        if(meta.equals(ComprarGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("ComprarGoalBDI");
            switch(estado){
                case ComprarGoalBDI.ESTADO_EJECUCION_REALIZAR_COMPRA:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_REALIZAR_COMPRA");
                    break;
                case ComprarGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_TRASLADO_CLIENTE");
                    break;
                case ComprarGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case ComprarGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        if(meta.equals(SalirGoalBDI.class.getName())){
            nombreYEsyadoMeta.add("SalirGoalBDI");
            switch(estado){
                case SalirGoalBDI.ESTADO_EJECUCION_SALIR:
                    nombreYEsyadoMeta.add("ESTADO_EJECUCION_SALIR");
                    break;
                case SalirGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE:
                    nombreYEsyadoMeta.add("ESTADO_ESPERA_TRASLADO_CLIENTE");
                    break;
                case SalirGoalBDI.ESTADO_FINALIZADA:
                    nombreYEsyadoMeta.add("ESTADO_FINALIZADA");
                    break;
                case SalirGoalBDI.ESTADO_SIN_INICIAR:
                    nombreYEsyadoMeta.add("ESTADO_SIN_INICIAR");
                    break;
            }
        }
        return nombreYEsyadoMeta;
    }
    
    public int getEstadoMeta(String nombreMeta){
        if(!estadoMetas.containsKey(nombreMeta)){
            if(nombreMeta.equals(SatisfacerNecesidadesGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, SatisfacerNecesidadesGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(CotizarNecesidadGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, CotizarNecesidadGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(ComprarGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, ComprarGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(TrasladarseALugarGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(IrAlSanitarioGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, IrAlSanitarioGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(AlimentarseGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, AlimentarseGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(AlimentarseGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, AlimentarseGoalBDI.ESTADO_SIN_INICIAR);
            }
            if(nombreMeta.equals(SalirGoalBDI.class.getName())){
                estadoMetas.put(nombreMeta, SalirGoalBDI.ESTADO_SIN_INICIAR);
            }
        }
        return estadoMetas.get(nombreMeta);
    }

    public ArrayList<Necesidad> getNecesidadesSatisfechas() {
        return necesidadesSatisfechas;
    }

    public void setNecesidadesSatisfechas(ArrayList<Necesidad> necesidadesSatisfechas) {
        this.necesidadesSatisfechas = necesidadesSatisfechas;
    }

    public ArrayList<Necesidad> getNecesidadesInsatisfizas() {
        return necesidadesInsatisfizas;
    }

    public void setNecesidadesNoRealizable(ArrayList<Necesidad> necesidadesInsatisfizas) {
        this.necesidadesInsatisfizas = necesidadesInsatisfizas;
    }

    public Necesidad getNecesidadEnCurso() {
        return necesidadEnCurso;
    }

    public void setNecesidadEnCurso(Necesidad necesidadEnCurso) {
        this.necesidadEnCurso = necesidadEnCurso;
    }
    
    public ArrayList<Necesidad> getNecesidadesSinSatisfacer() {
        return necesidadesSinSatisfacer;
    }

    public void setNecesidadesSinSatisfacer(ArrayList<Necesidad> necesidadesSinSatisfacer) {
        if(necesidadesSinSatisfacer!=null){
            for(Necesidad necesidad:necesidadesSinSatisfacer){
                agregarNecesidadSinSatisfacer(necesidad);
            }
        }
    }

    public ArrayList<Interes> getIntereses() {
        return intereses;
    }

    public void setIntereses(ArrayList<Interes> intereses) {
        this.intereses = intereses;
    }

    public DatosComprador getDatosComprador() {
        return datosComprador;
    }

    public void setDatosComprador(DatosComprador datosComprador) {
        this.datosComprador = datosComprador;
    }

    public DatosDemograficos getDatosDemograficos() {
        return datosDemograficos;
    }

    public void setDatosDemograficos(DatosDemograficos datosDemograficos) {
        this.datosDemograficos = datosDemograficos;
    }

    public DatosEconomicos getDatosEconomicos() {
        return datosEconomicos;
    }

    public void setDatosEconomicos(DatosEconomicos datosEconomicos) {
        this.datosEconomicos = datosEconomicos;
    }

    public ComponenteEmocional getComponenteEmocional() {
        return componenteEmocional;
    }

    public void setComponenteEmocional(ComponenteEmocional componenteEmocional) {
        this.componenteEmocional = componenteEmocional;
    }

    public ClienteBDIBelieves(String alias, ArrayList<Interes> intereses, DatosComprador datosComprador, DatosDemograficos datosDemograficos, DatosEconomicos datosEconomicos) {
        this.alias = alias;
        this.manejadorCliente = new ManejadorCliente(alias);
        this.componenteEmocional = new ComponenteEmocional();
        configurarDiccionarioSemantico();
        configurarComponenteEmocional();
        ArrayList<Necesidad> necesidadesAleatorias=MapaEstructural.getInstance().getArbolCategorias().getNecesidadesAsequiblesAleatorias(datosDemograficos, datosEconomicos, componenteEmocional);
        this.necesidadesSinSatisfacer = new ArrayList();
        for(Necesidad necesidadAleatoria:necesidadesAleatorias){
            agregarNecesidadSinSatisfacer(necesidadAleatoria);
        }
        this.necesidadesDeAlimentacion=new ArrayList();
        if(necesidadesSinSatisfacer!=null){
            for(Necesidad necesidad:necesidadesSinSatisfacer){
                if(necesidad.getProducto().getNicho().equalsIgnoreCase("Restaurante")){
                    necesidadesDeAlimentacion.add(necesidad);
                }
            }
        }
        if(necesidadesDeAlimentacion.isEmpty()){
            necesidadesDeAlimentacion=MapaEstructural.getInstance().getArbolCategorias().getCategoria("Restaurante").getNecesidadesAsequiblesAleatorias(datosDemograficos, datosEconomicos, componenteEmocional);
            if(necesidadesDeAlimentacion!=null){
                for(Necesidad necesidadDeAlimentacion:necesidadesDeAlimentacion){
                    agregarNecesidadSinSatisfacer(necesidadDeAlimentacion);
                }
            }
        }
        this.intereses = intereses;
        this.datosComprador = datosComprador;
        this.datosDemograficos = datosDemograficos;
        this.datosEconomicos = datosEconomicos;
        this.datosComprador.setComponenteEmocional(componenteEmocional);
        this.necesidadesInsatisfizas=new ArrayList();
        this.necesidadesSatisfechas=new ArrayList();
        this.estadoMetas=new HashMap();
        System.out.println(this);
        LogAuditoria.getInstance().escribirMensaje(ClienteBDIBelieves.class.getName(), alias, this.toString());
    }

    public ManejadorCliente getManejadorCliente() {
        return manejadorCliente;
    }

    @Override
    public boolean update(InfoData si) {
        return true;
    }

    public String getAlias() {
        return alias;
    }
    
    @Override
    public String toString() {
        String cadena = "ClienteBDIBelieves: \n";
        cadena += "Alias: " + alias + "\n";
        cadena += "\nNecesidades:\n\n";
        for (int i = 0; i < necesidadesSinSatisfacer.size(); i++) {
            cadena += "\tIntensidad: " + necesidadesSinSatisfacer.get(i).getIntensidad() + " Producto: " + necesidadesSinSatisfacer.get(i).getProducto().getNombre() + " Marca: " + necesidadesSinSatisfacer.get(i).getProducto().getMarca() + "\n";
        }
        cadena += "\nIntereses:\n\n";
        for (int i = 0; i < intereses.size(); i++) {
            cadena += "\tIntensidad: " + intereses.get(i).getIntensidad() + " Categoria: " + intereses.get(i).getCategoria().getNombre()+"\n";
        }
        cadena += "\n"+datosComprador;
        cadena += "\n"+datosDemograficos;
        cadena += "\n"+datosEconomicos;
        return cadena;
    }

    @Override
    public Believes clone(){
        /*ArrayList<Necesidad> necesidadesSinSatisfacerClone=null;
        if(this.necesidadesSinSatisfacer!=null){
            necesidadesSinSatisfacerClone=new ArrayList(this.necesidadesSinSatisfacer);
        }
        ArrayList<Interes> interesesClone=null;
        if(this.intereses!=null){
            interesesClone=new ArrayList(this.intereses);
        }
        DatosComprador datosCompradorClone=new DatosComprador(this.datosComprador);
        DatosDemograficos datosDemograficosClone=new DatosDemograficos(this.datosDemograficos);
        DatosEconomicos datosEconomicosClone=new DatosEconomicos(this.datosEconomicos);
        ComponenteEmocional componenteEmocionalClone=new ComponenteEmocional(this.componenteEmocional);
        
        ClienteBDIBelieves clienteBDIBelievesClone = new ClienteBDIBelieves(new String(alias), necesidadesSinSatisfacerClone, interesesClone, datosCompradorClone, datosDemograficosClone, datosEconomicosClone, componenteEmocionalClone);
        clienteBDIBelievesClone.manejadorCliente=new ManejadorCliente(this.manejadorCliente);
        clienteBDIBelievesClone.necesidadesSatisfechas=null;
        if(this.necesidadesSatisfechas!=null){
            clienteBDIBelievesClone.necesidadesSatisfechas=new ArrayList(this.necesidadesSatisfechas);
        }
        clienteBDIBelievesClone.necesidadesNoRealizables=null;
        if(this.necesidadesNoRealizables!=null){
            clienteBDIBelievesClone.necesidadesNoRealizables=new ArrayList(this.necesidadesNoRealizables);
        }
        clienteBDIBelievesClone.necesidadEnCurso=null;
        if(this.necesidadEnCurso!=null){
            clienteBDIBelievesClone.necesidadEnCurso=new Necesidad(this.necesidadEnCurso);
        }
        clienteBDIBelievesClone.estadoMetas=null;
        if(this.estadoMetas!=null){
            clienteBDIBelievesClone.estadoMetas=new HashMap(this.estadoMetas);
        }
        return clienteBDIBelievesClone;*/
        return this;
    }
    
    private void configurarComponenteEmocional() {
        
        configurarDiccionarioSemantico();
        
        this.componenteEmocional=new ComponenteEmocional();
        
        componenteEmocional.agregarEjeEmocional(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, 0.0f, 0.0f, 0.05f);
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaCompra, 0.1f);
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.RecibePublicidad, 0.8f);
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaSuciedad, 0.5f);
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaRobo, 0.7f);
        
        componenteEmocional.agregarEjeEmocional(Const.Semantica.Sensaciones.SinHambre, Const.Semantica.Sensaciones.Hambre, (float)Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1), 0.0f, 0.1f/*(float)Aleatorio.getInstance().siguienteDobleGeneracionClientes(0.002, 0.005)*/);
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Sensaciones.SinHambre, Const.Semantica.Sensaciones.Hambre, Const.Semantica.Eventos.SeAlimenta, 1.0f);
        
        componenteEmocional.agregarEjeEmocional(Const.Semantica.Sensaciones.NoNecesitaSanitario, Const.Semantica.Sensaciones.NecesitaSanitario, (float)Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1), 0.0f, (float)Aleatorio.getInstance().siguienteDobleGeneracionClientes(0.002, 0.005));
        componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Sensaciones.NoNecesitaSanitario, Const.Semantica.Sensaciones.NecesitaSanitario, Const.Semantica.Eventos.UtilizaSaniario, 1.0f);
        
        
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.RecibePublicidad, Const.Semantica.ValoracionEventos._4_AlgoDeseable.getName());
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.ObservaCompra, Const.Semantica.ValoracionEventos._5_Deseable.getName());
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.ObservaRobo, Const.Semantica.ValoracionEventos._1_Indeseable.getName());
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.ObservaSuciedad, Const.Semantica.ValoracionEventos._1_Indeseable.getName());
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.SeAlimenta, Const.Semantica.ValoracionEventos._5_Deseable.getName());
        componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.UtilizaSaniario, Const.Semantica.ValoracionEventos._5_Deseable.getName());
        
        componenteEmocional.configurarRelacionConObjeto(Const.Semantica.Objetos.Producto, Const.Semantica.ValoracionObjetos._1_Repulsivo.getName());
        componenteEmocional.configurarRelacionConObjeto(Const.Semantica.Objetos.Almacen, Const.Semantica.ValoracionObjetos._4_Valioso.getName());
        componenteEmocional.configurarRelacionConObjeto(Const.Semantica.Objetos.Sanitario, Const.Semantica.ValoracionObjetos._5_Importante.getName());
        componenteEmocional.configurarRelacionConObjeto(Const.Semantica.Objetos.Alimento, Const.Semantica.ValoracionObjetos._5_Importante.getName());
        
        componenteEmocional.configurarRelacionConPersona(Const.Semantica.Personas.Comprador, Const.Semantica.ValoracionPersonas._3_Desconocido.getName());
        componenteEmocional.configurarRelacionConPersona(Const.Semantica.Personas.Transeunte, Const.Semantica.ValoracionPersonas._3_Desconocido.getName());
        componenteEmocional.configurarRelacionConPersona(Const.Semantica.Personas.Vendedor, Const.Semantica.ValoracionPersonas._1_Enemigo.getName());
        componenteEmocional.configurarRelacionConPersona(Const.Semantica.Personas.Ladron, Const.Semantica.ValoracionPersonas._1_Enemigo.getName());
    }
    
    public void agregarNecesidadSinSatisfacer(Necesidad necesidadSinSatisfacer){
        if(necesidadSinSatisfacer!=null){
            Producto producto=necesidadSinSatisfacer.getProducto();
            componenteEmocional.agregarEjeEmocional(Const.Semantica.Emociones.Felicidad+producto.getNombre(), Const.Semantica.Emociones.Tristeza+producto.getNombre(), (float)necesidadSinSatisfacer.getIntensidad(), 0.0f, 0.01f);
            componenteEmocional.configurarInfluenciaDeEvento(Const.Semantica.Emociones.Felicidad+producto.getNombre(), Const.Semantica.Emociones.Tristeza+producto.getNombre(), Const.Semantica.Eventos.RecibePublicidad, 1.0f);
            componenteEmocional.configurarDeseoDeEvento(Const.Semantica.Eventos.RecibePublicidad+producto.getNombre(), Const.Semantica.ValoracionEventos._4_AlgoDeseable.getName());
            necesidadesSinSatisfacer.add(necesidadSinSatisfacer);
        }
    }
    
    private static void configurarDiccionarioSemantico() {
        SemanticDictionary sd = SemanticDictionary.getInstance();
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._1_Repulsivo);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._2_NoValioso);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._3_Indiferente);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._4_Valioso);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._5_Importante);

        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._1_Enemigo);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._2_NoAmigable);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._3_Desconocido);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._4_Amigo);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._5_Cercano);

        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._1_Indeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._2_AlgoIndeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._3_Indiferente);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._4_AlgoDeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._5_Deseable);
    }

    public void usarSanitario() {
        EmotionalEvent ev=new EmotionalEvent(Const.Semantica.Personas.Comprador, Const.Semantica.Eventos.UtilizaSaniario, Const.Semantica.Objetos.Sanitario);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);        
    }
    
    public void alimentarse() {
        EmotionalEvent ev=new EmotionalEvent(Const.Semantica.Personas.Comprador, Const.Semantica.Eventos.SeAlimenta, Const.Semantica.Objetos.Alimento);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);
        this.componenteEmocional.processEmotionalEvent(ev);        
    }

    public boolean priorizarAlimentacion() {
        double maximaIntensidadAlimentacion=-1;
        this.necesidadAlimentacionPriorizada=null;
        Necesidad necesidadAlimentacion=null;
        
        for(Necesidad necesidad:necesidadesSinSatisfacer){
            if(necesidad.getProducto().getNicho().equals("Restaurante")){
                if(maximaIntensidadAlimentacion==-1){
                    maximaIntensidadAlimentacion=necesidad.getIntensidad();
                    necesidadAlimentacion=necesidad;
                }
                else{
                    if(maximaIntensidadAlimentacion<necesidad.getIntensidad()){
                        maximaIntensidadAlimentacion=necesidad.getIntensidad();
                        necesidadAlimentacion=necesidad;
                    }
                }
            }
        }
        if(necesidadAlimentacion!=null){
            necesidadAlimentacionPriorizada=necesidadAlimentacion;
            necesidadAlimentacionPriorizada.setIntensidad(1f);
            return true;
        }
        return false;
    }

    public Necesidad getNecesidadSinSatisfacerMaximaPrioridad() {
        Necesidad necesidadMaximaPrioridad=null;
        if(necesidadesSinSatisfacer!=null){
            double mayorIntensidad=-1;
            for(Necesidad necesidad:necesidadesSinSatisfacer){
                if(mayorIntensidad==-1){
                    mayorIntensidad=necesidad.getIntensidad();
                    necesidadMaximaPrioridad=necesidad;
                }
                else{
                    if(mayorIntensidad<necesidad.getIntensidad()){
                        mayorIntensidad=necesidad.getIntensidad();
                        necesidadMaximaPrioridad=necesidad;
                    }
                }
            }
        }
        return necesidadMaximaPrioridad;
    }

    public void procesarMensajesPromocionales(PaqueteMensajesPromocionales paqueteMensajesPromocionales) {
        if(paqueteMensajesPromocionales!=null){
            ArrayList<ArrayList<MensajePromocional>> totalMensajesPromocionales=paqueteMensajesPromocionales.getTotalMensajesPromocionales();
            if(totalMensajesPromocionales!=null){
                for(ArrayList<MensajePromocional> mensajesPromocionales:totalMensajesPromocionales){
                    for(MensajePromocional mensajePromocional:mensajesPromocionales){
                        if(mensajePromocional!=null){
                            ArrayList<Producto> productosEnPromocion=mensajePromocional.getProductosEnPromocion();
                            for(Producto productoEnPromocion:productosEnPromocion){
                                if(datosEconomicos.evaluarCapacidadPago(productoEnPromocion)){
                                    if(necesidadesSinSatisfacer!=null){
                                        boolean necesidadExistente=false;
                                        for(Necesidad necesidad:necesidadesSinSatisfacer){
                                            if(necesidad.getProducto().getNombre().equals(productoEnPromocion.getNombre())){
                                                EmotionalEvent ev=new EmotionalEvent(mensajePromocional.getNombreComercio(),Semantica.Eventos.RecibePublicidad+productoEnPromocion.getNombre(), productoEnPromocion.getNombre());
                                                componenteEmocional.processEmotionalEvent(ev);
                                                necesidadExistente=true;
                                                break;
                                            }
                                        }
                                        if(!necesidadExistente){
                                            if(intereses!=null){
                                                for(Interes interes:intereses){
                                                    if(interes.getCategoria().getNombre().equals(productoEnPromocion.getCategoria())){
                                                        if(Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1)>=0.95){
                                                            Necesidad nuevaNecesidad=new Necesidad(productoEnPromocion,componenteEmocional,1f);
                                                            agregarNecesidadSinSatisfacer(nuevaNecesidad);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void salir() {
        try{
            PeriodicDataBESA data = new PeriodicDataBESA(PeriodicGuardBESA.SUSPEND_PERIODIC_CALL);
            AgHandlerBESA agHandlerCliente = AdmBESA.getInstance().getHandlerByAlias(this.getAlias());
            EventBESA eventBesaCliente = new EventBESA(IniciarLogroDeMetasGuard.class.getName(),data);
            agHandlerCliente.sendEvent(eventBesaCliente);
            System.out.println("Detinene Guarda Periódica");
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAlias(), e);
        }
    }
}
