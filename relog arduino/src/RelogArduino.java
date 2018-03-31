import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.fazecast.jSerialComm.SerialPort;
import javax.swing.JTextField;

public class RelogArduino {
	
	static SerialPort chosenPort;

	public static void main(String[] args) {
		
		// Crea y configura la ventana
		JFrame window = new JFrame();
		window.setTitle("Arduino Hora, mensajes y temperatura");
		window.setSize(800, 100);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Crea el drop box y el boton conectar, y los coloca hasta arriba
		JComboBox<String> portList = new JComboBox<String>();
		JButton connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
                JTextField txtcampo  = new JTextField ("",20);
                JButton btnenviar = new JButton("Enviar");
                JButton btncancelar = new JButton("Desconectar");
                JButton btntemperatura = new JButton("Temperatura");
		topPanel.add(portList);
		topPanel.add(connectButton);
                topPanel.add(txtcampo);
                topPanel.add(btnenviar);
                topPanel.add(btncancelar);
                topPanel.add(btntemperatura);
		window.add(topPanel, BorderLayout.NORTH);
		
		// Se asigna el drop box
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());
		
		// Configura el boton conectar y el hilo para enviar al serial
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(connectButton.getText().equals("Connect")) {
					// Se adjunta el numero de puerto al serial
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						connectButton.setEnabled(false);
                                                btnenviar.setEnabled(false);
                                                btntemperatura.setEnabled(false);
						portList.setEnabled(false);
						
						// Crea un nuevo hilo y enviar al arduino
						Thread thread = new Thread(){
							@Override public void run() {
								// Espera el booteo
								try {Thread.sleep(100); } catch(Exception e) {}

								//Ingresa al loop del arduino
								PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
								while(true) {
									output.print(new SimpleDateFormat("hh:mm:ss a     MMMMMMM dd, yyyy").format(new Date()));
									output.flush();
									try {Thread.sleep(100); } catch(Exception e) {}
								}
							}
						};
						thread.start();
					}
				} else {
					// Desconecta el puerto serial
					chosenPort.closePort();
					portList.setEnabled(true);
					
				}
			}
		});
		
                btnenviar.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(btnenviar.getText().equals("Enviar")) {
					// Empareja el serial puerto
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						btnenviar.setEnabled(false);
                                                btntemperatura.setEnabled(false);
                                                connectButton.setEnabled(false);
						portList.setEnabled(false);
						
						// Crea un nuevo hilo y lo envia al arduino
						Thread thread = new Thread(){
							@Override public void run() {
								// Espera la conexion
								try {Thread.sleep(100); } catch(Exception e) {}

								// Ingresa al loop del arduino
								PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
								while(true) {
									output.print(txtcampo.getText());
									output.flush();
									try {Thread.sleep(100); } catch(Exception e) {}
								}
							}
						};
						thread.start();
					}
				} 
			}
		});
                
                btncancelar.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
			
					// Desconecta los puertos seriales
					chosenPort.closePort();
					portList.setEnabled(true);
					btnenviar.setEnabled(true);
                                        btntemperatura.setEnabled(true);
				        connectButton.setEnabled(true);
                                        
			}
		});
                
                btntemperatura.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
			    Tempreatura tm = new Tempreatura();
                            tm.setVisible(true);
					
					
				
			}
		});
                
                
                
		// Muestra la ventana
		window.setVisible(true);
	}

}
