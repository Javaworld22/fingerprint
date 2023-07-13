
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
public class JavaNetwork extends Thread {
    
    
//    @Override
//    public void run(){
//    try{
//    URL url = new URL("http://localhost:3030/user/fingerprint");
//    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//   con.setRequestMethod("POST");
//   con.setRequestProperty("Content-Type", "application/json");
//    con.setDoOutput(true);
//    System.out.println("First");
//   OutputStream out = con.getOutputStream();
//    System.out.println("second");
//    var json = new ParameterStringBuilder().toJsonw("0023", "yyhgghujgdbchdhbdshc");
//    System.out.println("third");
//    out.write(json.getBytes());
//    System.out.println(json);
//    //System.out.println(con.getRequestMethod());
//    System.out.println("fourth");
//    //out.flush();
//    //out.close();
//   
//    
//    int responseCode = con.getResponseCode();
//		System.out.println("POST Response Code :: " + responseCode);
//                
//                
//                if (responseCode == HttpURLConnection.HTTP_OK) { //success
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//                        
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//
//			// print result
//			System.out.println(response.toString());
//                }else {
//			System.out.println("POST request not working");
//		}
//                
//}catch( MalformedURLException e){
//    
//}catch(IOException e){
//    
//}
//    }
    
    
    public class EchoPostHandler implements HttpHandler {

        public EchoPostHandler(){
            
        }
        
         @Override

         public void handle(HttpExchange he) throws IOException {
                 // parse request
                 Map<String, Object> parameters = new HashMap<String, Object>();
                 InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                 BufferedReader br = new BufferedReader(isr);
                 String query = br.readLine();
                 //System.out.println(query);
                 parseQuery(query, parameters);
                 
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
    @Override
    public void run(){
        try{
    int port = 9000;
HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
System.out.println("server started at " + port);
//server.createContext("/", new RootHandler());
server.createContext("/sendPostHandler", new SendPostHandler());
//server.createContext("/echoGet", new EchoGetHandler());
server.createContext("/echoPost", new EchoPostHandler());
server.setExecutor(null);
server.start();
        }catch(IOException e){
            
        }
    }
    
    public static void Main1(String args[]){
        JavaNetwork network = new JavaNetwork();
        Thread t = new Thread(network);
        t.start();
    }
    
    public static void parseQuery(String query, Map<String, 
	Object> parameters) throws UnsupportedEncodingException {
        //List<String> list = new ArrayList();
         if (query != null) {
                 String pairs[] = query.split("[&]");
                 for (String pair : pairs) {
                     String param[] = pair.split("[=]");
                     String key = null;
                          String value = null;
                         for(int i=0;i<param.length;i++){
                             //System.out.println(param[i]);
                          if(i == 0)
                           key = param[i];
                          if(i == 1)
                            value = param[i];
                    }
                         parameters.put(key, value);
                         
                 }
         }
         //System.out.println(parameters);
}
}
