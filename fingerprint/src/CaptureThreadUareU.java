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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.StringSelection;
 import java.awt.Toolkit;
 import java.awt.datatransfer.Clipboard;
 
 import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
 

public class CaptureThreadUareU extends Thread {
	
	
	public class PrintEvent extends ActionEvent {
		private static final long serialVersionUID = 101;
                public int m_check;
                //private HttpServer m_server;
		
		public PrintEvent(Object source, String action,
				Reader.CaptureResult cr, Reader.Status st, int check) {
			super(source, ActionEvent.ACTION_PERFORMED, action);
			capture_result = cr;
			reader_status = st;
			//exception = ex;
                        m_clipboardcheck = check;
                        m_check = check;
                        //m_server = server;
		}
		
	}
        
        public class ClipboardEvent{
            
            public ClipboardEvent(){
            
            }
        }
	
	
	public static final String ACT_CAPTURE = "capture_thread_captured";
	
	public static final String ACT_CANCEL = "cancel_thread";
	public static final String ACT_START = "start_thread";
        public static final String ACT_IDENTIFY = "identification_thread";
	
	private boolean m_bCancel;
	private Reader m_reader;
	private boolean m_bStream;
	private Fid.Format m_format;
	private Reader.ImageProcessing m_proc;
	public Reader.CaptureResult capture_result;
	public Reader.Status reader_status;
	public UareUException exception;
        static final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        private static int m_clipboardcheck;
	//private CaptureEvent m_last_capture;
	
	private ActionListener m_listener;
//        static final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        // read clipboard and take ownership to get the FlavorListener notified
        // when the content has changed but the owner has not
        //processClipboard(cb);
       
	public CaptureThreadUareU(ActionListener listener){
            m_listener = listener;
        }
        
	public CaptureThreadUareU(Reader reader, boolean bStream, Fid.Format img_format,
			Reader.ImageProcessing img_proc,int clipboardcheck) {
		m_bCancel = false;
		m_reader = reader;
		m_bStream = bStream;
		m_format = img_format;
		m_proc = img_proc;
                m_clipboardcheck = clipboardcheck;
                m_clipboardcheck++;
                System.out.println("EnrollPanel.clipboardcheck "+m_clipboardcheck);
//                if(m_clipboardcheck == 1)
//                     cb.addFlavorListener(new FlavorListener() {
//            @Override
//            public void flavorsChanged(FlavorEvent e) {
//                processClipboard(cb);
//            }
//        });
//                
		
			 // The clipboard
//        final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        // read clipboard and take ownership to get the FlavorListener notified
        // when the content has changed but the owner has not
        //processClipboard(cb);
       
        // keep thread for testing
       // Thread.sleep(100000L);
	}
	
	public void start(ActionListener listener) {
		m_listener = listener;
		
		super.start();
	}
	
	public void join(int milliseconds) {
		try {
			super.join(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void Capture() {
		try {
			// wait for reader to become ready
			boolean bReady = false;
                        System.out.println("bReady "+bReady+" "+"m__bCancel "+m_bCancel);
			while (!bReady && !m_bCancel) {
				Reader.Status rs = m_reader.GetStatus();
				reader_status = rs;
                                System.out.println("Status "+rs.status);
				if (Reader.ReaderStatus.BUSY == rs.status) {
					// if busy, wait a bit
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				} else if (Reader.ReaderStatus.READY == rs.status
						|| Reader.ReaderStatus.NEED_CALIBRATION == rs.status) {
					// ready for capture
					bReady = true;
					break;
				} else {
					// reader failure
					//NotifyListener(ACT_CAPTURE, null, rs, null);
					break;
				}
			}
			if (m_bCancel) {
				Reader.CaptureResult cr = new Reader.CaptureResult();
				cr.quality = Reader.CaptureQuality.CANCELED;
				NotifyListener(ACT_CAPTURE, cr, null, m_clipboardcheck);
			}

			if (bReady) {
				// capture
                                System.out.println("Start capture ");
				Reader.CaptureResult cr = m_reader.Capture(m_format, m_proc,
						m_reader.GetCapabilities().resolutions[0], -1);
						capture_result = cr;
                                                System.out.println("At capture here");
				NotifyListener(ACT_CAPTURE, cr, null, m_clipboardcheck);
			}
		} catch (UareUException e) {
			//NotifyListener(ACT_CAPTURE, null, null, e);
			exception = e;
		}
	}

	private void Stream(){
		System.out.println("Cannot Stream");
	}
	
	public void cancel() {
		m_bCancel = true;
		try {
			if (!m_bStream)
				m_reader.CancelCapture();
				//System.exit(0);
		} catch (UareUException e) {
		}
	}
	
	@Override
	public void run() {
		if (m_bStream) {
			Stream();
		} 
		else {
			Capture();
		}
	}
	
	public Reader.CaptureResult getLastCaptureResult() {
		return capture_result;
	}
	
	public Reader.Status getStatus(){
		return reader_status;
	}
	
	public UareUException getException(){
		return exception;
	}
	
	private void NotifyListener(String action, Reader.CaptureResult cr,
			Reader.Status st, int check) {
		final PrintEvent evt = new PrintEvent(this, action, cr, st,check);

		// store last capture event
		//m_last_capture = evt;
         //System.out.println("Listener "+m_listener);
		if (null == m_listener || null == action || action.equals(""))
			return;

		// invoke listener on EDT thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				m_listener.actionPerformed(evt);
			}
		});
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
                System.out.println(s);
				if(s.equals("cancel")){
					//EnrollPanel.Run(m_reader);
					//try{
					//m_reader.Close();
					//EnrollPanel.Run(m_reader,"cancel");
					//}catch(UareUException e){
						
					
					NotifyListener(ACT_CANCEL, null, null, m_clipboardcheck);
					
				}else if(s.equals("start")){
					//m_reader = m_collection.get(0);
					//EnrollPanel.Run(m_reader,"");
					//try {
					//	m_reader.Open(Reader.Priority.EXCLUSIVE);
					//	} catch (UareUException e) {
					//		MessageBox.DpError("Reader.Open()", e);
					
					NotifyListener(ACT_START, null, null, m_clipboardcheck);
				
				}else if(s.equals("identify")){
                                   // NotifyListener(ACT_IDENTIFY, null, null, m_clipboardcheck);
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
                
              
	
	}