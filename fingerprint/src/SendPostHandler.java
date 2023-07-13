
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
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
public class SendPostHandler implements HttpHandler {
    
    private String id;
    private String msg;
    
    
    public SendPostHandler(String id, String msg){
        this.id = id;
        this.msg = msg;
    }

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
                 String json = new ParameterStringBuilder().toJsonw(id, msg);
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
