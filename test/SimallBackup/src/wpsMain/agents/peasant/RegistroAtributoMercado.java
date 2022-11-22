/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.util.Punto;

/**
 *
 * @author dsvalencia
 */
public class RegistroAtributoMercado {
    public static final int TIPO_EDAD=0;
    public static final int TIPO_GENERO=1;
    public static final int TIPO_NIVEL_SOCIOECONOMICO=2;
    public static final int TIPO_ESCOLARIDAD=3;
    public static final int TIPO_OCUPACION=4;
    
    private double porcentajeMercado;
    private int tipo;
    private Object valor;

    public RegistroAtributoMercado(double porcentajeMercado, int tipo, Object valor) {
        this.porcentajeMercado = porcentajeMercado;
        this.tipo = tipo;
        this.valor = valor;
    }

    public double getPorcentajeMercado() {
        return porcentajeMercado;
    }

    public void setPorcentajeMercado(double porcentajeMercado) {
        this.porcentajeMercado = porcentajeMercado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public boolean evaluarCompatibilidad(Object valor) {
        try{
            switch(tipo){
                case TIPO_EDAD: if((Integer)valor>=((Punto)this.valor).getX()&&(Integer)valor<=((Punto)this.valor).getY()){
                                    return true;
                                }
                                break;
                case TIPO_GENERO:   if(((Integer)valor).equals((Integer)this.valor)){
                                        return true;
                                    }
                                    break;
                case TIPO_NIVEL_SOCIOECONOMICO: if(((Integer)valor).equals((Integer)this.valor)){
                                                    return true;
                                                }
                                                break;
                case TIPO_ESCOLARIDAD:  if(((Integer)valor).equals((Integer)this.valor)){
                                            return true;
                                        }
                                        break;
                case TIPO_OCUPACION:    if(((Integer)valor).equals((Integer)this.valor)){
                                            return true;
                                        }
                                        break;
            }
        }catch(Exception e){
            System.err.println("Se presentó un error al evaluar la compatibilidad "+e.getMessage());
            for(StackTraceElement st:e.getStackTrace()){
                System.err.println(st);
            }
            System.exit(0);
        }
        return false;
    }
}
