package org.b_verify.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;

import java.util.List;
import pki.Account;
import pki.PKIDirectory;

/**
 * Sets up layout of server configuration page to sync to b_verify server.
 * 
 * @author Binh
 */
public class BVerifyClientConfigGui {

	protected Shell shell;
	private Display display;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private Text textHost;
	private Text textPort;
	private Text textUserId;
	private boolean configured;
	
	private Account warehouse;
	private List<Account> depositors;
	private PKIDirectory pki;
	
	private final Font sectionHeaderLabelFont = new Font(display, new FontData(".AppleSystemUIFont", 14, SWT.BOLD));
	private final Font subHeaderLabelFont = new Font(display, new FontData(".AppleSystemUIFont", 12, SWT.NORMAL));

	/**
	 * Starts the configuration gui.
	 * @wbp.parser.entryPoint
	 */
	public BVerifyClientConfigGui(Account warehouse, List<Account> depositors, PKIDirectory pki) {
		this.warehouse = warehouse;
		this.depositors = depositors;
		this.pki = pki;
		try {
			this.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 180);
		shell.setText("B_verify Desktop Client");
		
		Label lblUserId = new Label(shell, SWT.NONE);
		lblUserId.setFont(subHeaderLabelFont);
		lblUserId.setBounds(30, 36, 96, 22);
		lblUserId.setText("User Id:");
		
		Label lblHost = new Label(shell, SWT.NONE);
		lblHost.setFont(subHeaderLabelFont);
		lblHost.setBounds(30, 66, 96, 22);
		lblHost.setText("Host:");
		
		Label lblPort = new Label(shell, SWT.NONE);
		lblPort.setFont(subHeaderLabelFont);
		lblPort.setBounds(30, 94, 96, 22);
		lblPort.setText("Port:");
		
		textHost = new Text(shell, SWT.BORDER);
		textHost.setBounds(132, 64, 308, 22);
		
		textPort = new Text(shell, SWT.BORDER);
		textPort.setBounds(132, 92, 308, 22);
		
		textUserId = new Text(shell, SWT.BORDER);
		textUserId.setText("<-- set once the sync is started -->");
		textUserId.setBounds(132, 36, 308, 22);
		
		Button btnStartSync = new Button(shell, SWT.NONE);
		btnStartSync.setFont(subHeaderLabelFont);
		btnStartSync.setBounds(10, 122, 106, 28);
		btnStartSync.setText("Start Sync");
		
		Label lblConfiguration = new Label(shell, SWT.NONE);
		lblConfiguration.setAlignment(SWT.CENTER);
		lblConfiguration.setFont(sectionHeaderLabelFont);
		lblConfiguration.setBounds(20, 10, 420, 22);
		formToolkit.adapt(lblConfiguration, true, true);
		lblConfiguration.setText("CONFIGURATION");
		
		// mark as starting not configured
		configured = false;
		
		Listener startSyncListener = new Listener() {
			public void handleEvent(Event event) {
				if (configured) {
					System.out.println("ALREADY CONFIGURED");
				}
				String host = textHost.getText();
				int port = Integer.parseInt(textPort.getText());
				configured = true;	
				BVerifyClientDemo bverifyclientdemo = new BVerifyClientDemo(warehouse, depositors, host, port);
				BVerifyClientApp bverifyclientapp = new BVerifyClientApp(bverifyclientdemo, pki, warehouse);
				BVerifyClientGui bverifyclientgui = new BVerifyClientGui(bverifyclientapp);
				shell.close();
				bverifyclientgui.openWindow();
				
				Thread tr = new Thread(bverifyclientapp);
				tr.start();
			}
		};
		btnStartSync.addListener(SWT.Selection, startSyncListener);
	}
}