/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUGlobal;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Engine;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;

import java.io.*;



 import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class ReadCapture extends Thread implements ActionListener{
	
	

private static final long serialVersionUID = 2;
	private static final String ACT_BACK = "back";

	private CaptureThreadUareU m_capture;
	private static Reader m_reader;
	private boolean m_bStreaming;
     private static ReaderCollection m_collection;
	private static Fmd enrollmentFMD;
	private static Reader.Status rs;
	private static Reader.CaptureResult cr;
	//private final ActionListener m_listener;
        
        // read clipboard and take ownership to get the FlavorListener notified
        // when the content has changed but the owner has not
        //processClipboard(cb);
	
	
	
	public ReadCapture(){
		
		
	}
	
public static void main(String args[]){
	//writeToFile("this is seroius".getBytes(), new java.util.Date());
	
			new ReadCapture();
//try{
//	
//	m_collection = UareUGlobal.GetReaderCollection();
//	m_collection.GetReaders();
//	System.out.println(m_collection.get(0).GetDescription().name);
//	System.out.println(m_collection.get(0).GetDescription().id);
//	System.out.println(m_collection.get(0));
//        System.out.println("Collections "+m_collection);
//	System.out.println(m_collection.get(0).GetDescription().modality);
//	
//	if(!(m_collection.get(0).GetDescription().name.equals("05ba&000a&0123{A75B9490-FEFB-0642-96E2-C78316F3C739}"))){ 
//		return;
//	}
//}catch(UareUException e1){
// System.out.println("Error getting collection");
//}
//	m_reader = m_collection.get(0);
	
	//try {
		//	m_reader.Open(Reader.Priority.EXCLUSIVE); //Priority. EXCLUSIVE COOPERATIVE
			//m_reader.Open(Reader.Priority.COOPERATIVE); //Priority. EXCLUSIVE COOPERATIVE
	//		rs = m_reader.GetStatus();
	//	} catch (UareUException e) {
	//		System.out.println("Error happened here");
	//	}
		
		
		//try{
		//if (Reader.ReaderStatus.READY == rs.status
		//				|| Reader.ReaderStatus.NEED_CALIBRATION == rs.status) {
		//					System.out.println("Waiting here");
							// UareUGlobal.DestroyReaderCollection();
							//engine.Close();
							
				//	 cr = m_reader.Capture(Fid.Format.ISO_19794_4_2005,   //ISO_19794_4_2005  ANSI_381_2004
				//	Reader.ImageProcessing.IMG_PROC_DEFAULT,
				//		m_reader.GetCapabilities().resolutions[0], -1);
					//m_reader.CancelCapture();
					//Reader.CaptureResult cr = m_reader.GetStreamImage(Fid.Format.ISO_19794_4_2005,
					//		Reader.ImageProcessing.IMG_PROC_DEFAULT, 500);
					//	System.out.println("Status is "+cr.quality);
				//		} 
						
		//} catch (UareUException e) {
		//	System.out.println("Error happened here "+e.getMessage());
		//	e.printStackTrace();
		//}
						// acquire engine
						//Engine engine = UareUGlobal.GetEngine();
						
						//try{
						//Fmd fmd = engine.CreateFmd(
						//			cr.image,
						//			Fmd.Format.DP_PRE_REG_FEATURES);
						//			System.out.println("FMD "+fmd);
						//} catch (UareUException e) {
			//System.out.println("Error happened here");
		//}
		//try {
			//m_reader.Calibrate();
			
				System.out.println("Start program");
		  ThreadSafe safe = new ThreadSafe();
                  safe.start();
                  
                 
		
			////EnrollPanel.Run(null,"");
			
		
		     //Identification.Run(m_reader);
			
		//enrollmentFMD = Enrollment.Run(m_reader);
 }

@Override
	public void actionPerformed(ActionEvent e) {
            
            System.out.println("Event at Main method");
        }

	
}