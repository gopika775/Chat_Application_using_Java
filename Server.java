import java.net.*;
import java.io.*;

class Server{

    ServerSocket server; //variable
    Socket socket;

    BufferedReader br; //Variables for reading data....
    PrintWriter out; //Variables for writing data...   //these 2 varibles are for writing data 

    public Server() //Constructor..
    {
        try
        {
           server = new ServerSocket(7777);
           System.out.println("Server is ready to accept connection");
           System.out.println("Waiting...");
           socket = server.accept(); //code for accepting server- means accepting client(i.e) Socket..issi socket ka object return kardega 

           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //reading as input & writing as output
           out = new PrintWriter(socket.getOutputStream()); 

           startReading();
           startWriting();

        }



        catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void startReading()
    {
        //thread- read karke deta rahega
        Runnable r1 =()->
        {
            System.out.println("reader started..");
            
            try {
                   while(true) //taking while loop because we have to read multiple times..
                   {   
                      String msg = br.readLine() ;

                      if(msg.equals("exit")) //if client send exit then chat is terminated
                        {
                           System.out.println("Client terminated the chat");
                           socket.close();
                           break;
                        }

                       System.out.println("Client : " + msg);
                
                
                    } //while(true) means loop working for infinte times

                } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                System.out.println("Connection is closed..");
                }
            
        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
       //thread- data ko user se lega then send karega client tak
       Runnable r2 =()->
        {
            System.out.println("Writer started...");

            try 
            {
               
                while(!socket.isClosed())
               {
                   BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)); //created another variable br1
                
                   String content =br1.readLine(); 
                   out.println(content);
                   out.flush();

                   if(content.equals("exit")){
                     socket.close();
                     break;
                   }
                } 
            }

            catch (Exception e) {
              // TODO: handle exception
              //e.printStackTrace();
              System.out.println("Connection is closed..");
            }
  

        };
    

       new Thread(r2).start();
    }

  
    public static void main(String args[]){
        System.out.println("This is server.. Going to start server");

        new Server();
    }
}