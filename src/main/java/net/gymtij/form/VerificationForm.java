package net.gymtij.form;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.verification.*;

// import javafx.event.ActionEvent;
import net.gymtij.Huella;
import net.gymtij.RecuperaHuellas;
import net.gymtij.Torniquete;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class VerificationForm extends ValidateForm {
	private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();

	ArrayList<Huella> huellas = RecuperaHuellas.recuperaHuellas();

	VerificationForm(Frame owner) {
		super(owner);
	}

	@Override
	protected void init() {
		super.init();

		reloadFingerprints.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				huellas = RecuperaHuellas.recuperaHuellas();
				
				JOptionPane.showMessageDialog(VerificationForm.this, "Huellas actualizadas.", "Enrolamiento", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});

			
		this.setTitle("Fingerprint Enrollment");
		updateStatus(0);
	}

	protected boolean processFingerprint(DPFPFeatureSet features, String base64) {

		// System.out.println("huella "+ base64);
		// String base64 =
		// "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";

		byte[] bytes = Base64.getDecoder().decode(base64);
		DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate(bytes);

		// Compare the feature set with our template
		// DPFPVerificationResult result =
		// verificator.verify(features, ((MainForm)getOwner()).getTemplate());

		DPFPVerificationResult result = verificator.verify(features, template);
		updateStatus(result.getFalseAcceptRate());

		// Check if the number is the correct answer
		if (result.isVerified()) {
			return true;
		} else {
			throw new RuntimeException();
		}

	}

	@Override
	protected void process(DPFPSample sample) {
		super.process(sample);

		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

		// Check quality of the sample and start verification if it's good
		if (features != null) {

			new SwingWorker<Boolean, Void>() {

				@Override
				protected Boolean doInBackground() {

					long start = System.currentTimeMillis();
					Boolean isVerified = false;

					// Get the number of cores of the current computer
					int numCores = Runtime.getRuntime().availableProcessors();

					// Create a thread pool with the size of the number of cores
					ExecutorService executor = new ThreadPoolExecutor(numCores, Integer.MAX_VALUE, 1, TimeUnit.DAYS,
							new LinkedBlockingQueue<>());

					// System.out.println("Number of cores: " + numCores);

					// Create a list of tasks to submit to the thread pool
					List<Callable<Boolean>> taskList = new ArrayList<>();

					for (Huella huella : huellas) {

						// Define the method you want to execute
						Callable<Boolean> myMethodTask = new Callable<Boolean>() {
							@Override
							public Boolean call() {

								return processFingerprint(features, huella.huella);
							}
						};

						taskList.add(myMethodTask);
					}

					try {
						// Submit the tasks to the thread pool and get the result of the first completed
						// task
						isVerified = executor.invokeAny(taskList);

						// Shutdown the executor once all tasks are complete
						executor.shutdown();

					} catch (Exception e) {
						// Handle the exception thrown by the task
						// e.printStackTrace();
					}
					System.out.println(System.currentTimeMillis() - start);
					return isVerified;
				}

				protected void done() {
					Boolean isVerified;
					
					try {
						isVerified = get();

						if (isVerified) {
							makeReport("The fingerprint was VERIFIED.");
							if (Torniquete.torniquete()) {
								makeReport("Se activa torniquete.");
								Toolkit.getDefaultToolkit().beep();
							} else {
								makeReport("Falla en torniquete.");
							};
							

						} else {
							makeReport("The fingerprint was NOT VERIFIED.");
						}

					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};

			}.execute();

			// for (int i = 0; i < 400; i++) {

			// String base64 =
			// "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";

			// byte[] bytes = Base64.getDecoder().decode(base64);
			// DPFPTemplate template =
			// DPFPGlobal.getTemplateFactory().createTemplate(bytes);

			// // Compare the feature set with our template
			// // DPFPVerificationResult result =
			// // verificator.verify(features, ((MainForm)getOwner()).getTemplate());

			// DPFPVerificationResult result = verificator.verify(features, template);
			// updateStatus(result.getFalseAcceptRate());

			// if (result.isVerified()) {
			// isVerified = true;
			// break;
			// }

			// }

		}
	}

	private void updateStatus(int FAR) {
		// Show "False accept rate" value
		setStatus(String.format("False Accept Rate (FAR) = %1$s", FAR));
	}

}
