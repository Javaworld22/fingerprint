/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Fmd.Format;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
//import java.io.FileInputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.io.IOException;
//import java.nio.charset.StandardCharsets;

//import java.nio.charset.Charset;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
//import java.awt.Frame;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
//import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;


import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
//import javax.swing.Box;
import java.awt.datatransfer.Clipboard;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;



public class Identification extends JPanel implements ActionListener, MouseMotionListener{


	
	private static JDialog m_dlgParent;
	private final Reader m_reader;
	private CaptureThreadUareU m_capture;
	//public List<FingerDB.Record> m_listOfRecords = new ArrayList<FingerDB.Record>();
	private Fmd[] m_fmds;
	public List<Fmd> m_fmdList = new ArrayList<Fmd>();
	public Reader.CaptureResult capture_result;
	public List<Fmd> m_printedFingers = new ArrayList<Fmd>();
	public Fmd[] m_fmdArray = null; // Will hold final array of FMDs to identify
									// against
        private static ReaderCollection m_collection;
        //private Box b;
        
        private URL url;
        private HttpURLConnection con;

	public File fileDB;
        
        private JLabel label;
        private JLabel title;
        private JLabel name;
        private JLabel correct;
        private JLabel clss;
        private JLabel gender;
        private JLabel clss1;
        private JLabel gender1;
        private JTextField tname;
        
        public static int m_finger_id;
        public Map<Integer, String> student_id;
        public static int index;
        
        private JLabel mouseLabel;
        private BufferedImage myPicture;
        private BufferedImage myPicture1;
        private BufferedImage cancelImage;

	public Identification(Map<String, 
	Object> parameters) {
            
             try{
    m_collection = UareUGlobal.GetReaderCollection();
	m_collection.GetReaders();
        System.out.println(this.getClass().getName());
	System.out.println(m_collection.get(0).GetDescription().name);
	System.out.println(m_collection.get(0).GetDescription().id);
	System.out.println(m_collection.get(0));
        System.out.println("Collections "+m_collection);
	System.out.println(m_collection.get(0).GetDescription().modality);
        student_id = new HashMap<>();
        
        
        }catch(UareUException e1){
 System.out.println("Error getting collection");
}
            
		m_reader =  m_collection.get(0);
		final File folders = new File("C:\\Users\\Abc\\Documents\\fingerprint");
		m_fmds = new Fmd[2]; // two FMDs to perform comparison
		loadFromFile(parameters);
                System.out.println("End of load ");
                
                setLayout(null);
                
                label = new JLabel("Match: ");
                                
                 title = new JLabel("Verification...");
                 title.setFont(new Font("Arial", Font.PLAIN, 18));
                title.setSize(150, 50);
                title.setLocation(150, 25);
                //add(title);
                
                try{
                myPicture = ImageIO.read(this.getClass().getResource("avarter-preview.png"));
                cancelImage = ImageIO.read(this.getClass().getResource("cancel-preview.png"));
                
                name = new JLabel(new ImageIcon(myPicture));
                //name.setFont(new Font("Arial", Font.PLAIN, 20));
                name.setSize(120, 120);
                name.setLocation(95, 47);
                add(name);
                }catch(IOException ex){
                    System.out.println("Cannot see picture "+ex.getMessage());
                    ex.printStackTrace();
                }
                
                
                clss = new JLabel("Class:");
                clss.setFont(new Font("Arial", Font.PLAIN, 20));
                clss.setSize(100, 50);
                clss.setLocation(95, 180);
                add(clss);
                
                clss1 = new JLabel("UB3A");
                clss1.setFont(new Font("Arial", Font.PLAIN, 20));
                clss1.setSize(100, 50);
                clss1.setLocation(166, 180);
                add(clss1);
                
                gender = new JLabel("Gender:");
                gender.setFont(new Font("Arial", Font.PLAIN, 20));
                gender.setSize(100, 50);
                gender.setLocation(95, 210);
                add(gender);
                
                gender1 = new JLabel("MALE");
                gender1.setFont(new Font("Arial", Font.PLAIN, 20));
                gender1.setSize(100, 50);
                gender1.setLocation(178, 210);
                add(gender1);
                
                 try{
                 myPicture1 = ImageIO.read(this.getClass().getResource("correct-preview.png"));
                
                correct = new JLabel(new ImageIcon(myPicture1));
                //name.setFont(new Font("Arial", Font.PLAIN, 20));
                correct.setSize(120, 120);
                correct.setLocation(105, 227);
                add(correct);
                }catch(IOException ex){
                    System.out.println("Cannot see picture "+ex.getMessage());
                    ex.printStackTrace();
                }
                
                mouseLabel = new JLabel();
                mouseLabel.setBounds(0, 40,100, 20);
                add(mouseLabel);
               
                
                 setOpaque(true);
                
               // b = Box.createHorizontalBox();
               // b.add(Box.createHorizontalGlue());
                //b.add(label, BorderLayout.CENTER);
                //b.add(Box.createHorizontalGlue());
                StartCaptureThread();
    
                
	}
        
          @Override
          public void mouseMoved(MouseEvent e) { 
               mouseLabel.setText("X= "+e.getX()+" Y= "+e.getY());
          }
          
          @Override
          public void mouseDragged(MouseEvent e) { 
               mouseLabel.setText("X= "+e.getX()+" Y= "+e.getY());
          }
          

        public void StartCaptureThread()  {
		//JDialog dlg = new JDialog((JDialog) null, "V", true);
		//Identification identification = new Identification(reader);
		//verification.m_enrollmentFmd = _enrolledFmd;
		//identification.doModal(dlg);

			// start capture thread
			//StartCaptureThread();
                        System.out.println("Started thread for identification");
		m_capture = new CaptureThreadUareU(m_reader, false,
				Fid.Format.ISO_19794_4_2005,
				Reader.ImageProcessing.IMG_PROC_DEFAULT,4);
		m_capture.start(this);
             
	}


	public void doModal(JDialog dlgParent) {
		// open reader
		try {
			m_reader.Open(Reader.Priority.COOPERATIVE);

			// start capture thread
			StartCaptureThread();

		// bring up modal dialog
                final ImageIcon backgroundImage = new ImageIcon("access_3.png");
                JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = Math.max(backgroundImage.getIconWidth(), size.width);
                size.height = Math.max(backgroundImage.getIconHeight(), size.height);

                return size;
            }
        };
                addMouseMotionListener(this);
		m_dlgParent = dlgParent;
                //m_dlgParent.add(b);
                //m_dlgParent.add(mainPanel);
		m_dlgParent.setContentPane(this);
		//m_dlgParent.pack();
                //m_dlgParent.setSize(400, 300);
		m_dlgParent.setLocationRelativeTo(null);
                m_dlgParent.setBounds(350, 90, 350, 400);
		m_dlgParent.setVisible(true);
                //m_dlgParent.setAlwaysOnTop(true);
               // m_dlgParent.setLocationByPlatform(true);
                //m_dlgParent.setFocusable(true);
		m_dlgParent.dispose();
                                                 
		
		// cancel capture
		StopCaptureThread();

		// wait for capture thread to finish
		WaitForCaptureThread();

		// close reader
		try {
			m_reader.Close();
		} catch (UareUException e) {
			
		}

	}catch(UareUException e){
		
	}
}

//	private void StartCaptureThread() {
//            System.out.println("Started thread for identification");
//		m_capture = new CaptureThreadUareU(m_reader, false,
//				Fid.Format.ISO_19794_4_2005,
//				Reader.ImageProcessing.IMG_PROC_DEFAULT,4);
//		m_capture.start(this);
//	}

	private void StopCaptureThread() {
		if (null != m_capture)
			m_capture.cancel();
	}

	private void WaitForCaptureThread() {
		if (null != m_capture)
			m_capture.join(1000);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Starting the action Event");
		if (e.getActionCommand().equals(CaptureThreadUareU.ACT_CAPTURE)) {
			// process result
			System.out.println("Starting process in identification ");
			 capture_result = m_capture.getLastCaptureResult();
                         System.out.println("Capture object "+capture_result.image);
				if (capture_result.image != null)
					if (ProcessCaptureResult()) {
						// restart capture thread
					WaitForCaptureThread();
					StartCaptureThread();
				}else {
					// destroy dialog
					m_dlgParent.setVisible(false);
				}
					}
		}

	private boolean ProcessCaptureResult() {
		boolean bCanceled = false;
               System.out.println();
		if (null == m_fmdArray || m_fmdArray.length < 1) {
			//MessageBox
			//		.Warning("You cannot verify until you register or load a template.");
                        System.out.println("You cannot verify until you register or load a template.");
			return !bCanceled;
		}

		if (null != capture_result) {
			if (null != capture_result.image
					&& Reader.CaptureQuality.GOOD == capture_result.quality) {
				// extract features
				Engine engine = UareUGlobal.GetEngine();

			try {
				Fmd fmd = engine.CreateFmd(capture_result.image,
							Fmd.Format.DP_VER_FEATURES);
				m_fmds[0] = fmd;

			}catch (UareUException e) {
					//MessageBox.DpError("Engine.CreateFmd()", e);
                                        System.out.println(e.getMessage());
				}
				
				try{
						// Perform identification
					
						int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; // target
																						// rate
																						// is
																						// 0.00001

				Candidate[] matches = engine.Identify(m_fmds[0], 0,
								m_fmdArray, target_falsematch_rate, 1);

				if (matches.length == 1){
							//JOptionPane
								//	.showMessageDialog(
									//		null,
										//	"Match found:"
											//		+ this.m_listOfRecords
												//			.get(matches[0].fmd_index).userID);
							System.out.println("Match Found: "+matches.length+"  " +matches[0].fmd_index);
                                                              
                                                                //label = new JLabel("Match Found "+matches[0].fmd_index);
                                                                int total_fingers =  matches[0].fmd_index + 1;
                                                                int finger_id;
                                                                if(total_fingers%4 == 0 ){
                                                                    finger_id = total_fingers/4;
                                                                 System.out.println("Finger ID if"+finger_id);
                                                                }else{
                                                                    int x = (int)total_fingers/4;
                                                                    x += 1;
                                                                    finger_id = x;
                                                                    System.out.println("Finger ID else"+finger_id);
                                                                }
                                                                try{
                                                               m_finger_id =  Integer.parseInt(student_id.get(finger_id));
                                                                }catch(Exception ee){
                                                                    ee.printStackTrace();
                                                                }
                                                                label.setText("Match identified "+m_finger_id);
                                                                label.repaint();
                                                                clss1.setText("UB1C");
                                                                clss1.repaint();
                                                                correct.setIcon(new ImageIcon(myPicture1));
                                                                correct.repaint();

                                }else{
							
						System.out.println("Not Identified");
                                                m_finger_id = 0;
                                                label.setText("Not Identified");
                                                                label.repaint();
                                                clss1.setText("No data");
                                                clss1.repaint();
                                                correct.setIcon(new ImageIcon(cancelImage));
                                                correct.repaint();
                                }
			}catch (UareUException e) {
					//MessageBox.DpError("Engine.CreateFmd()", e);
                                        System.out.println(e.getMessage());
				}
			}
		}else if (Reader.CaptureQuality.CANCELED == capture_result.quality) {
			// capture or streaming was canceled, just quit
			bCanceled = true;
		}

		else if (null != m_capture.getException()) {
			// exception during capture
			//MessageBox.DpError("Capture", m_capture.getException());
                        System.out.println(m_capture.getException());
			bCanceled = true;
		} else if (null != m_capture.getStatus()) {
			// reader failure
			//MessageBox.BadStatus(m_capture.getStatus());
                        System.out.println(m_capture.getStatus());
			bCanceled = true;
		}

		else {
			// bad quality
			//MessageBox.BadQuality(capture_result.quality);
                        System.out.println("capture_result.quality");
		}
		return !bCanceled;
	}

	

	private void loadFromFile(Map<String, 
	Object> parameters){
		InputStream input  = null;
		for(Map.Entry<String, Object> set : parameters.entrySet()){
                    String value = (String)set.getValue();
                    System.out.println(value);
                    index++;
			//if(fileEntry.isDirectory()){
			//	loadFromFile(fileEntry);
			//}else{
				//	fileDB = fileEntry;
				//System.out.println(fileEntry.getName());
                    	try{
                            
				String[] arrayFmd =  getString(value);
				//input = new BufferedInputStream(new FileInputStream(fileEntry));
				//byte[] data = new byte[input.available()];
				//input.read(data, 0, input.available());
				//input.close();

				System.out.println("ArrayFmd "+arrayFmd.length);
				for(int i = 0;i<arrayFmd.length;i++){
				byte[] predecoded = decodeToByte(arrayFmd[i]);
				Fmd fmd = UareUGlobal
							.GetImporter()
							.ImportFmd(
									predecoded,
									com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES,
									com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES);
				m_printedFingers.add(fmd);
                                System.out.println("End of load added to fmd");
				}
                                student_id.put(index, set.getKey());
			}catch (UareUException e1) {
				// TODO Auto-generated catch block
				//JOptionPane
					//	.showMessageDialog(null, "Error importing fmd data.");
				System.out.print("Error importing fmd data ");
				return;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				//JOptionPane.showMessageDialog(null, "Error saving file.");
				System.out.println("Error saving file."+e.getMessage());
				e.printStackTrace();
			}
		}
	//}
	m_fmdArray = new Fmd[this.m_printedFingers.size()];
	this.m_printedFingers.toArray(m_fmdArray); 
		
	}
	
	public static void clipBoard(String clipstring){
		
		 StringSelection stringSelection = new StringSelection(clipstring);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection,null);
	}
	
	public static String encodeToString(byte[] fmd){
		String encoded = Base64.getEncoder().encodeToString(fmd);
		//System.out.println(encoded);
		return encoded;
	}
	
	public static byte[] decodeToByte(String encoded){
		byte[] decoded = Base64.getDecoder().decode(encoded);
		return decoded;
	}
	
	public static String[] getString(String data){
		String[] tokens = new String[2];
		InputStream input  = null;
		//System.out.println("File length "+file.length());
		int counter = 0;
		int sizeCount = 0;
		String ss = null;
		//BufferedReader reader = null;
		try{
		//reader = new BufferedReader(
			//						new InputStreamReader(
				//							new FileInputStream(file), "UTF8"));
				
				//input = new BufferedInputStream(new FileInputStream(file));
				//byte[] data = new byte[input.available()];
				//input.read(data, 0, input.available());
				//input.close();
				//System.out.println(new String(data));
				ss = new String(data);
                                String mData = new String(); 
                               
                              
                                mData = URLDecoder.decode(ss, "UTF-8");
                                String secondEncode = URLDecoder.decode(mData, "UTF-8");
                                 ss = secondEncode;
                                System.out.println("Lengrh is "+ss.length());
                                System.out.println("Decoded "+ss);
                                
				
		}
//                catch(FileNotFoundException e){
//			System.out.println("File is not found "+e.getMessage());
//			e.printStackTrace();
//		}
                catch(Exception e){
			System.out.println("Exception found "+e.getMessage());
			e.printStackTrace();
		}
													
		int c;
		String s = new String();
	for(int i=0;i<ss.length();i++){
		s += ss.charAt(i);
		if(i != 0){
		if(i == 2187){
			System.out.println("Number "+i);
			tokens[sizeCount] = s;
			s = new String();
			sizeCount++;
			}else if(i == 4375){
				System.out.println("Number "+i);
			tokens[sizeCount] = s;
			s = new String();
			sizeCount++;
			}
//                        else if(i == 6563){
//				System.out.println("Number "+i);
//			tokens[sizeCount] = s;
//			s = new String();
//			sizeCount++;
//			}else if(i == 8751){
//				System.out.println("Number "+i);
//			tokens[sizeCount] = s;
//			s = new String();
//			sizeCount++;
//			}
		}
		
	}
		
			return tokens;
	}
} 