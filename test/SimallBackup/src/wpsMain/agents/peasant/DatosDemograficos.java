/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.ApetitoEndeudamiento;
import wpsMain.agents.peasant.NivelEconomico;
import wpsMain.agents.peasant.PresupuestoGasto;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import wpsMain.util.Aleatorio;

/**
 *
 * @author dsvalencia
 */
public class DatosDemograficos {
    
    public static final int NIVEL_SOCIOECONOMICO_MUY_BAJO=0;
    public static final int NIVEL_SOCIOECONOMICO_BAJO=1;
    public static final int NIVEL_SOCIOECONOMICO_BAJO_MEDIO=2;
    public static final int NIVEL_SOCIOECONOMICO_MEDIO=3;
    public static final int NIVEL_SOCIOECONOMICO_MEDIO_ALTO=4;
    public static final int NIVEL_SOCIOECONOMICO_ALTO=5;
    public static final int NIVEL_SOCIOECONOMICO_MUY_ALTO=6;
    
    public static final int GENERO_MASCULINO=0;
    public static final int GENERO_FEMENINO=1;
    public static final int GENERO_LGBT=2;
    
    public static final int OCUPACION_HOGAR=0;
    public static final int OCUPACION_ESTUDIANTE=1;
    public static final int OCUPACION_EMPLEADO=2;
    public static final int OCUPACION_DESEMPLEADO=3;
    public static final int OCUPACION_INDEPENDIENTE=4;
    
    public static final int ESCOLARIDAD_NINGUNA=0;
    public static final int ESCOLARIDAD_PRIMARIA=1;
    public static final int ESCOLARIDAD_BACHILLER=2;
    public static final int ESCOLARIDAD_PROFESIONAL=3;
    public static final int ESCOLARIDAD_POSTGRADO=4;
    public static final int ESCOLARIDAD_MAERSTRIA=5;
    public static final int ESCOLARIDAD_DOCTORADO=6;

    private String nombre;
    private int genero;
    private int edad;
    private int ocupaciones[];
    private int nivelSocioeconomico;
    private int escolaridad;
    private double porcentajeNecesidadSanitario; //0 mínnimo, 1 máximo

    DatosDemograficos(DatosDemograficos datosDemograficos) {
        this.nombre = datosDemograficos.nombre;
        this.genero = datosDemograficos.genero;
        this.edad = datosDemograficos.edad;
        this.ocupaciones = datosDemograficos.ocupaciones;
        this.escolaridad = datosDemograficos.escolaridad;
        this.nivelSocioeconomico = datosDemograficos.nivelSocioeconomico;
        this.porcentajeNecesidadSanitario = datosDemograficos.porcentajeNecesidadSanitario;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int[] getOcupaciones() {
        return ocupaciones;
    }

    public void setOcupaciones(int[] ocupaciones) {
        this.ocupaciones = ocupaciones;
    }

    public int getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(int escolarodad) {
        this.escolaridad = escolarodad;
    }

    public double getPorcentajeNecesidadSanitario() {
        return porcentajeNecesidadSanitario;
    }

    public void setPorcentajeNecesidadSanitario(double porcentajeNecesidadSanitario) {
        this.porcentajeNecesidadSanitario = porcentajeNecesidadSanitario;
    }
    
    
    public DatosDemograficos(String nombre, int genero, int edad, int[] ocupaciones, int escolaridad, int nivelSocioeconomico, double porcentajeNecesidadSanitario) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.ocupaciones = ocupaciones;
        this.escolaridad = escolaridad;
        this.nivelSocioeconomico = nivelSocioeconomico;
        this.porcentajeNecesidadSanitario = porcentajeNecesidadSanitario;
    }
    
    @Override
    public String toString(){
        String cadena="Datos Demográficos: \n\n";
        cadena+="\tNombre: "+nombre+"\n";
        cadena+="\tGenero: ";
        switch(genero){
            case GENERO_MASCULINO:  cadena+="MASCULINO";
                                    break;
            case GENERO_FEMENINO:   cadena+="FEMENINO";
                                    break;
            case GENERO_LGBT:       cadena+="LGBT";
                                    break;
            default:    cadena+="NINGUNO";
                        break;
        }
        cadena+="\n";
        cadena+="\tEdad: "+edad+" años\n";
        cadena+="\tNivel Socioeconómico: ";
        switch(nivelSocioeconomico){
            case NIVEL_SOCIOECONOMICO_MUY_BAJO:     cadena+="MUY BAJO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_BAJO:         cadena+="BAJO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_BAJO_MEDIO:   cadena+="BAJO - MEDIO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_MEDIO:         cadena+="MEDIO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_MEDIO_ALTO:   cadena+="MEDIO - ALTO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_ALTO:         cadena+="ALTO";
                                                    break;
            case NIVEL_SOCIOECONOMICO_MUY_ALTO:     cadena+="MUY ALTO";
                                                    break;
            default:    cadena+="NINGUNO";
                        break;
        }
        cadena+="\n";
        
        if(ocupaciones.length>0){
            for(int i=0;i<ocupaciones.length;i++){
                cadena+="\tOcupación: ";
                switch(ocupaciones[i]){
                    case OCUPACION_HOGAR:           cadena+="HOGAR";
                                                    break;
                    case OCUPACION_ESTUDIANTE:      cadena+="ESTUDIANTE";
                                                    break;
                    case OCUPACION_EMPLEADO:        cadena+="EMPLEADO";
                                                    break;
                    case OCUPACION_DESEMPLEADO:     cadena+="DESEMPLEADO";
                                                    break;
                    case OCUPACION_INDEPENDIENTE:   cadena+="INDEPENDIENTE";
                                                    break;
                    default:                        cadena+="NINGUNO";
                                                    break;
                }
                cadena+="\n";
            }
        }
        else{
            cadena+="\tOcupación: NINGUNA\n";
        }
        return cadena;
    }

    public DatosEconomicos generarDatosEconomicosAleatorios(DatosDemograficos datosDemograficos) {
        Aleatorio aleatorio=Aleatorio.getInstance();
        NivelEconomico nivelEconomico=MapaEstructural.getInstance().getDemografia().getNivelEconomico();
        ApetitoEndeudamiento apetitoEndeudamiento=MapaEstructural.getInstance().getDemografia().getApetitoEndeudamiento();
        PresupuestoGasto presupuestoGasto=MapaEstructural.getInstance().getDemografia().getPresupuestoGasto();
        double totalDinero=aleatorio.siguienteDobleGeneracionClientes(nivelEconomico.obtenerRangoEconomico(datosDemograficos.nivelSocioeconomico).getX(), nivelEconomico.obtenerRangoEconomico(datosDemograficos.nivelSocioeconomico).getY());
        double dineroPresupuestadoParaGasto=aleatorio.siguienteDobleGeneracionClientes(presupuestoGasto.obtenerRangoPresupuestoGasto(nivelSocioeconomico).getX(), presupuestoGasto.obtenerRangoPresupuestoGasto(nivelSocioeconomico).getY())*totalDinero;
        double dineroDisponibleEfectivo=aleatorio.siguienteDobleGeneracionClientes(0,totalDinero);
        double dineroDisponibleCredito=aleatorio.siguienteDobleGeneracionClientes(0,totalDinero-dineroDisponibleEfectivo);
        double dineroDisponibleCuenta=totalDinero-dineroDisponibleEfectivo-dineroDisponibleCredito;
        double porcentajeApetitoEndeudamiento=aleatorio.siguienteDobleGeneracionClientes(apetitoEndeudamiento.obtenerRangoApetitoEndeudamiento(nivelSocioeconomico).getX(),apetitoEndeudamiento.obtenerRangoApetitoEndeudamiento(nivelSocioeconomico).getY());
        return new DatosEconomicos(dineroPresupuestadoParaGasto,dineroDisponibleEfectivo, dineroDisponibleCredito, dineroDisponibleCuenta, porcentajeApetitoEndeudamiento);
    }
}
