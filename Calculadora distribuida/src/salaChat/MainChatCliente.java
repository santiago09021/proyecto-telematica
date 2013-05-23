/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package salachat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Christian Botero
 */
public class MainChatCliente {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        /*
         •    - Dirección IP del Servidor.
         •    - Puerto del Servidor.
         •    - Nick del usuario.
         */
        System.out.print("ingrese la ip:");
        String ipserver = readLine();
        System.out.print("ingrese el puerto:");
        String puerto = readLine();
        System.out.print("ingrese su nick:");
        String nick = readLine();

        ClienteChat charla = new ClienteChat(ipserver, puerto, nick);

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
}