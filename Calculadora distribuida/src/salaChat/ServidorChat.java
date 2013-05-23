/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package salachat;

/**
 *
 * @author Christian Botero
 */
// Configurar un servidor que reciba una conexión de un cliente, envíe
// una cadena al cliente y cierre la conexión.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ServidorChat {
 
    /**
     * @param args
     */
 
    private static ArrayList<Socket> listadoClientes = new ArrayList<Socket>();
    
    private static HashMap listaSalas = new HashMap(); 
 
      //--- LISTA DE USUARIOS CONECTADOS ---
    private static ArrayList<String> listadoConectados = new ArrayList<String>();
 
    private static String cadena_usuariosConectados = "";
 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
 
        /*
            Argumento: Puerto del Servidor.
        */
        System.out.print("ingrese el puerto:");
        String puerto = readLine();
        
        try {
 
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(puerto));
            System.out.println("Servidor abierto por el puerto : " + puerto);
            while(true){
 
                Socket cliente = serverSocket.accept();
                new ChatThread(cliente).start();
 
                listadoClientes.add(cliente);
 
            }
 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
    public static synchronized void  enviarMensage(String mensaje, Socket cliente){
        try {
            PrintWriter p;
            p = new PrintWriter(cliente.getOutputStream(),true);
            p.println(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static synchronized void comunicar_a_todos(String mensaje, String nick, int tipo){
 
        //El servidor comunica a todos los clientes conectados, qué nicks van llegando
        //y que nicks se van yendo, así como los mensajes que escribe cada uno.
 
        Socket s = null;
        PrintWriter p;
        
 
        Iterator<Socket> itr = listadoClientes.iterator();
 
        if(tipo == 1){
            listadoConectados.add(nick);
            cadena_lista_conectados();
        }
 
        if(tipo == 2){
            listadoConectados.remove(nick);
            cadena_lista_conectados();
        }
 
        while(itr.hasNext()) {
            s = itr.next();
            try {
                p = new PrintWriter(s.getOutputStream(),true);
                if(tipo == 1){ //Se conecta un nuevo cliente
                    p.println(mensaje);
                    //Añadir a la lista de usuarios conectados.
                    p.println("CONNECT:" + cadena_usuariosConectados);
                    //------------------------------------------------
                }else if(tipo == 2){ //Se desconecta un cliente
                    p.println(mensaje);
                    //Mensaje a sock para Quitar de la lista de usuarios conectados.
                    p.println("DISCONNECT:" + cadena_usuariosConectados);
                    //------------------------------------------------
                }else{// Intercambio de mensajes
                    p.println(nick + ">" + mensaje);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 
        }
        cadena_usuariosConectados = "";
    }
 
    public static boolean setNick(String nick){
        if(listadoConectados!=null){
            for (int i = 0; i < listadoConectados.size(); i++) {
                if(listadoConectados.get(i).equals(nick)){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
        
    }
    public static void listarConectados(Socket cliente){
        //Transforma el ArrayList de usuarios conectados a cadena para enviar por el socket.
        String cadena_usuariosConectados="";
        for(String s:listadoConectados){
            cadena_usuariosConectados += s + "::";
        }
        System.out.println(cadena_usuariosConectados);
        enviarMensage(cadena_usuariosConectados, cliente);
    }
    
     private static void cadena_lista_conectados(){
        //Transforma el ArrayList de usuarios conectados a cadena para enviar por el socket.
 
        for(String s:listadoConectados){
            cadena_usuariosConectados += s + "::";
        }
        System.out.println(cadena_usuariosConectados);
        //enviarMensage(cadena_usuariosConectados, cliente);
    }
 
    public static synchronized void eliminar_socket_cliente(Socket cliente , String sala){
 
        Iterator<Socket> itr = listadoClientes.iterator();
 
        while(itr.hasNext()){
 
            Socket soc = itr.next();
 
            if(soc.equals(cliente)){
                itr.remove();
                break;
            }
        }
        ((ArrayList<Socket>)listaSalas.get(sala)).remove(cliente);
    }

    static void comunicar_a_sala(String mensaje, String nick, String sala, int i) {
        //throw new UnsupportedOperationException("Not yet implemented");
        if(sala!=null){
        ArrayList<Socket> list=((ArrayList<Socket>)listaSalas.get(sala));
        if(list!=null){
            for (int j = 0; j < list.size(); j++) {
                enviarMensage(nick +": "+ mensaje,list.get(j) );
                System.out.println("iteracion: " + j);
                System.out.println("socket: " + list.get(j));
            }
        }
        }
            
    }
    
    private static String readLine() {
		try {
			return new BufferedReader(new InputStreamReader(System.in))
					.readLine();
		} catch (IOException e) {
			System.out.println("Error reading line!");
			e.printStackTrace();
			return "";
		}
    }
 
    static String crear_sala(String sala , Socket cliente){
        if(listaSalas.get(sala)==null){
            listaSalas.put(sala,new ArrayList<Socket>());
            ingresar_sala(sala, cliente);
            return sala;
        }else{
            enviarMensage("Error al crear la sala: " + sala, cliente);
            return null;
        }
    }
    
    static String ingresar_sala(String sala  , Socket cliente){
        if(listaSalas.get(sala)==null){
            //System.out.println("Error la sala no existe");
            enviarMensage("error no existe la sala: " + sala, cliente);
            return null;
        }else{
            ((ArrayList<Socket>)listaSalas.get(sala)).add(cliente);
            enviarMensage("Bienvenido a : " + sala, cliente);
            return sala;
        }
    }
    
    static void salir_sala(String sala  , Socket cliente){
        if(listaSalas.get(sala)==null){
            //System.out.println("Error la sala no existe");
            enviarMensage("error no existe la sala: " + sala, cliente);
        }else{
            ((ArrayList<Socket>)listaSalas.get(sala)).remove(cliente);
            enviarMensage("Has dejado a : " + sala, cliente);
            if(((ArrayList<Socket>)listaSalas.get(sala)).size()==0){
                listaSalas.remove(sala);
            }
        }
    }
    
    static void listar_salas(Socket cliente){
        Iterator it = listaSalas.entrySet().iterator();
        
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            int contadorUsers=0;
            for (Socket user : (ArrayList<Socket>)e.getValue()) {
                contadorUsers++;
            }
            enviarMensage(e.getKey() + " = " + contadorUsers, cliente);
        }
    }
}