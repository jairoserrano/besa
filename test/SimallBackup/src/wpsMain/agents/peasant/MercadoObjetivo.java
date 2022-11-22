/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.ia.fuzzy.pares.NodoFuzzy;
import wpsMain.util.Aleatorio;
import wpsMain.util.Punto;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class MercadoObjetivo {
    
    private AtributoMercado edad;
    private AtributoMercado genero;
    private AtributoMercado nivelSocioeconomico;
    private AtributoMercado ocupacion;
    private AtributoMercado escolaridad;
    
    private boolean esMercadoObjetivoPorDefecto=true;
    
    private int cantidadClientes=0;
    
    public AtributoMercado getEdad() {
        return edad;
    }

    public void setEdad(AtributoMercado edad) {
        esMercadoObjetivoPorDefecto=false;
        this.edad = edad;
    }

    public AtributoMercado getGenero() {
        return genero;
    }

    public void setGenero(AtributoMercado genero) {
        esMercadoObjetivoPorDefecto=false;
        this.genero = genero;
    }

    public AtributoMercado getNivelSocioeconomico() {
        return nivelSocioeconomico;
    }

    public void setNivelSocioeconomico(AtributoMercado nivelSocioeconomico) {
        esMercadoObjetivoPorDefecto=false;
        this.nivelSocioeconomico = nivelSocioeconomico;
    }

    public AtributoMercado getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(AtributoMercado ocupacion) {
        esMercadoObjetivoPorDefecto=false;
        this.ocupacion = ocupacion;
    }

    public AtributoMercado getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(AtributoMercado escolaridad) {
        esMercadoObjetivoPorDefecto=false;
        this.escolaridad = escolaridad;
    }


    public int getCantidadClientes() {
        return cantidadClientes;
    }

    public void setCantidadClientes(int cantidadClientes) {
        esMercadoObjetivoPorDefecto=false;
        this.cantidadClientes = cantidadClientes;
    }
    
    public MercadoObjetivo(){
        edad=new AtributoMercado();
        genero=new AtributoMercado();
        nivelSocioeconomico=new AtributoMercado();
        ocupacion=new AtributoMercado();
        escolaridad=new AtributoMercado();
    }
    
    public boolean agregarRegistroAtributoMercado(RegistroAtributoMercado registroAtributoMercado){
        if(registroAtributoMercado!=null){
            switch(registroAtributoMercado.getTipo()){
                case RegistroAtributoMercado.TIPO_EDAD: return edad.agregarRegistroAtributoMercado(registroAtributoMercado);
                case RegistroAtributoMercado.TIPO_ESCOLARIDAD: return escolaridad.agregarRegistroAtributoMercado(registroAtributoMercado);
                case RegistroAtributoMercado.TIPO_GENERO: return genero.agregarRegistroAtributoMercado(registroAtributoMercado);
                case RegistroAtributoMercado.TIPO_NIVEL_SOCIOECONOMICO: return nivelSocioeconomico.agregarRegistroAtributoMercado(registroAtributoMercado);
                case RegistroAtributoMercado.TIPO_OCUPACION: return ocupacion.agregarRegistroAtributoMercado(registroAtributoMercado);
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        String cadena="Mercado Objetivo:";
        if(esMercadoObjetivoPorDefecto){
            return cadena+=" Valores por Defecto\n";
        }
        if( edad.getRegistrosAtributoMercado().isEmpty()&&
            genero.getRegistrosAtributoMercado().isEmpty()&&
            nivelSocioeconomico.getRegistrosAtributoMercado().isEmpty()&&
            ocupacion.getRegistrosAtributoMercado().isEmpty()&&
            escolaridad.getRegistrosAtributoMercado().isEmpty()){
            return cadena+=" SIN MERCADO OBJETIVO\n";
        }
        cadena+="\n";
        if(!genero.getRegistrosAtributoMercado().isEmpty()){
            cadena+="   Generos:\n";
            for(RegistroAtributoMercado registro:genero.getRegistrosAtributoMercado()){
                cadena+="       P="+registro.getPorcentajeMercado()+" valor="+registro.getValor()+"\n";
            }
        }
        if(!edad.getRegistrosAtributoMercado().isEmpty()){
            cadena+="   Edades:\n";
            for(RegistroAtributoMercado registro:edad.getRegistrosAtributoMercado()){
                cadena+="       P="+registro.getPorcentajeMercado()+" desde="+((Punto)registro.getValor()).getX()+" hasta="+((Punto)registro.getValor()).getY()+"\n";
            }
        }
        if(!nivelSocioeconomico.getRegistrosAtributoMercado().isEmpty()){
            cadena+="   Nivel Socioeconómico:\n";
            for(RegistroAtributoMercado registro:nivelSocioeconomico.getRegistrosAtributoMercado()){
                cadena+="       P="+registro.getPorcentajeMercado()+" valor="+registro.getValor()+"\n";
            }
        }
        if(!ocupacion.getRegistrosAtributoMercado().isEmpty()){
            cadena+="   Ocupación:\n";
            for(RegistroAtributoMercado registro:ocupacion.getRegistrosAtributoMercado()){
                cadena+="       P="+registro.getPorcentajeMercado()+" valor="+registro.getValor()+"\n";
            }
        }
        if(!escolaridad.getRegistrosAtributoMercado().isEmpty()){
            cadena+="   Escolaridad:\n";
            for(RegistroAtributoMercado registro:escolaridad.getRegistrosAtributoMercado()){
                cadena+="       P="+registro.getPorcentajeMercado()+" valor="+registro.getValor()+"\n";
            }
        }
        return cadena;
    }
    
    public boolean esMercadoObjetivoPorDefecto() {
        return esMercadoObjetivoPorDefecto;
    }

    public boolean evaluarCompatibilidad(DatosDemograficos datosDemograficos) {
        if(esMercadoObjetivoPorDefecto){
            return true;
        }
        else{
            if(edad.evaluarCompatibilidad(datosDemograficos.getEdad())){
                if(genero.evaluarCompatibilidad(datosDemograficos.getEscolaridad())){
                    if(nivelSocioeconomico.evaluarCompatibilidad(datosDemograficos.getEscolaridad())){
                        if(escolaridad.evaluarCompatibilidad(datosDemograficos.getEscolaridad())){
                            if(ocupacion.evaluarCompatibilidad(datosDemograficos.getEscolaridad())){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    
    
    public DatosDemograficos generarDatosDemogaficosAleatorios(String nombre) {
        Aleatorio aleatorio=Aleatorio.getInstance();
        int generoAleatorio=generarGeneroAleatorio();
        int edadAleatorio=generarEdadAleatoria();
        int[] ocupacionesAleatorio=generarOcupacionesAleatoria();
        int escolaridadAleatorio=generarEscolaridadAleatoria();
        int nivelSocioeconomicoAleatorio=generarNivelSocioeconomicoAleatorio();
        double porcentajeNecesidadSanitarioAleatorio=-1;
        if(edadAleatorio<=10||edadAleatorio>60){
            porcentajeNecesidadSanitarioAleatorio=0.5;
        }
        if(edadAleatorio>40||edadAleatorio<=60){
            porcentajeNecesidadSanitarioAleatorio=0.4;
        }
        if(edadAleatorio>10||edadAleatorio<=20){
            porcentajeNecesidadSanitarioAleatorio=0.3;
        }
        if(edadAleatorio>20||edadAleatorio<=40){
            porcentajeNecesidadSanitarioAleatorio=0.2;
        }
        return new DatosDemograficos(nombre, generoAleatorio, edadAleatorio, ocupacionesAleatorio, escolaridadAleatorio, nivelSocioeconomicoAleatorio, porcentajeNecesidadSanitarioAleatorio);
    }
    
    public DatosComprador generarDatosCompradorAleatorios(String aliasCliente) {
        Aleatorio aleatorio=Aleatorio.getInstance();
        int tiempoDisponible=aleatorio.siguienteEnteroGeneracionClientes(0, 100);
        double porcentajePersuadible=aleatorio.siguienteDobleGeneracionClientes(0, 1);
        double porcentajeImpulsivo=aleatorio.siguienteDobleGeneracionClientes(0, 1);
        return new DatosComprador(aliasCliente, new NodoFuzzy(MapaEstructural.getInstance().getNodoFuzzyContribucionCompra()),tiempoDisponible, porcentajePersuadible, porcentajeImpulsivo);
    }
    
    private int generarGeneroAleatorio(){
        ArrayList<RegistroAtributoMercado> registros=genero.getRegistrosAtributoMercado();
        double [] porcentajes=new double[registros.size()];
        for(int i=0;i<registros.size();i++){
            porcentajes[i]=registros.get(i).getPorcentajeMercado();
        }
        String valorGenero=(String)registros.get(Aleatorio.getInstance().siguienteOpcionGeneracionClientes(porcentajes)).getValor();
        int genero=-1;
        if(valorGenero.equalsIgnoreCase("femenino")){
            genero=DatosDemograficos.GENERO_FEMENINO;
        }
        else{
            if(valorGenero.equalsIgnoreCase("masculino")){
                genero=DatosDemograficos.GENERO_MASCULINO;
            }
            else{
                if(valorGenero.equalsIgnoreCase("lgbt")){
                    genero=DatosDemograficos.GENERO_LGBT;
                }
            }
        }
        return genero;
    } 

    private int generarEdadAleatoria() {
        ArrayList<RegistroAtributoMercado> registros=edad.getRegistrosAtributoMercado();
        double [] porcentajes=new double[registros.size()];
        for(int i=0;i<registros.size();i++){
            porcentajes[i]=registros.get(i).getPorcentajeMercado();
        }
        Punto rangoEdad=(Punto)registros.get(Aleatorio.getInstance().siguienteOpcionGeneracionClientes(porcentajes)).getValor();
        return Aleatorio.getInstance().siguienteEntero((int)rangoEdad.getX(), (int)rangoEdad.getY());
    }

    private int[] generarOcupacionesAleatoria() {
        ArrayList<RegistroAtributoMercado> registros=ocupacion.getRegistrosAtributoMercado();
        double [] porcentajes=new double[registros.size()];
        int j=0;
        for(int i=0;i<registros.size();i++){
            porcentajes[i]=registros.get(i).getPorcentajeMercado();
            if(((String)registros.get(i).getValor()).equalsIgnoreCase("empleado")){
                j++;
            }
            if(((String)registros.get(i).getValor()).equalsIgnoreCase("desempleado")){
                j++;
            }
            if(((String)registros.get(i).getValor()).equalsIgnoreCase("independiente")){
                j++;
            }
        }
        int cantidadOcupaciones=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(1, registros.size());
        if(cantidadOcupaciones>=2&&j>1){
            cantidadOcupaciones=cantidadOcupaciones-(j-1);
        }
        int[] ocupacionesAleatorias=new int[cantidadOcupaciones];
        for(int i=0;i<ocupacionesAleatorias.length;i++){
            ocupacionesAleatorias[i]=-1;
        }
        ArrayList<String> ocupacionesSeleccionadas=new ArrayList();
        for(int i=0;i<cantidadOcupaciones;i++){
            String ocupacionAleatoria=(String)registros.get(Aleatorio.getInstance().siguienteOpcionGeneracionClientes(porcentajes)).getValor();
            if(!ocupacionesSeleccionadas.contains(ocupacionAleatoria)){
                ocupacionesSeleccionadas.add(ocupacionAleatoria);
                if(ocupacionAleatoria.equalsIgnoreCase("desempleado")){
                    for(int ocupacionSeleccionada:ocupacionesAleatorias){
                        if(ocupacionSeleccionada==DatosDemograficos.OCUPACION_EMPLEADO||ocupacionSeleccionada==DatosDemograficos.OCUPACION_INDEPENDIENTE){
                            i--;
                            break;
                        }
                    }
                    ocupacionesAleatorias[i]=DatosDemograficos.OCUPACION_DESEMPLEADO;
                }
                else{
                    if(ocupacionAleatoria.equalsIgnoreCase("empleado")){
                        for(int ocupacionSeleccionada:ocupacionesAleatorias){
                            if(ocupacionSeleccionada==DatosDemograficos.OCUPACION_DESEMPLEADO||ocupacionSeleccionada==DatosDemograficos.OCUPACION_INDEPENDIENTE){
                                i--;
                                break;
                            }
                        }
                        ocupacionesAleatorias[i]=DatosDemograficos.OCUPACION_EMPLEADO;
                    }
                    else{
                        if(ocupacionAleatoria.equalsIgnoreCase("estudiante")){
                            ocupacionesAleatorias[i]=DatosDemograficos.OCUPACION_ESTUDIANTE;
                        }
                        else{
                            if(ocupacionAleatoria.equalsIgnoreCase("hogar")){
                                ocupacionesAleatorias[i]=DatosDemograficos.OCUPACION_HOGAR;
                            }
                            else{
                                if(ocupacionAleatoria.equalsIgnoreCase("independiente")){
                                    for(int ocupacionSeleccionada:ocupacionesAleatorias){
                                        if(ocupacionSeleccionada==DatosDemograficos.OCUPACION_DESEMPLEADO||ocupacionSeleccionada==DatosDemograficos.OCUPACION_EMPLEADO){
                                            i--;
                                            break;
                                        }
                                    }
                                    ocupacionesAleatorias[i]=DatosDemograficos.OCUPACION_INDEPENDIENTE;
                                }
                            }
                        }
                    }
                }
            }else{
                i--;
            }
        }
        return ocupacionesAleatorias;
    }

    private int generarNivelSocioeconomicoAleatorio() {
        ArrayList<RegistroAtributoMercado> registros=nivelSocioeconomico.getRegistrosAtributoMercado();
        double [] porcentajes=new double[registros.size()];
        for(int i=0;i<registros.size();i++){
            porcentajes[i]=registros.get(i).getPorcentajeMercado();
        }
        String valorNivelSocioeconomico=(String)registros.get(Aleatorio.getInstance().siguienteOpcionGeneracionClientes(porcentajes)).getValor();
        int nivelSocioeconomicoAleatorio=-1;
        if(valorNivelSocioeconomico.equalsIgnoreCase("muy_bajo")){
            nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_BAJO;
        }
        else{
            if(valorNivelSocioeconomico.equalsIgnoreCase("bajo")){
                nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO;
            }
            else{
                if(valorNivelSocioeconomico.equalsIgnoreCase("bajo_medio")){
                    nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO_MEDIO;
                }
                else{
                    if(valorNivelSocioeconomico.equalsIgnoreCase("medio")){
                        nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO;
                    }
                    else{
                        if(valorNivelSocioeconomico.equalsIgnoreCase("medio_alto")){
                            nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO_ALTO;
                        }
                        else{
                            if(valorNivelSocioeconomico.equalsIgnoreCase("alto")){
                                nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_ALTO;
                            }
                            else{
                                if(valorNivelSocioeconomico.equalsIgnoreCase("muy_alto")){
                                    nivelSocioeconomicoAleatorio=DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_ALTO;
                                }
                            }
                        }
                    }
                }
            }
        }
        return nivelSocioeconomicoAleatorio;
    }

    private int generarEscolaridadAleatoria() {
        ArrayList<RegistroAtributoMercado> registros=escolaridad.getRegistrosAtributoMercado();
        double [] porcentajes=new double[registros.size()];
        for(int i=0;i<registros.size();i++){
            porcentajes[i]=registros.get(i).getPorcentajeMercado();
        }
        String valorEscolaridad=(String)registros.get(Aleatorio.getInstance().siguienteOpcionGeneracionClientes(porcentajes)).getValor();
        int escolaridadAleatoria=-1;
        if(valorEscolaridad.equalsIgnoreCase("bachiller")){
            escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_BACHILLER;
        }
        else{
            if(valorEscolaridad.equalsIgnoreCase("doctorado")){
                escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_DOCTORADO;
            }
            else{
                if(valorEscolaridad.equalsIgnoreCase("maestria")){
                    escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_MAERSTRIA;
                }
                else{
                    if(valorEscolaridad.equalsIgnoreCase("ninguna")){
                        escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_NINGUNA;
                    }
                    else{
                        if(valorEscolaridad.equalsIgnoreCase("postgrado")){
                            escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_POSTGRADO;
                        }
                        else{
                            if(valorEscolaridad.equalsIgnoreCase("primaria")){
                                escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_PRIMARIA;
                            }
                            else{
                                if(valorEscolaridad.equalsIgnoreCase("profesional")){
                                    escolaridadAleatoria=DatosDemograficos.ESCOLARIDAD_PROFESIONAL;
                                }
                            }
                        }
                    }
                }
            }
        }
        return escolaridadAleatoria;
    }
}
