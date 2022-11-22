/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SIMALL.modelo.log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author dsvalencia
 */
public final class LogAuditoria{
    private static final Hashtable hashColasDeEventos=new Hashtable();
    private static final Hashtable hashColasDeErrores=new Hashtable();
    private static final Hashtable hashDeHelpers=new Hashtable();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
    private static final LogAuditoria logAuditoria=new LogAuditoria();
    
    private LogAuditoria(){
        borrarArchivosDeLog();
        crearLog(this.getClass().getName(),"LogEventos");
        crearLog(LogAuditoriaHelper.class.getName(),"LogEventos");
    }
    
    public static LogAuditoria getInstance(){
        return logAuditoria;
    }
    
    public void escribirEvento(String className, String id, String mensaje){
        if(!hashColasDeEventos.containsKey(className+id)){
            crearLog(className,id);
        }
        ((Cola)hashColasDeEventos.get(className+id)).push(" Fecha:"+dateFormat.format(new Date())+" Id:"+id+" Mensaje:"+mensaje);
        ((LogAuditoriaHelper)hashDeHelpers.get(className+id)).notificar();
    }
    
    public void escribirMensaje(String className, String id, String mensaje){
        if(!hashColasDeEventos.containsKey(className+id)){
            crearLog(className,id);
        }
        ((Cola)hashColasDeEventos.get(className+id)).push(mensaje);
        ((LogAuditoriaHelper)hashDeHelpers.get(className+id)).notificar();
    }
    
    public void escribirError(String className, String id, Exception e){
        if(!hashColasDeErrores.containsKey(className+id)){
            crearLog(className,id);
        }
        String stackTrace="";
        for(StackTraceElement st:e.getStackTrace()){
            stackTrace+=st.toString()+"\n";
        }
        ((Cola)hashColasDeErrores.get(className+id)).push("Fecha:"+dateFormat.format(new Date())+" ClassName:"+className+" Id:"+id+" Mensaje:"+e.getMessage()+" StackTrace:"+stackTrace);
        ((LogAuditoriaHelper)hashDeHelpers.get(className+id)).notificar();
        System.err.println("Se produjo un error en "+className+" "+id+" Mensaje:"+e.getMessage()+" StackTrace:"+stackTrace);
    }
    
    public boolean crearLog(String className, String id){
        if(!this.hashColasDeEventos.containsKey(className+id)){
            LogAuditoriaHelper logAuditoriaHelper=new LogAuditoriaHelper(className,id);
            if(logAuditoriaHelper.iniciar()){
                hashColasDeEventos.put(className+id,new Cola());
                hashColasDeErrores.put(className+id,new Cola());
                hashDeHelpers.put(className+id,logAuditoriaHelper);
                return true;
            }
        }
        return false;
    }
    
    public void borrarArchivosDeLog(){
        try{
            File carpeta=new File("log");
            carpeta.mkdir();
            carpeta.getCanonicalPath();
            String archivos[]=carpeta.list();
            for (String archivo : archivos) {
                if (archivo.endsWith(".log")) {
                    File archivoBorrable = new File("./log/"+archivo);
                    archivoBorrable.delete();
                }
            }
        }
        catch(IOException e){
            escribirError(this.getClass().getName(),"LogEventos",e);
        }
    }
    
    public Cola buscarEventos(String className, String id){
        if(hashColasDeEventos.containsKey(className+id)){
            return (Cola)hashColasDeEventos.get(className+id);
        }
        return null;
    }
    
    public Cola buscarErrores(String className, String id){
        if(hashColasDeErrores.containsKey(className+id)){
            return (Cola)hashColasDeErrores.get(className+id);
        }
        return null;
    }

    public void escribirAuditoria() {
        Enumeration enumerador=hashDeHelpers.elements();
        while(enumerador.hasMoreElements()){
            ((LogAuditoriaHelper)enumerador.nextElement()).notificar();
        }
    }
}
