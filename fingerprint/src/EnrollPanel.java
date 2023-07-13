/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
import com.digitalpersona.uareu.UareUGlobal;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.ReaderCollection;



import javax.swing.JDialog;
import java.util.Date;
import java.io.*;


import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLEncoder;





	public class EnrollPanel extends Thread implements ActionListener {
		
		public class FMDUnicode implements Serializable{
			public List<String> list;
			public FMDUnicode(List<String> fmd){
				list = fmd;
			}
		}
		
		public class User{
			private String id;
			private String finger;
			
			public User(String id, String finger){
				this.id = id;
				this.finger = finger;
			}
			
		}
		
	protected Reader m_reader;
	private final Enroll enroll;
	private JDialog m_dlgParent;
	private int counter = 1;
	private boolean flag;
	private List<String> m_fmd;
	private List<byte[]> fmdList;
	private int count_finger;
	private int no_of_finger_placed;
	private String finger_type = "A";
	private EnrollPanel panel;
	public static final String ACT_CANCEL = "cancel_thread";
	public static final String ACT_START = "start_thread";
        public static final String ACT_IDENTIFY = "identification_thread";
        private static ActionListener m_listener;
        private static int _check_clipboard_call;
        private static boolean m_bCancel;
        private static String CANCEL = "";
        public static int clipboardcheck;
        private static ReaderCollection m_collection;
        //private static HttpServer m_server;
        public static String id;
        public static String finger;

	
protected EnrollPanel(HttpServer server){
   // m_server = server;
    try{
    m_collection = UareUGlobal.GetReaderCollection();
	m_collection.GetReaders();
        System.out.println(this.getClass().getName());
	System.out.println(m_collection.get(0).GetDescription().name);
	System.out.println(m_collection.get(0).GetDescription().id);
	//System.out.println(m_collection.get(0));
        //System.out.println("Collections "+m_collection);
	//System.out.println(m_collection.get(0).GetDescription().modality);
        
        
        }catch(UareUException e1){
 System.out.println("Error getting collection");
 System.out.println("System not plugged");
 new SendPostHandler("Error","System not plugged");
 e1.printStackTrace();
}
		m_reader = m_collection.get(0);
		fmdList = new ArrayList<byte[]>();
		//new CaptureThreadUareU().new ClipboardEvent();
                m_listener = this;
		
		enroll = new Enroll(m_reader,this);
                
                if(!(m_collection.get(0).GetDescription().name.equals("05ba&000a&0123{A75B9490-FEFB-0642-96E2-C78316F3C739}"))){ 
		return;
	}
                new SendPostHandler("Success","Server Started");
          
	}
public class Enroll extends Thread implements
			Engine.EnrollmentCallback {
				
		public Fmd enrollment_fmd;
		private final Reader m_reader;
		
		private CaptureThreadUareU m_capture;
		private final ActionListener m_listener;

		protected Enroll(Reader reader, final ActionListener listener){
		m_reader = reader;
		m_listener = listener;
			new SendPostHandler("Success","Fingerprint Started");
	}
				
				@Override
		public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
			Engine.PreEnrollmentFmd prefmd = null;
			//System.out.println("Start of callback");
			while (null == prefmd && !m_bCancel){
				// start capture thread
                                
				m_capture = new CaptureThreadUareU(m_reader, false,
						Fid.Format.ISO_19794_4_2005,
						Reader.ImageProcessing.IMG_PROC_DEFAULT,clipboardcheck);
				m_capture.start(m_listener);
				
				// wait till done
				m_capture.join(0);

				// check result
				Reader.CaptureResult capture_result = m_capture.getLastCaptureResult();
				System.out.println("Quality "+capture_result.quality);
				if (null != capture_result) {
					if (Reader.CaptureQuality.CANCELED == capture_result.quality) {
						// capture canceled, return null
						break;
					}else if (null != capture_result.image
							&& Reader.CaptureQuality.GOOD == capture_result.quality){
						
							//System.out.println("Finger Captured");
							System.out.println("Quality is "+capture_result.quality);
                                                        //////new SendPostHandler("Success","Quality is "+capture_result.quality);
							
							// acquire engine
						Engine engine = UareUGlobal.GetEngine();
						
						try {
							// extract features
							//System.out.println("Starting Pre-finger print");
							Fmd fmd = engine.CreateFmd(
									capture_result.image,
									Fmd.Format.DP_PRE_REG_FEATURES); //Fmd.Format.DP_REG_FEATURES  Fmd.Format.DP_PRE_REG_FEATURES

							// return prefmd
							prefmd = new Engine.PreEnrollmentFmd();
							prefmd.fmd = fmd;
							prefmd.view_index = 0;
							
							no_of_finger_placed++;
							if(no_of_finger_placed > 4){
								no_of_finger_placed = 1;
								
							}
							System.out.println(finger_type+no_of_finger_placed);
							String mod = toJsonw(finger_type+no_of_finger_placed,"");
							Identification.clipBoard(mod);
                                                        id = finger_type+no_of_finger_placed;
                                                        

							// send success
							//SendToListener(ACT_FEATURES, null, null, null, null);
							System.out.println("fingerprint captured, features extracted\n\n");
						} catch (UareUException e) {
							// send extraction error
							System.out.println("Error extracting fingerprint features "+m_capture.getException().getMessage());
                                                        //new SendPostHandler("Success","Error extracting fingerprint features "+m_capture.getException().getMessage());
						}
					}else {
						// send quality result
						//SendToListener(ACT_CAPTURE, null, evt.capture_result,
							//	evt.reader_status, evt.exception);
								System.out.println(m_capture.getException().getMessage());
								System.out.println(m_capture.getStatus().status);
					}
				}else {
					// send capture error
					//SendToListener(ACT_CAPTURE, null, evt.capture_result,
						//	evt.reader_status, evt.exception);
							System.out.println(m_capture.getException().getMessage());
								System.out.println(m_capture.getStatus().status);
				}
			}
                       // if(null != prefmd)
                         //   clipboardcheck = 0;
			return prefmd;
			
		}
		
		@Override
		public void run() {
			// acquire engine
			
			Engine engine = UareUGlobal.GetEngine();
			try{
					//m_bCancel = false;
				while (!m_bCancel && !(CANCEL.equals("identify"))) {
						// run enrollment
						System.out.println("Starting finger print");
					Fmd fmd = engine.CreateEnrollmentFmd(
							Fmd.Format.DP_REG_FEATURES, this);
						System.out.println("Ending finger print");
						//System.out.println("Get view count "+fmd.getViewCnt());
					// send result
					if (null != fmd) {
							//SendToListener(ACT_DONE, fmd, null, null, null);
							System.out.println("Done...!");
						//if there is no error
						//if(error == null){
						//String str = String
						//	.format("    Enrollment template created, size: %d\n\n\nPlease save to file or verify.",
						//			fmd.getData().length);
							//enrollmentFMD = fmd;
							fmdList.add(fmd.getData());
							//System.out.println(str);
							m_fmd = new ArrayList<String>();
							String unicode = null;
							String[] arrayof2 = new String[2];
							StringBuffer buf = new StringBuffer();
							
							
							//System.out.println(finger_type+no_of_finger_placed);
							//String mod = toJsonw(finger_type+no_of_finger_placed,"");
							//Identification.clipBoard(mod);
							System.out.println("FMDLIST "+fmdList);
                                                        System.out.println("FMDLIST size"+fmdList.size());
							if(fmdList.size() == 2){
								count_finger = 0;
								try{
									for(int i=0;i<fmdList.size();i++){
										arrayof2[i] = Identification.encodeToString(fmdList.get(i));
										System.out.println("Encoded Length "+arrayof2[i].length()); // This gives 2188
										buf.append(arrayof2[i]);
									}
									unicode = arrayof2[0]+arrayof2[1]/**+arrayof4[2]+arrayof4[3]**/;  
                                                                        System.out.println("unicode fingers " +unicode);
                                                                        String encodeurl = URLEncoder.encode(unicode, "UTF-8");
                                                                        finger = encodeurl;
                                                                        System.out.println("unicode fingers " +finger);
									//FMDUnicode f_Unicode = new FMDUnicode(m_fmd);
									//System.out.println("unicode " +unicode.trim().length());
									//System.out.println("Stringbuffer " +buf.length());
									//System.out.println("unicode " +unicode);
								//fmdList.toArray(m_fmd);
								
								
								
								
							writeToFile2(unicode, new Date());
							fmdList.clear();
							count_finger = 0;
							finger_type = "A";
							//cancel();
								}catch(Exception e){
									System.out.println("Error occurred at loading fingers "+e.getMessage());
									e.printStackTrace();
								}
							}else{
								count_finger++;
							no_of_finger_placed = 0;
							if(count_finger == 0)
								finger_type = "A";
							else if(count_finger == 1)
								finger_type = "B";
							else if(count_finger == 2)
								finger_type = "C";
							else if(count_finger == 3)
								finger_type = "D";
							}
						
					}else {
						//SendToListener(ACT_CANCELED, null, null, null, null);
						System.out.println("m_reader "+m_reader);
                                                System.out.println("m_bCancel "+m_bCancel);
                                                
                                                System.out.println("Canceled");
                                                System.out.println("CANCEL "+CANCEL);
                                                 if(m_bCancel && m_reader != null && !(CANCEL.equals("identify"))){
                                                     m_bCancel = false;
                                                        //flag = false;
                                                        fmdList.clear();
                                                        count_finger = 0;
                                                        finger_type = "A";
                                                        no_of_finger_placed = 0;
                                                        CANCEL = "";
                                                        System.out.println("Clipboard count "+_check_clipboard_call);
                                                        _check_clipboard_call = 0;
                                                       Enroll ee =  new Enroll(m_reader,EnrollPanel.this);
                                                                ee.start();
                                                                ee.join(100);
                                             }else if(CANCEL.equals("identify")){
                                                 if(null != m_reader)
                                                     m_reader.Close();
                                             }
						break;
					}
				}
                                if(CANCEL.equals("cancel") && m_bCancel){
                                    System.out.println("m_reader "+m_reader);
                                                System.out.println("m_bCancel "+m_bCancel);
                                                System.out.println("Canceled");
                                                 if(m_bCancel && m_reader != null){
                                                     m_bCancel = false;
                                                        //flag = false;
                                                        fmdList.clear();
                                                        count_finger = 0;
                                                        finger_type = "A";
                                                        no_of_finger_placed = 0;
                                                        CANCEL = "";
                                                        System.out.println("Clipboard count "+_check_clipboard_call);
                                                        _check_clipboard_call = 0;
                                                       Enroll ee =  new Enroll(m_reader,EnrollPanel.this);
                                                                ee.start();
                                                                ee.join(100);
                                             }
                                }
			}catch(UareUException e){
				System.out.println("Exception during creation of data and import "+e.getMessage());
				e.printStackTrace();
			}catch(NullPointerException e){
                            System.out.println("Nullpointer Exception");
                            e.printStackTrace();
                            
                        }
		}

		public void cancel() {
			m_bCancel = true;
                        System.out.println("From cancel() method "+m_bCancel);
                       
			//flag = true;
			if (null != m_capture){
				m_capture.cancel();
                                //if(!(CANCEL.equals("identify")))
                                
                        }
		}
		
		public void join(int i){
			if(m_capture != null)
				m_capture.join(i);
		}

}

		private void doModal(JDialog dlgParent) {
		// open reader
		
	  
		

		

		// close reader
		System.out.println("Finishing");
//                if(enroll.m_bCancel && flag && m_reader != null){
//                   // enroll.m_bCancel = false;
//                    flag = false;
//                    new Enroll(m_reader,this).start();
//             
	}

	private final void writeToFile(byte[] data, java.util.Date date){
	OutputStream output  = null;
	String clipEncode = null;
	try{
		File db1 = new File("C:\\Users\\Abc\\Documents\\fingerprint\\"+Long.toString(date.getTime())+".txt");
		File directory = new File("C:\\Users\\Abc\\Documents\\fingerprint");
		
		if(!directory.exists())
			directory.mkdirs();
		if(!db1.exists())
			db1.createNewFile();
		output = new BufferedOutputStream(new FileOutputStream(db1));
		String encoded = Identification.encodeToString(data); // Encode to string
		output.write(encoded.getBytes());
				output.close();
				clipEncode = encoded;
	}catch(IOException e){
		System.out.println("Error occurred");
	}catch (Exception e) {
				// TODO Auto-generated catch block
				//JOptionPane.showMessageDialog(null, "Error saving file.");
			}
			String mod = toJsonw(finger_type+no_of_finger_placed,clipEncode);
			Identification.clipBoard(mod);
	}
	
	private final void writeToFile2(String data, java.util.Date date){
	OutputStream output  = null;
	String clipEncode = null;
	try{
		//File db1 = new File("C:\\Users\\Abc\\Documents\\fingerprint\\"+Long.toString(date.getTime())+".txt");
		//File directory = new File("C:\\Users\\Abc\\Documents\\fingerprint");
		
//		if(!directory.exists())
//			directory.mkdirs();
//		if(!db1.exists())
//			db1.createNewFile();
//		output = new BufferedOutputStream(new FileOutputStream(db1));
//		
//		output.write(data.getBytes());
//				output.close();
				//clipEncode = data;
                               clipEncode = URLEncoder.encode(data, "UTF-8");
                                 new SendPostHandler(finger_type+no_of_finger_placed, clipEncode);
	}
//        catch(IOException e){
//		System.out.println("Error occurred");
//	}
        catch (Exception e) {
            e.printStackTrace();
				// TODO Auto-generated catch block
				//JOptionPane.showMessageDialog(null, "Error saving file.");
			}
			//String mod = toJsonw(finger_type+no_of_finger_placed,clipEncode); 
			//Identification.clipBoard(mod);
                       
	}

        @Override
	public void  run() {
            //super.run();
            Reader mReader = null;
            try{
                      m_collection = UareUGlobal.GetReaderCollection();
                                                 System.out.println("Collections "+m_collection);
                                                    m_collection.GetReaders();
                                                    //m_reader = null;
                                                    
                                                    mReader = m_collection.get(0);
                     }catch(UareUException e1){
                        System.out.println("Error Destroying collection");
                }
		try {
			m_reader.Open(Reader.Priority.EXCLUSIVE);
		} catch (UareUException e) {
			//MessageBox.DpError("Reader.Open()", e);
                        System.out.println(e.getMessage());
		}
                 try{
		 Reader.Status rs = m_reader.GetStatus();
		System.out.println("Status is "+rs.status);
		} catch (UareUException e) {
			System.out.println("Error happened here");
		}
                 
                 Reader.Capabilities rc = m_reader.GetCapabilities();
		System.out.println("Can stream is "+rc.can_stream);
		System.out.println("Can capture is "+rc.can_capture);
		System.out.println("Resolution "+m_reader.GetCapabilities().resolutions[0]);


		// start enrollment thread
		enroll.start();
	}
	
	private String toJsonw( String id,String data){
		Gson g = new Gson();
			User user = new User(id,data);
			String transferable = g.toJson(user);
			return transferable;
	}
	
	//public void dispose1(){
	//	this.dispose();
	//}
	
	
	
	//private void cancel(){
		//enroll.cancel();
	//}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACT_CANCEL)) {
                    CaptureThreadUareU.PrintEvent evt = (CaptureThreadUareU.PrintEvent) e;
                    clipboardcheck =  evt.m_check;
			System.out.println("Account cancelled");
                        _check_clipboard_call++;
                         System.out.println("Clipboard count "+_check_clipboard_call);
                         //if(_check_clipboard_call == 1){
                                enroll.cancel();
                        // }
		}else if(e.getActionCommand().equals(ACT_START)){
			System.out.println("Account started");
			//enroll.join(100);
			enroll.start();
		}else if(e.getActionCommand().equals(ACT_IDENTIFY)){
                    CANCEL = "identify";
                    _check_clipboard_call++;
                    if(_check_clipboard_call == 1){
                     if (null != enroll.m_capture){
				//enroll.m_capture.cancel();
                                //enroll.join(0);
//                                try{
//                                m_reader.Close();
//                                }catch(UareUException a){
//                                    System.out.println("Error encountered while closing Reader");
//                                    a.printStackTrace();
//                                }
                     }
//                     try {
//			m_reader.Close();
//                        enroll.join(0);
//		} catch (UareUException d) {
//			//MessageBox.DpError("Reader.Close()", e);
//                        System.out.println(d.getMessage());
//		}
//                     try{
//                     UareUGlobal.DestroyReaderCollection();
//                     }catch(UareUException e1){
//                        System.out.println("Error Destroying collection");
//                }
//                     try{
//                      m_collection = UareUGlobal.GetReaderCollection();
//                                                 System.out.println("Collections "+m_collection);
//                                                    m_collection.GetReaders();
//                                                    //m_reader = null;
//                                                    
//                                                    m_reader = m_collection.get(0);
//                                                    //Identification.Run(m_reader);
//                     }catch(UareUException e1){
//                        System.out.println("Error Destroying collection");
//                }
                    }
                     
                     
	}
      }
        
        public void cancel(String s) {
            CANCEL = s;
            enroll.cancel();
        }
	
	
}
