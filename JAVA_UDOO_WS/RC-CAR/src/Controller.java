import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;


public class Controller extends JFrame implements WindowListener,ActionListener,KeyListener{
	
	private Boolean connected = false;
	private JButton UpButton;
	private JButton DownButton;
	private JButton LeftButton;
	private JButton RightButton;
	private JTextField IpAddress;
	private JTextField Port;
	JPanel middlefield=new JPanel();
	JPanel leftfield=new JPanel();
	private JButton ConnectButton;
	private JButton Keyboard;
	Container contant=new Container();
	RcClient rc;
	
	public Controller(){
		
		UpButton=new JButton("W");
		UpButton.addActionListener(this);
		DownButton=new JButton("S");
		DownButton.addActionListener(this);
		LeftButton=new JButton("A");
		LeftButton.addActionListener(this);
		RightButton=new JButton("D");
		RightButton.addActionListener(this);
		IpAddress = new JTextField("127.0.0.1");
		Port= new JTextField("8888");
		
		ConnectButton=new JButton("Connect");
		ConnectButton.addActionListener(this);
		Keyboard = new JButton("Keyboard:Off");
		Keyboard.addActionListener(this);
		
		leftfield.setLayout(new GridLayout(3,1));
		leftfield.add(IpAddress);
		leftfield.add(Port);
		leftfield.add(ConnectButton);
		
		
		middlefield.setLayout(new GridLayout(2,3));
		middlefield.add(new JLabel(""));
		middlefield.add(UpButton);
		middlefield.add(new JLabel(""));
		middlefield.add(LeftButton);
		middlefield.add(DownButton);
		middlefield.add(RightButton);
		
		JSplitPane rightsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,middlefield,Keyboard);
		rightsplit.setDividerLocation(150);
		
		JSplitPane mainsplit= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftfield,rightsplit);
		mainsplit.setDividerLocation(150);
		contant=this.getContentPane();
		contant.add(mainsplit);
		this.addWindowListener(this);
		this.setTitle("RC UDOO CONTROLLER");
		this.setLocation(500, 250);
		this.setSize(450,250);
		this.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try{
		if(e.getSource()==Keyboard){
			Keyboard.setText("Keyboard:On");
			Keyboard.addKeyListener(this);
		}
		
		else if(e.getSource()==ConnectButton){
			if(connected==false){
				String ip = IpAddress.getText();
				int port= Integer.parseInt(Port.getText());
				System.out.print("Trying to connect to server:"+ip+" with port:"+port);
				rc = new RcClient(ip,port);
				ConnectButton.setText("Disconnect");
				connected=true;
			}
			else if(connected==true){
					rc.end();
					ConnectButton.setText("Connect");
			}
		}
		
		else if(e.getSource()==UpButton){
			rc.send("Button:Up");
		}
		else if(e.getSource()==DownButton){
			rc.send("Button:Down");
		}
		else if(e.getSource()==LeftButton){
			rc.send("Button:Left");
		}
		else if(e.getSource()==RightButton){
			rc.send("Button:Right");
		}
		
		if (e.getSource()!=Keyboard){
			Keyboard.setText("Keyboard:Off");
			Keyboard.addKeyListener(null);
		}
		
		}
		catch(IOException e1){
			
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    String keyText = KeyEvent.getKeyText(keyCode);
	    System.out.println("Pressed:"+keyText);
	   if(connected==true){
		   try {
				rc.send("Pressed:"+keyText);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	   }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    String keyText = KeyEvent.getKeyText(keyCode);
	    System.out.println("Released:"+keyText);
	   if(connected==true){
		   try {
				rc.send("Released:"+keyText);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	   }
	}

	@Override
	public void keyTyped(KeyEvent arge) {
		
	}

}
