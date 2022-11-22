/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SIMALL.modelo.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author dsvalencia
 */
public class LogAuditoriaHelper implements Runnable {

    private FileOutputStream flujoDeSalidaEventos;
    private FileOutputStream flujoDeSalidaErrores;
    private Thread hilo;
    private String className;
    private String id;
    private boolean activo;
    private int contadorEventos=0;
    private int contadorErrores=0;
    
    
    public String getClassName() {
        return className;
    }

    public void setClassName(String clasName) {
        this.className = clasName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    public LogAuditoriaHelper(String className, String id) {
        this.className=className;
        this.id=id;
        activo=true;
    }
        
    
    public boolean iniciar(){
        try{
            
            File archivoEventos = new File("./log/Eventos-"+className+"-"+id+".log");
            if(!archivoEventos.exists()) {
                archivoEventos.createNewFile();
            }
            File archivoErrores = new File("./log/Errores-"+className+"-"+id+".log");
            if(!archivoErrores.exists()) {
                archivoErrores.createNewFile();
            }
            
            flujoDeSalidaEventos=new FileOutputStream(archivoEventos);
            flujoDeSalidaErrores=new FileOutputStream(archivoErrores);
            String registroEvento="Inicio de Archivo Log de Eventos "+className+"-"+id+".log\r\n";
            String registroError="Inicio de Archivo Log de Errores "+className+"-"+id+".log\r\n";
            flujoDeSalidaEventos.write(registroEvento.getBytes());
            flujoDeSalidaErrores.write(registroError.getBytes());
            
            hilo=new Thread(this);
            hilo.start();           
        }catch(FileNotFoundException e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),id,e);
            return false;
        }
        catch(IOException e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),id,e);
            return false;
        }
        return true;
    }
    
    @Override
    public void run() {
        synchronized(this){
            try{
                while(activo){
                    escribirMensajes();
                    this.wait();
                }
            }catch(InterruptedException e){
                LogAuditoria.getInstance().escribirError(this.getClass().getName(),"LogEventos",e);
            }
        }
    }
        
    private void escribirMensajes(){
        
        Cola eventosPendientes=LogAuditoria.getInstance().buscarEventos(className,id);
        if(eventosPendientes!=null){
            while(!eventosPendientes.estaVacia()){
                try{
                    String registroEvento=contadorEventos+(String)eventosPendientes.get()+"\r\n";
                    flujoDeSalidaEventos.write(registroEvento.getBytes());
                    contadorEventos++;
                }catch(IOException e){
                    LogAuditoria.getInstance().escribirError(this.getClass().getName(),"LogEventos",e);
                }
                
            }
        }
        Cola erroresPendientes=LogAuditoria.getInstance().buscarErrores(className,id);
        if(erroresPendientes!=null){
            while(!erroresPendientes.estaVacia()){
                try{
                    String registroError=contadorErrores+(String)erroresPendientes.get()+"\r\n";
                    flujoDeSalidaErrores.write(registroError.getBytes());
                    contadorErrores++;
                }catch(IOException e){
                    LogAuditoria.getInstance().escribirError(this.getClass().getName(),"LogEventos",e);
                }
            }
        }   
        
    }
    
    public void detener(){
        activo=false;
    }

    void notificar() {
        synchronized(this){
            this.notify();
        }
    }
    
    
}
