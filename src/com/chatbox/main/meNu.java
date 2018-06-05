package com.chatbox.main;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;
import java.lang.String;


public class meNu extends JFrame implements ActionListener, Runnable{

       StringBuffer results= null;
       String temp="";
       byte[] b= new byte[100];
       int j=0;
       Socket socket;
       DataInputStream in;
       DataOutputStream out;
       Container con;
       JPanel panel1,panel2;
       JTextField textfield;
       JButton button;
       JButton close;
       JList list;
       JScrollPane scroll;
       Vector data=null;
       Thread t= null;
       int index;
       boolean flag = false;
       int bytes_available;



       meNu(String title){
       super(title);

     con=this.getContentPane();
     this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

     con.setLayout(new BorderLayout());
     panel1 = new JPanel();
     panel2 = new JPanel();
     textfield= new JTextField(20);
     String[] d = {"Welcome to my Chat Engine !!"};

     list = new JList(d);

     button = new JButton();
     close = new JButton();

     button.setText("SEND");
     close.setText("CLOSE");

     close.addActionListener(this);
     button.addActionListener(this);

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

       System.out.println("Connecting to server at local host");
       socket = new Socket("192.168.200.168", 1541);
       System.out.println("Connected.");
       in = new DataInputStream(socket.getInputStream());
       out = new DataOutputStream(socket.getOutputStream());

       t = new Thread(this);
       t.start();



       data= new Vector();
       results = new StringBuffer();

       }

       catch(NullPointerException e){
       e.printStackTrace();
       }

       catch (Exception e) {
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
       }
       catch(Exception e){
       e.printStackTrace();
       System.exit(1);
       }
       }

       public void close(){
       try{
       in.close();
       out.close();
       socket.close();
       }
       catch(Exception e){
       e.printStackTrace();
       System.exit(0);
       }
       }

       public void run(){


       System.out.println("Thread Started!!");
       while(true){

       recieve();
       }
       }

        public void actionPerformed(ActionEvent e){
        if(e.getSource() == button){
        send();
        }
        else{
        close();
        }
        textfield.setText("");
	}

       public static void main(String args[]) {

       meNu obj = new meNu("meNu");


   }
}