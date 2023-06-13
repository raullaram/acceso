package net.gymtij.form;

import java.awt.*;
import java.awt.event.*;
// import java.util.Base64;

import javax.swing.*;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;

import net.gymtij.ConsultaSocio;
// import net.gymtij.GuardaHuella;
// import net.gymtij.Huella;

public class ValidateForm 
	extends JDialog  
{
	private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
	public JLabel picture = new JLabel();
	private JTextField prompt = new JTextField();
	private JTextArea log = new JTextArea();
	private JTextField status = new JTextField();
	protected JButton reloadFingerprints = new JButton("Actualizar huellas");


	public String BuscaSocio(String idSocio) {
		String consultaSocio = ConsultaSocio.consultaSocio(Integer.parseInt(idSocio));
		return consultaSocio.toString();
	}

    public ValidateForm(Frame owner) {
        super (owner, true);
        setTitle("Enrolamiento de Huellas");

		setLayout(new BorderLayout());
		rootPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		picture.setPreferredSize(new Dimension(240, 280));
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
		
	
		
		 
        

		JButton quit = new JButton("Close");
        quit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) { setVisible(false); }});

		
		JPanel center = new JPanel(new BorderLayout());
		center.setBackground(Color.getColor("control"));
		center.add(picture, BorderLayout.CENTER);
		center.add(status, BorderLayout.PAGE_END);

		JPanel right = new JPanel(new BorderLayout());
		right.setBackground(Color.getColor("control"));
		right.add(prompt, BorderLayout.NORTH);
		right.add(logpane, BorderLayout.CENTER);
		
		
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBackground(Color.getColor("control"));
		bottom.add(reloadFingerprints);
		bottom.add(quit);

		setLayout(new BorderLayout());
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
		 			makeReport("El lector de huella esta activo para validar.");
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
		setPrompt("Utiliza el lector para validar el acceso");
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
	
	public void makeReport(String string) {
		log.append(string + "\n");
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
