package com.chatbox.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;
import java.lang.String;


public class uNme extends JFrame implements ActionListener, Runnable{

	private static final long serialVersionUID = 1L;

	byte[] b =new byte[100];
    Socket client;
    ServerSocket server;
    DataInputStream in;
    DataOutputStream out;
    StringBuffer results=null ;
    String temp="";
    Container con;
    JPanel panel1,panel2;
    JTextField textfield;
    JButton button;
    JButton close;
    JList list;
    Vector data=null;
    Thread t=null;
    JScrollPane scroll;
    int index;
    int j=0;
    boolean flag = false;
    int bytes_available=0;


    public uNme(String title){

        super(title);

        con=this.getContentPane();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        con.setLayout(new BorderLayout());
        panel1 = new JPanel();
        panel2 = new JPanel();
        textfield= new JTextField(20);
        String[] d = {"Welcome to my Chat engine!!"};

        list = new JList(d);

        button = new JButton();
        close = new JButton();
        button.setText("SEND");
        button.addActionListener(this);

        close.setText("CLOSE");
        close.addActionListener(this);

        panel1.setLayout(new FlowLayout());
        panel1.add(textfield);
        panel1.add(button);

        scroll= new JScrollPane(list);
        scroll.setViewportView(list);

        panel2.setLayout(new BorderLayout());
        panel2.add(scroll,BorderLayout.NORTH);
        panel2.add(close,BorderLayout.SOUTH);

        con.add(panel1,BorderLayout.NORTH);
        con.add(panel2,BorderLayout.SOUTH);

        this.setSize(400,400);
        this.setVisible(true);
        pack();

        try {

            server = new ServerSocket(1541);
            System.out.println("Waiting for client...");
            client = server.accept();
            System.out.println("Client Accepted.");

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            t = new Thread(this);
            t.start();

            results= new StringBuffer();

            data =  new Vector();

        }

        catch(NullPointerException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

     public void recieve(){

	         try{

	             bytes_available = in.available();
	 			flag = false;


	             while(bytes_available != 0){
	 				flag= true;
	 				index = in.read(b);
	 				temp = new String(b);
	 				temp=temp.substring(0,index);
	 				results= results.append(temp);
	 				bytes_available = in.available();
	 				System.out.println("bytes_available :"+bytes_available);

	 				System.out.println("results:"+results.toString());

	             }

	 			if(/*bytes_available == 0 &&*/ flag== true){
	 				data.add("u :"+results.toString());
	 				list.setListData(data);
	 				System.out.println("Message recieved from client: " +results.toString() );
	 				results = new StringBuffer();
	 				flag= false;
	 			}



	         }

	         catch(Exception e){
	             e.printStackTrace();
	             System.exit(1);
	         }

        }



    public void send(){
        try{
            String str =textfield.getText();

            data.add("me: "+str);
            list.setListData(data);
            System.out.println("msg to be sent: "+str);
            out.writeBytes(str);
            System.out.println("ms sent: ");
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void close(){
        try{
            in.close();
            out.close();
            client.close();
            server.close();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()== button){
            send();
        } else{
            close();
        }
        textfield.setText("");

    }

    public void run(){

        System.out.println("Thread Started!!");

        while(true){
            recieve();
        }

    }


    public static void main(String args[]) {

        uNme obj= new uNme("uNme");

    }
}
