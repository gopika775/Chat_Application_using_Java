import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*; 

public class Client extends JFrame{

    Socket socket;
    BufferedReader br; //Variables for reading data....
    PrintWriter out;

    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN, 20);
    

    //Connstructor..
    public Client()
    {
        try {
            System.out.println("Sending request to server");
           socket = new Socket("127.0.0.1",7777); 
           System.out.println("Connection done..");
           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream());
  
           
           createGUI();
           handleEvents();
           
           startReading();
           //startWriting(); 

        } catch (Exception e) {
            // TODO: handle exception
        }
     
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key Released" + e.getKeyCode()); //e.getKeyCode() is written to know which key of keyboard is released..
                if(e.getKeyCode()==10)
                {
                    //System.out.println("You have pressed enter button..");
                    String contentToSend=messageInput.getText();
                    messagArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                } 
            }
            
        });
    }

    private void createGUI()
    {
        //GUI code
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null); //to make the screen in centre position
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Coding for Components..
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("new.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messagArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        
        //Setting layout for Frame..
        this.setLayout(new BorderLayout());

        //Adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        
        this.setVisible(true);

    }

    // Start Reading [METHOD]
    public void startReading()
    {
        //thread- read karke deta rahega
        Runnable r1 =()->{
            System.out.println("reader started..");

            try {
                while(true) //taking while loop because we have to read multiple times..
                {  
                       String msg = br.readLine() ;
    
                       if(msg.equals("exit")) //if client send exit then chat is terminated
                       {
                         System.out.println("Server terminated the chat");
                         JOptionPane.showMessageDialog(this, "Server terminated the chat");
                         messageInput.setEnabled(false);
                         socket.close();
                         break;
                       }
    
                       //System.out.println("Server: " + msg);
                       messagArea.append("Server : " + msg + "\n");
                    
                } //while(true) means loop working for infinte times
            } catch (Exception e) {
                // TODO: handle exception
                //e.printStackTrace(); now r not going to print this
                System.out.println("Connection is closed..");
            }

        };

        new Thread(r1).start();
    }

    // Start Writing & msgs Send - [METHOD]
    public void startWriting()
    {
       //thread- data ko user se lega then send karega client tak
       Runnable r2 =()->{
         System.out.println("Writer started...");
        
         try {
              while(!socket.isClosed()){ 
                
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)); //created another variable br1
                  
                String content =br1.readLine(); 

                out.println(content);
                out.flush();

                if(content.equals("exit")){
                 socket.close();
                  break;
                }
              }

              System.out.println("Connection is closed..");
           }
               
                     
                
                catch (Exception e)
                 {
                     // TODO: handle exception
                     e.printStackTrace();
                     
                 }
               
        };

       new Thread(r2).start();
    }

    public static void main(String args[]){
        System.out.println("This is client..s");
        new Client();
    }
}
