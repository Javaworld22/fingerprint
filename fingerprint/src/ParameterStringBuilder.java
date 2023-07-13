
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
public class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
    
    public class User{
			private String id;
			private String finger;
			
			public User(String id, String finger){
				this.id = id;
				this.finger = finger;
			}
			
		}
    
    public class MsgError{
			private String id;
			private String msg;
			
			public MsgError(String id, String msg){
				this.id = id;
				this.msg = msg;
			}
			
		}
    
    public String toJsonw( String id,String data){
		Gson g = new Gson();
			User user = new User(id,data);
			String transferable = g.toJson(user);
			return transferable;
	}
    
    public String toJsonError( String id,String msg){
		Gson g = new Gson();
			MsgError error = new MsgError(id,msg);
			String transferable = g.toJson(error);
			return transferable;
	}
	
}