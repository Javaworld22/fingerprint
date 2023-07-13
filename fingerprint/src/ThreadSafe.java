

import com.google.gson.Gson;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import com.sun.net.httpserver.HttpServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ThreadSafe extends Thread implements ActionListener{
    
    static  Clipboard cb;
    private EnrollPanel enroll;
    private static int counter;
    private Identification identify;
    private URL url;
    private HttpURLConnection con;
    private static HttpServer server;
    
   
    public ThreadSafe(){
          System.out.println("Constructor");
//          try{
//          url = new URL("http://localhost:3030/user/fingerprint");
//     con = (HttpURLConnection) url.openConnection();
//   con.setRequestMethod("POST");
//   con.setRequestProperty("Content-Type", "application/json");
//          }catch( MalformedURLException e){
//               e.
//}catch(IOException e){
//    
//}
          
//           cb = Toolkit.getDefaultToolkit().getSystemClipboard();
//           cb.addFlavorListener(new FlavorListener() {
//            @Override
//            public void flavorsChanged(FlavorEvent e) {
//                processClipboard(cb);
//            }
//        });
//           
         
           
    }
    
    
     public class EchoPostHandler implements HttpHandler {

        public EchoPostHandler(){
            
        }
        
         @Override

         public void handle(HttpExchange he) throws IOException {
                 // parse request
                 he.setAttribute("Content-Type", 
                         "application/json");
                 StringBuilder source = new StringBuilder();
                 List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
                 Map<String, Object> objects = new HashMap<String, Object>();
                 InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                 BufferedReader br = new BufferedReader(isr);
                 String query = "";
                 while((query = br.readLine()) != null) {
                     query = URLDecoder.decode(query, "UTF-8");
                  source.append(query);
                  System.out.println(query);
                 parseQuery(query, parameters, objects);
                 }
                 
                 Gson gson = new Gson();
                 
//                 User.UserArray userArray = gson.fromJson(query, User.UserArray.class);
//                 System.out.println("Number of arrays: "+userArray);
//                 for(User user: userArray.getUserArray()){
//                     System.out.println(user);
//                 }
                 

                 // send response
                 String response = "";
                 Map<String, Object> obj;
                 Iterator it = parameters.iterator();
                 System.out.println(parameters.size());
                 while (it.hasNext()){
                          obj = (Map)it.next();
                          System.out.println(obj);
                 }
                 
//                 he.sendResponseHeaders(200, response.length());
//                 OutputStream os = he.getResponseBody();
//                 os.write(response.toString().getBytes());
//                 os.close();
                System.out.println("Query: "+query);
                System.out.println("Response: "+he.getRequestBody());
                System.out.println("Parameter: "+parameters);
                
                if(query.length() > 4000){
                     javax.swing.SwingUtilities.invokeLater(new Runnable(){
                                    @Override
                                        public void run(){
                                            
                                             identify = new Identification(null); //parameters
                                        JDialog dlg = new JDialog((JDialog) null, "Verify", true);
                                identify.doModal(dlg);
                                   //System.out.println("Identify");
                                    }
                                });
         }else if(response.toString().equals("enroll")){
             if(null != server){
              enroll = new EnrollPanel(server);
                                enroll.start();
             }
         }else if(response.toString().equals("cancel")){
             if(enroll != null)
                 enroll.cancel("cancel");
         }
      }
}
     
      public class SendPostHandler implements HttpHandler {

         @Override

         public void handle(HttpExchange he) throws IOException {
                 // parse request
                 he.setAttribute("Content-Type", 
                         "application/json");
                 he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                 he.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                he.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
                he.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                he.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
                 String json = new ParameterStringBuilder().toJsonw("0023", "yyhgghujgdbchdhbdshc");
                 byte[] s = json.getBytes();
                 Map<String, Object> parameters = new HashMap<String, Object>();
                 he.sendResponseHeaders(200, s.length);
                 OutputStream isr = he.getResponseBody();
                 isr.write(s);
                 isr.flush();
                 isr.close();
                 //String query = br.readLine();
                 //System.out.println(query);
                 
         }
}
    
    
    public void processClipboard(Clipboard cb) {
        // gets the content of clipboard
        Transferable trans = cb.getContents(null);
        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                // cast to string
                String s = (String) trans
                        .getTransferData(DataFlavor.stringFlavor);
						System.out.println("Clipboard listener "+s);
                //System.out.println(s);
				if(s.equals("cancel")){	
				
                                System.out.println("cancel");
					
				}else if(s.equals("start")){
                                   System.out.println("start");
				System.out.println("Start");
				}else if(s.equals("identify")){
                                    //enroll.cancel(s);
                                     System.out.println("Counter is "+counter++);
                                    System.out.println("Thread alive for enroll "+enroll.isAlive());
                                    System.out.println("Thread interrupted for enroll "+enroll.isInterrupted());
                                    if(counter == 1){
                                    try{
                                        enroll.cancel(s);
                                        
                                        enroll.join(0);
                                        //enroll.m_reader.Close();
                                        //enroll.join(0);
                                   
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }
//                                    catch( UareUException a){
//                                        a.printStackTrace();
//                                  
                                
                                    }
                                }
				//else if(s.)
                // only StringSelection can take ownership, i think
                StringSelection ss = new StringSelection(s);
                // set content, take ownership
                cb.setContents(ss, null);
            } catch (UnsupportedFlavorException e2) {
                e2.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    @Override
    public void run(){
//        
                                // enroll = new EnrollPanel();
                               // enroll.start();
                          
                
              try{
    int port = 9112;
 server = HttpServer.create(new InetSocketAddress(port), 0);
System.out.println("server started at " + port);
//server.createContext("/", new RootHandler());
server.createContext("/sendPostHandler", new SendPostHandler());
//server.createContext("/echoGet", new EchoGetHandler());

server.createContext("/echoPost", new EchoPostHandler());
server.createContext("/sendEnrollData", new SendEnrollData());
server.createContext("/sendVerifyData", new SendVerifyData());
server.setExecutor(null);
server.start();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
                                
                                    // identify.StartCaptureThread();
    }
    
    public static void parseQuery(String query, List<Map<String, 
	Object>> parameters, Map<String, Object> objects) throws UnsupportedEncodingException {
         boolean flag;
         if (query != null) {
                 String pairs[] = query.split("[&]");
                 int j = 1;
                 for (String pair : pairs) {
                      if(j%5 == 0){
                          flag = true;
                      } else{
                          flag = false;
                      }
                     String param[] = pair.split("[=]");
                     String key = null;
                          String value = null;
                          j++;
                         for(int i=0;i<param.length;i++){
                            // System.out.println(param[i]);
                          if(i == 0){
                           key = param[i];
                           if(key.contains("id"))
                               key = "id";
                           else if(key.contains("fname"))
                               key = "fname";
                           else if(key.contains("lname"))
                               key = "lname";
                           else if(key.contains("picture"))
                               key = "picture";
                           else if(key.contains("finger_extract"))
                               key = "finger_extract";
                          }
                          if(i == 1)
                            value = param[i];
                    }
                         if(!flag)
                             objects.put(key, value);
                         if(flag){
                             objects.put(key, value);
                             System.out.println(objects);
                             parameters.add(objects);
                             objects = new HashMap<>();
                         }
                         
                 }
         }
         //System.out.println(parameters);
}
    
    @Override
	public void actionPerformed(ActionEvent e) {
            
        }
	
}
