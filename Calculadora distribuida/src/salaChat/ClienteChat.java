/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package salachat;

/**
 *
 * @author Christian Botero
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 
public class ClienteChat extends JFrame implements ActionListener,WindowListener{
 
 
    private static final long serialVersionUID = 1L;
 
 
    JTextField JTEXTO = null;
    JButton JBOTON = null;
 
    JTextArea JTAREA = null;
    JScrollPane scrollpane1 = null;
 
    //---
    JTextArea JTAREA2 = null;
    JScrollPane scrollpane2 = null;
 
    String ipserver,puerto,nick;
 
    //---
    Socket sock_c;//Socket Cliente
    PrintWriter canal_w;    //Canal de Escritura
    BufferedReader canal_r; //Canal de Lectura
    //---
 
 
    //--- CONSTRUCTOR ---
    public ClienteChat(String ipserver, String puerto, String nick) {
 
        this.ipserver = ipserver;
        this.puerto = puerto;
        this.nick = nick;
 
        crearInterfaz();
 
        try {
            //sock_c = new Socket(InetAddress.getByName(ipserver),Integer.parseInt(this.puerto));
            sock_c = new Socket(this.ipserver,Integer.parseInt(this.puerto));
            canal_w = new PrintWriter(sock_c.getOutputStream(),true);
            canal_r = new BufferedReader(new InputStreamReader(sock_c.getInputStream()));
 
            canal_w.println(nick);
 
            while(true){
                String linea = canal_r.readLine();
 
                if(linea.equals("FIN")){
                    System.exit(0);
                }else if((linea.length()>=7) && linea.substring(0,7).equals("CONNECT")){
                    JTAREA2.setText(linea.substring(8));
                }else if((linea.length()>=10) && linea.substring(0,10).equals("DISCONNECT")){
                    JTAREA2.setText(linea.substring(11));
                }else{
                    JTAREA.append(linea + "\n");//Mensajes
                }
            }
 
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }//Fin constructor
 
    private void crearInterfaz(){
        //--- Frame ---
        this.addWindowListener(this);
        this.setLayout(null);
        this.setSize(405, 570);
        this.setTitle("Chat - " + this.nick);
 
        //--- TextArea::Mensajes ---
        JTAREA = new JTextArea();
        JTAREA.setBounds(10, 10, 370, 370);
        JTAREA.setEditable(false);
        this.add(JTAREA);
 
        //--- JScrollPane::Mensajes ---
        scrollpane1 = new JScrollPane(JTAREA);
        scrollpane1.setBounds(10, 10, 370, 370);
        this.add(scrollpane1);
 
 
      //--- TextArea2::Usuarios Conectados ---
        JTAREA2 = new JTextArea();
        JTAREA2.setBounds(10, 420, 370, 100);
        JTAREA2.setEditable(false);
        this.add(JTAREA2);
 
        //--- JScrollPane2::Usuarios Conectados ---
        scrollpane2 = new JScrollPane(JTAREA2);
        scrollpane2.setBounds(10, 420, 370, 100);
        this.add(scrollpane2);
 
        //--- Caja de Texto ---
        JTEXTO = new JTextField();
        JTEXTO.setBounds(10, 390, 260, 20);
        this.add(JTEXTO);
 
        //--- Botón Enviar ---
        JBOTON= new JButton();
        JBOTON.setBounds(280, 390, 100, 20);
        JBOTON.setText("Enviar");
        JBOTON.addActionListener(this);
        this.add(JBOTON);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
 
    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub
        canal_w.println("FIN");
    }
 
    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
 
        Object control = arg0.getSource();
 
        if(control instanceof JButton){
            canal_w.println(JTEXTO.getText());
            JTEXTO.setText("");
        }
 
    }
 
}

