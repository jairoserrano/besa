/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugar.agente;

import BESA.Kernel.Agent.StateBESA;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.MensajePromocional;
import java.util.ArrayList;
import SIMALL.modelo.util.DistribucionProbabilidadTriangular;

/**
 *
 * @author dsvalencia
 */
public abstract class LugarState extends StateBESA {
    private String alias;
    private ArrayList<String> clientes;
    private ArrayList<String> objetos;
    private String nicho;
    
    private int transito=0;
    
    private int x;
    private int y;
    private double angulo;
    
    private int unidades;

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    

    public String getNicho() {
        return nicho;
    }

    public void setNicho(String nicho) {
        this.nicho = nicho;
    }
    
    public static final int ENTRADA=0;
    public static final int PASILLO=1;
    public static final int SANITARIO=2;
    public static final int COMERCIO=3;
    public static final int PUNTO_DE_INFORMACION=4;
    public static final int MURO=5;


    public ArrayList<String> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<String> clientes) {
        this.clientes = clientes;
    }

    public ArrayList<String> getObjetos() {
        return objetos;
    }

    public void setObjetos(ArrayList<String> objetos) {
        this.objetos = objetos;
    }

    public DistribucionProbabilidadTriangular getTiempoServicio() {
        return tiempoServicio;
    }

    public void setTiempoServicio(DistribucionProbabilidadTriangular tiempoServicio) {
        this.tiempoServicio = tiempoServicio;
    }

    public DistribucionProbabilidadTriangular getValorProductos() {
        return valorProductos;
    }

    public void setValorProductos(DistribucionProbabilidadTriangular valorProductos) {
        this.valorProductos = valorProductos;
    }

    public DistribucionProbabilidadTriangular getVentas() {
        return ventas;
    }

    public void setVentas(DistribucionProbabilidadTriangular ventas) {
        this.ventas = ventas;
    }
    
    private DistribucionProbabilidadTriangular tiempoServicio;
    private DistribucionProbabilidadTriangular valorProductos;
    private DistribucionProbabilidadTriangular ventas;
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public LugarState(String alias, String tipo, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades){
        this.nicho=tipo;
        this.tiempoServicio=tiempoServicio;
        this.valorProductos=valorProductos;
        this.ventas=ventas;
        this.alias=alias;
        this.angulo=0;
        this.clientes=new ArrayList();
        this.objetos=new ArrayList();
        this.x=x;
        this.y=y;
        this.unidades=unidades;
    }
    
    public boolean agregarCliente(String clienteAlias){
        if(!clientes.contains(clienteAlias)){
            if(clientes.add(clienteAlias)){
                transito++;
                return true;
            }
        }
        return false;
    }
    
    public int getTransito(){
        return transito;
    }
    
    
    public boolean agregarObjeto(String objetoAlias){
        if(!clientes.contains(objetoAlias)){
            return clientes.add(objetoAlias);
        }
        return false;
    }

    public void eliminarCliente(String nombreAgente) {
        clientes.remove(nombreAgente);
    }
    
    @Override
    public String toString(){
        return alias;
    }
    
    public void setAngulo(double angulo) {
        this.angulo=angulo;
    }
    
    public double getAngulo() {
        return angulo;
    }

    public ArrayList<MensajePromocional> getMensajesPromocionales() {
        return null;
    }
    
}
