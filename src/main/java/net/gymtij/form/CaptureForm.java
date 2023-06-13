package net.gymtij.form;

import java.awt.*;
import java.awt.event.*;
import java.util.Base64;

import javax.swing.*;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;

import net.gymtij.ConsultaSocio;
import net.gymtij.GuardaHuella;
import net.gymtij.Huella;

public class CaptureForm 
	extends JDialog  
{
	private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
	public JLabel picture = new JLabel();
	public JTextField socio = new JTextField();
	private JTextField nombreSocio = new JTextField();
	private String[] dedos = {"Pulgar Derecho", "Indice Derecho", "Medio Derecho", "Anular Derecho", "Menique Derecho", "Pulgar Izquierdo", "Indice Izquierdo", "Medio Izquierdo", "Anular Izquierdo", "Menique Izquierdo"};
	private JComboBox dedo = new JComboBox(dedos);
	private JTextField prompt = new JTextField();
	private JTextArea log = new JTextArea();
	private JTextField status = new JTextField();


	public String BuscaSocio(String idSocio) {
		String consultaSocio = ConsultaSocio.consultaSocio(Integer.parseInt(idSocio));
		return consultaSocio.toString();
	}

    public CaptureForm(Frame owner) {
        super (owner, true);
        setTitle("Enrolamiento de Huellas");

		setLayout(new BorderLayout());
		rootPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		picture.setPreferredSize(new Dimension(240, 300));
		picture.setBorder(BorderFactory.createLoweredBevelBorder());

		prompt.setColumns(40);
		prompt.setMaximumSize(prompt.getPreferredSize());
		prompt.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Prompt:"),
					BorderFactory.createLoweredBevelBorder()
				));

		log.setColumns(40);
		log.setEditable(false);
		log.setFont(UIManager.getFont("Panel.font"));
		JScrollPane logpane = new JScrollPane(log);
		logpane.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Status:"),
					BorderFactory.createLoweredBevelBorder()
				));
		
		status.setEditable(false);
		status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		status.setFont(UIManager.getFont("Panel.font"));
		
		socio.setColumns(8);
		socio.setEditable(true);
		socio.setFont(UIManager.getFont("Panel.font"));
		socio.setMaximumSize(prompt.getPreferredSize());
		socio.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Credencial:"),
					BorderFactory.createLoweredBevelBorder()
				));

		
		dedo.setEditable(true);
		dedo.setFont(UIManager.getFont("Panel.font"));
		dedo.setMaximumSize(prompt.getPreferredSize());
		dedo.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Dedo:"),
					BorderFactory.createLoweredBevelBorder()
				));

		nombreSocio.setColumns(20);
		nombreSocio.setEditable(false);
		nombreSocio.setFont(UIManager.getFont("Panel.font"));
		nombreSocio.setMaximumSize(prompt.getPreferredSize());
		nombreSocio.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Nombre:"),
					BorderFactory.createLoweredBevelBorder()
				));
		
		

		JButton registra = new JButton("Registra");
		registra.setEnabled(false);
		registra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
			Integer dedoEnroll = dedo.getSelectedIndex() + 1;
			

			DPFPTemplate  template = ((MainForm)getOwner()).getTemplate();
			

			if ( !socio.getText().equals("") && template != null ) {
				byte[] bytes = template.serialize();
				
				String base64 = Base64.getEncoder().encodeToString(bytes);

				System.out.println("Dedo "+dedoEnroll);
			System.out.println("Socio "+socio.getText());
			System.out.println("Base64 "+base64);
				 GuardaHuella.guardaHuella(Integer.parseInt(socio.getText()), dedoEnroll, base64);
				 JOptionPane.showMessageDialog(CaptureForm.this, "Guardado.", "Enrolamiento", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(CaptureForm.this, "Proporciona toda la información.", "Enrolamiento", JOptionPane.ERROR_MESSAGE);
			}

			// registra.setEnabled(false);

			}});


			JButton consulta = new JButton("Consulta");
		consulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setVisible(true); 				
				nombreSocio.setText(BuscaSocio(socio.getText().toString()));
				registra.setEnabled(true);
				// System.out.println("NOMBRE del socio " + socio.getText().toString() + "*****");
			}

			}
			);
		JButton quit = new JButton("Close");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { setVisible(false); }});

		
		JPanel center = new JPanel(new BorderLayout());
		center.setBackground(Color.getColor("control"));
		center.add(dedo, BorderLayout.PAGE_START);
		center.add(picture, BorderLayout.LINE_START);
		center.add(status, BorderLayout.PAGE_END);

		JPanel right = new JPanel(new BorderLayout());
		right.setBackground(Color.getColor("control"));
		right.add(prompt, BorderLayout.NORTH);
		right.add(logpane, BorderLayout.CENTER);
		
		JPanel busca = new JPanel(new BorderLayout());
		busca.setBackground(Color.getColor("control"));
		busca.add(socio, BorderLayout.LINE_START);
		busca.add(nombreSocio, BorderLayout.CENTER);
		busca.add(consulta, BorderLayout.LINE_END);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBackground(Color.getColor("control"));
		bottom.add(registra);
		bottom.add(quit);

		setLayout(new BorderLayout());
		add(busca, BorderLayout.PAGE_START);
		add(center, BorderLayout.LINE_START);
		add(right, BorderLayout.LINE_END);
		add(bottom, BorderLayout.PAGE_END);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override public void componentShown(ComponentEvent e) {
				init();
				start();
			}
			@Override public void componentHidden(ComponentEvent e) {
				stop();
			}
			
		});
		
		pack();
        setLocationRelativeTo(null);
	}

	protected void init()
	{
		capturer.addDataListener(new DPFPDataAdapter() {
			@Override public void dataAcquired(final DPFPDataEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("Huella capturada.");
					setPrompt("Escanea otra huella.");
					process(e.getSample());
				}});
			}
		});
		capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			@Override public void readerConnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
		 			makeReport("El lector de huella esta activo.");
				}});
			}
			@Override public void readerDisconnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("El lector de huella se ha desconectado.");
				}});
			}
		});
		capturer.addSensorListener(new DPFPSensorAdapter() {
			@Override public void fingerTouched(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("Se ha activado el lector de huella.");
				}});
			}
			@Override public void fingerGone(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					makeReport("Se ha quitado el dedo del lector.");
				}});
			}
		});
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			@Override public void onImageQuality(final DPFPImageQualityEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
						makeReport("La calidad de la huella es adecuada.");
					else
						makeReport("La calidad de la huella es baja.");
				}});
			}
		});
	}

	protected void process(DPFPSample sample)
	{
		// Draw fingerprint sample image.
		drawPicture(convertSampleToBitmap(sample));
	}

	protected void start()
	{
		capturer.startCapture();
		setPrompt("Utiliza el lector para registrar tus huellas (4)");
	}

	protected void stop()
	{
		capturer.stopCapture();
	}

	public void setStatus(String string) {
		status.setText(string);
	}
	public void setPrompt(String string) {
		prompt.setText(string);
	}
	
	public JTextField getSocio() {
		return socio;
	}

	public void setNombreSocio(String string) {
		nombreSocio.setText(string);
	}

	public void setSocio(String string) {
		socio.setText(string);
	}

	public void makeReport(String string) {
		log.append(string + "\n");
	}
	
	public CaptureForm() {
		super();
	}
	

	public void drawPicture(Image image) {
		picture.setIcon(new ImageIcon(
			image.getScaledInstance(picture.getWidth(), picture.getHeight(), Image.SCALE_DEFAULT)));
	}
	
	protected Image convertSampleToBitmap(DPFPSample sample) {
		return DPFPGlobal.getSampleConversionFactory().createImage(sample);
	}

	protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose)
	{
		DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
		try {
			return extractor.createFeatureSet(sample, purpose);
		} catch (DPFPImageQualityException e) {
			return null;
		}
	}
	
	
}
