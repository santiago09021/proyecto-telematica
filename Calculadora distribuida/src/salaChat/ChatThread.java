/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package salachat;

/**
 *
 * @author Christian Botero
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class ChatThread extends Thread {
 
    private Socket clientSocket = null;
    private String nick = null;
    private String sala = null;
 
    public ChatThread(Socket param){
 
        this.clientSocket = param;
 
    }
 
    public void run(){
 
        try {
 
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("#Conectando con el servidor");
            out.println("#Abriendo streams");
            out.println("#Conectado");
 
            this.nick = in.readLine();
            if(!ServidorChat.setNick(nick)){
                out.println("El nick  <" + nick + ">. ya existia lo sentimos");
                clientSocket.close();
                return;
            }
            out.println("Bienvenido al Chat <" + nick + ">. Teclea quit para terminar.");
            ServidorChat.comunicar_a_todos("#Se ha conectado: " + this.nick, this.nick, 1);
 
            //Difundir mensaje
            while(true){
 
                String mensaje = in.readLine();
 
                if(mensaje.equals("quit")){
                    ServidorChat.comunicar_a_todos("#Se ha desconectado: " + this.nick, this.nick, 2);
                    out.println("quit");
                    break;
                }
                if(mensaje.startsWith("create_topic")){
                    sala=ServidorChat.crear_sala(mensaje.substring("create_topic".length()+1,mensaje.length()), clientSocket);
                } 
                if(mensaje.startsWith("join_topic")){
                    sala=ServidorChat.ingresar_sala(mensaje.substring("join_topic".length()+1,mensaje.length()), clientSocket);       
                }
                if(mensaje.startsWith("list_user")){
                    ServidorChat.listarConectados(clientSocket);
                } 
                if(mensaje.startsWith("list_topic")){
                    ServidorChat.listar_salas(clientSocket);
                }
                if(mensaje.startsWith("leave_topic")){
                    ServidorChat.salir_sala(sala,clientSocket);
                    sala=null;
                }
                if(mensaje.startsWith("send")){
                    if(sala!=null){
                    ServidorChat.comunicar_a_sala(mensaje.substring("send".length()+1,mensaje.length()),this.nick, this.sala , 0);
                    }else{
                        ServidorChat.enviarMensage("Usted no esta logueado en ninguna Sala", clientSocket);
                    }
                    
                }
 
            }
 
            ServidorChat.eliminar_socket_cliente(this.clientSocket , sala);
            clientSocket.close();
 
 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
}//Fin clase.
