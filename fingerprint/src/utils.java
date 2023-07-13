
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
public class utils {
    public static String readString(InputStream inputStream) throws IOException{
        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for(int n; (n=inputStream.read(buf)) > 0;){
            into.write(buf,0,n);
        }
        into.close();
        return new String(into.toByteArray(), "UTF-8");
    }
}
