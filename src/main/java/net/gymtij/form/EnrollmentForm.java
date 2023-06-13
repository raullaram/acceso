package net.gymtij.form;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;
import java.awt.*;
import java.util.Base64;

import javax.swing.JOptionPane;

public class EnrollmentForm extends CaptureForm
{
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	
	EnrollmentForm(Frame owner) {
		super(owner);
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Enrolamiento de huellas");
		updateStatus();
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);
		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

		// Check quality of the sample and add to enroller if it's good
		if (features != null) try
		{
			makeReport("Configuración activada.");
			enroller.addFeatures(features);		// Add feature set to template.
		}
		catch (DPFPImageQualityException ex) { }
		finally {
			updateStatus();

			// Check if template has been created.
			switch(enroller.getTemplateStatus())
			{
				case TEMPLATE_STATUS_READY:	// report success and stop capturing
					stop();
					((MainForm)getOwner()).setTemplate(enroller.getTemplate());

					DPFPTemplate template = enroller.getTemplate();
					byte[] bytes = template.serialize();
					String string = Base64.getEncoder().encodeToString(bytes);
					setPrompt("Click Close, and then click Fingerprint Verification. Base 64: "+string);
					break;

				case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
					enroller.clear();
					stop();
					updateStatus();
					((MainForm)getOwner()).setTemplate(null);
					JOptionPane.showMessageDialog(EnrollmentForm.this, "La lectura de huella no es adecuada.", "Enrolamiento", JOptionPane.ERROR_MESSAGE);
					start();
					break;
			}
		}
	}
	
	private void updateStatus()
	{
		// Show number of samples needed.
		setStatus(String.format("Huellas por dedo necesarias: %1$s", enroller.getFeaturesNeeded()));
	}
	
}
