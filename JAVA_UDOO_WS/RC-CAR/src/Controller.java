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

public class Controller extends JFrame implements WindowListener,
		ActionListener, KeyListener {

	//Interface Components:
	private Boolean connected = false;
	private JButton UpButton;
	private JButton DownButton;
	private JButton LeftButton;
	private JButton RightButton;
	private JButton ConnectButton;
	private JButton Keyboard;
	private JTextField IpAddress;
	private JTextField Port;
	private JPanel Messagefield;
	private JPanel middlefield;
	private JPanel leftfield;
	private Container contant;
	private JLabel Message;
	
	
	//UDP Client + Key values:
	private RcClient rc;
	private int keyCode;
	private String keyText;
	private boolean Keylistening;

	public Controller() {

		Message=new JLabel("Autor:Kevin Pan | Last Update:27.05.2014");
		
		
		UpButton = new JButton("W");
		UpButton.addActionListener(this);
		DownButton = new JButton("S");
		DownButton.addActionListener(this);
		LeftButton = new JButton("A");
		LeftButton.addActionListener(this);
		RightButton = new JButton("D");
		RightButton.addActionListener(this);
		IpAddress = new JTextField("192.168.0.108");
		Port = new JTextField("8888");

		ConnectButton = new JButton("Connect");
		ConnectButton.addActionListener(this);
		Keyboard = new JButton("Keyboard:Off");
		Keyboard.addKeyListener(this);
		Keyboard.addActionListener(this);
		Keylistening = false;

		middlefield = new JPanel();
		leftfield = new JPanel();
		Messagefield=new JPanel();
		contant = new Container();

		leftfield.setLayout(new GridLayout(3, 1));
		leftfield.add(IpAddress);
		leftfield.add(Port);
		leftfield.add(ConnectButton);

		middlefield.setLayout(new GridLayout(2, 3));
		middlefield.add(new JLabel(""));
		middlefield.add(UpButton);
		middlefield.add(new JLabel(""));
		middlefield.add(LeftButton);
		middlefield.add(DownButton);
		middlefield.add(RightButton);

		Messagefield.add(Message);

		JSplitPane rightsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				middlefield, Keyboard);
		rightsplit.setDividerLocation(150);

		JSplitPane mainsplit2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftfield, rightsplit);
		mainsplit2.setDividerLocation(150);
		JSplitPane mainsplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				mainsplit2,Messagefield);
		mainsplit1.setDividerLocation(250);
		contant = this.getContentPane();
		contant.add(mainsplit1);
		this.addWindowListener(this);
		this.setTitle("Udoo Remote Controller V1.1");
		this.setLocation(500, 250);
		this.setSize(450, 315);
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == Keyboard) {
				if (Keylistening == false) {
					Keyboard.setText("Keyboard:On");
					Keylistening = true;
				}
			}

			else if (e.getSource() == ConnectButton) {
				if (connected == false) {
					String ip = IpAddress.getText();
					int port = Integer.parseInt(Port.getText());
					if (rc == null) {
						System.out.println("Trying to connect to server:" + ip
								+ " with port:" + port + "for the first time");
						rc = new RcClient(ip, port);
						ConnectButton.setText("Disconnect");
						connected = true;
					} else {
						System.out.println("Trying to reconnect to server:"
								+ ip + " with port:" + port);
						rc.reconnect(ip, port);
						ConnectButton.setText("Disconnect");
						connected = true;
					}
				} else if (connected == true) {
					rc.end();
					ConnectButton.setText("Connect");
					connected = false;
				}
			}

			else {
				if (connected != false) {
					if (e.getSource() == UpButton) {
						rc.send("Button:Up");
					}
					if (e.getSource() == DownButton) {
						rc.send("Button:Left");
					}
					if (e.getSource() == LeftButton) {
						rc.send("Button:Left");
					}
					if (e.getSource() == RightButton) {
						rc.send("Button:Left");
					}
				}
			}

			// Check input source to disable the Keyboard
			if (e.getSource() != Keyboard) {
				Keyboard.setText("Keyboard:Off");
				Keylistening = false;
			}
		} catch (IOException e1) {
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.out.println("Programm Ended");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Keylistening == true) {
			keyCode = e.getKeyCode();
			keyText = KeyEvent.getKeyText(keyCode);
			System.out.println("Pressed:" + keyText);
			if (connected == true) {
				try {
					rc.send("Pressed:" + keyText);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (Keylistening == true) {
			keyCode = e.getKeyCode();
			keyText = KeyEvent.getKeyText(keyCode);
			System.out.println("Released:" + keyText);
			if (connected == true) {
				try {
					rc.send("Released:" + keyText);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arge) {

	}

}
