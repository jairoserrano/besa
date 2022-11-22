/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import SIMALL.modelo.cc.infraestructura.ConfLugar;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class ConfigComercio extends ConfLugar{
    
    public static final int TIPO_NORMAL=0;
    public static final int TIPO_ANCLA=1;
    
    public static final int NIVEL_COSTO_MUY_BAJO=0;
    public static final int NIVEL_COSTO_BAJO=1;
    public static final int NIVEL_COSTO_BAJO_MEDIO=2;
    public static final int NIVEL_COSTO_MEDIO=3;
    public static final int NIVEL_COSTO_MEDIO_ALTO=4;
    public static final int NIVEL_COSTO_ALTO=5;
    public static final int NIVEL_COSTO_MUY_ALTO=6;
    
    
    private MercadoObjetivo mercadoObjetivo;
    private Inventario inventario;
    private boolean inventarioAutogenerado;
    private double porcentajeAbastecimiento;
    private double porcentajeDiversidad;
    private int nivelCosto;
    
    private int tipo;
    private String nombre;
    private String nicho;
    private ArrayList<String> categoriasRegistradas;
    private int unidades;
    private int vitrinas;
    private int locales;
    private int minItems;
    private int avgItems;
    private int maxItems;
    private int minValor;
    private int avgValor;
    private int maxValor;
    private int minTiempo;
    private int avgTiempo;
    private int maxTiempo;
    private int minVentas;
    private int avgVentas;
    private int maxVentas;
    private int x;
    private int y;
    private int impulsadores;

    public ConfigComercio(int tipo, String nombre, String nicho, ArrayList<String> categoriasRegistradas, MercadoObjetivo mercadoObjetivo, Inventario inventario, int unidades, int vitrinas, int locales, int minItems, int avgItems, int maxItems, int minValor, int avgValor, int maxValor, int minTiempo, int avgTiempo, int maxTiempo, int minVentas, int avgVentas, int maxVentas, int x, int y, boolean inventarioAutogenerado, double porcentajeAbastecimiento, double porcentajeDiversidad, int nivelCosto, int impulsadores) {
        super(nombre, nicho, unidades, locales, x, y, false);
        this.tipo=tipo;
        this.nombre = nombre;
        this.nicho=nicho;
        this.categoriasRegistradas=categoriasRegistradas;
        this.mercadoObjetivo=mercadoObjetivo;
        this.inventario=inventario;
        this.unidades = unidades;
        this.vitrinas = vitrinas;
        this.locales = locales;
        this.minItems = minItems;
        this.avgItems = avgItems;
        this.maxItems = maxItems;
        this.minValor = minValor;
        this.avgValor = avgValor;
        this.maxValor = maxValor;
        this.minTiempo = minTiempo;
        this.avgTiempo = avgTiempo;
        this.maxTiempo = maxTiempo;
        this.minVentas = minVentas;
        this.avgVentas = avgVentas;
        this.maxVentas = maxVentas;
        this.x=x;
        this.y=y;
        this.inventarioAutogenerado=inventarioAutogenerado;
        this.porcentajeAbastecimiento=porcentajeAbastecimiento;
        this.porcentajeDiversidad=porcentajeDiversidad;
        this.nivelCosto=nivelCosto;
        this.impulsadores=impulsadores;
    }
    
    public ConfigComercio(ConfigComercio configComercio) {
        super(configComercio.nombre, configComercio.nicho, configComercio.unidades, configComercio.locales, configComercio.x, configComercio.y, false);
        this.tipo=configComercio.tipo;
        this.nombre = configComercio.nombre;
        this.nicho = configComercio.nicho;
        this.categoriasRegistradas = configComercio.categoriasRegistradas;
        this.mercadoObjetivo = configComercio.mercadoObjetivo;
        this.inventario = configComercio.inventario;
        this.unidades = configComercio.unidades;
        this.vitrinas = configComercio.vitrinas;
        this.locales = configComercio.locales;
        this.minItems = configComercio.minItems;
        this.avgItems = configComercio.avgItems;
        this.maxItems = configComercio.maxItems;
        this.minValor = configComercio.minValor;
        this.avgValor = configComercio.avgValor;
        this.maxValor = configComercio.maxValor;
        this.minTiempo = configComercio.minTiempo;
        this.avgTiempo = configComercio.avgTiempo;
        this.maxTiempo = configComercio.maxTiempo;
        this.minVentas = configComercio.minVentas;
        this.avgVentas = configComercio.avgVentas;
        this.maxVentas = configComercio.maxVentas;
        this.x=configComercio.x;
        this.y=configComercio.y;
        this.inventarioAutogenerado=configComercio.inventarioAutogenerado;
        this.porcentajeAbastecimiento=configComercio.porcentajeAbastecimiento;
        this.porcentajeDiversidad=configComercio.porcentajeDiversidad;
        this.nivelCosto=configComercio.nivelCosto;
        this.impulsadores=configComercio.impulsadores;
    }
    
    public int getImpulsadores() {
        return impulsadores;
    }

    public void setImpulsadores(int impulsadores) {
        this.impulsadores = impulsadores;
    }
    
    
    public String getNicho() {
        return nicho;
    }

    public void setNicho(String nicho) {
        this.nicho = nicho;
    }

    public ArrayList<String> getCategoriasRegistradas() {
        return categoriasRegistradas;
    }

    public void setCategoriasRegistradas(ArrayList<String> categoriasRegistradas) {
        this.categoriasRegistradas = categoriasRegistradas;
    }
    
    public MercadoObjetivo getMercadoObjetivo() {
        return mercadoObjetivo;
    }

    public void setMercadoObjetivo(MercadoObjetivo mercadoObjetivo) {
        this.mercadoObjetivo = mercadoObjetivo;
    }
    
    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getVitrinas() {
        return vitrinas;
    }

    public void setVitrinas(int vitrinas) {
        this.vitrinas = vitrinas;
    }

    public int getLocales() {
        return locales;
    }

    public void setLocales(int locales) {
        this.locales = locales;
    }

    public int getMinItems() {
        return minItems;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }
    
    public int getAvgItems() {
        return avgItems;
    }

    public void setAvgItems(int avgItems) {
        this.avgItems = avgItems;
    }
    
    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public int getMinValor() {
        return minValor;
    }

    public void setMinValor(int minValor) {
        this.minValor = minValor;
    }

    public int getAvgValor() {
        return avgValor;
    }

    public void setAvgValor(int avgValor) {
        this.avgValor = avgValor;
    }

    public int getMaxValor() {
        return maxValor;
    }

    public void setMaxValor(int maxValor) {
        this.maxValor = maxValor;
    }

    public int getMinTiempo() {
        return minTiempo;
    }

    public void setMinTiempo(int minTiempo) {
        this.minTiempo = minTiempo;
    }

    public int getAvgTiempo() {
        return avgTiempo;
    }

    public void setAvgTiempo(int avgTiempo) {
        this.avgTiempo = avgTiempo;
    }

    public int getMaxTiempo() {
        return maxTiempo;
    }

    public void setMaxTiempo(int maxTiempo) {
        this.maxTiempo = maxTiempo;
    }

    public int getMinVentas() {
        return minVentas;
    }

    public void setMinVentas(int minVentas) {
        this.minVentas = minVentas;
    }

    public int getAvgVentas() {
        return avgVentas;
    }

    public void setAvgVentas(int avgVentas) {
        this.avgVentas = avgVentas;
    }

    public int getMaxVentas() {
        return maxVentas;
    }

    public void setMaxVentas(int maxVentas) {
        this.maxVentas = maxVentas;
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

    public boolean isInventarioAutogenerado() {
        return inventarioAutogenerado;
    }

    public void setInventarioAutogenerado(boolean inventarioAutogenerado) {
        this.inventarioAutogenerado = inventarioAutogenerado;
    }

    public double getPorcentajeAbastecimiento() {
        return porcentajeAbastecimiento;
    }

    public void setPorcentajeAbastecimiento(double porcentajeAbastecimiento) {
        this.porcentajeAbastecimiento = porcentajeAbastecimiento;
    }

    public double getPorcentajeDiversidad() {
        return porcentajeDiversidad;
    }

    public void setPorcentajeDiversidad(double porcentajeDiversidad) {
        this.porcentajeDiversidad = porcentajeDiversidad;
    }

    public int getNivelCosto() {
        return nivelCosto;
    }

    public void setNivelCosto(int nivelCosto) {
        this.nivelCosto = nivelCosto;
    }
    
    
    
    
    
    @Override
    public String toString(){
        String cadena="Comercio: "+nombre;
        if(!categoriasRegistradas.equals("")){
            cadena+=" Categoria: "+categoriasRegistradas;
        }
        else{
            cadena+=" Categoria: [Sin Categoria]";
        }
        cadena+=" U="+unidades+" V="+vitrinas+" L="+locales+" minI="+minItems+" promI="+avgItems+" maxI="+maxItems+" minV="+minValor+" avgV="+avgValor+" maxT="+maxTiempo+" minT="+minTiempo+" avgT="+avgTiempo+" maxT="+maxTiempo+" minS="+minVentas+" avgS="+avgVentas+" maxS="+maxVentas+"\n";
        cadena+=mercadoObjetivo;
        cadena+=inventario;
        return cadena;
    }
}
