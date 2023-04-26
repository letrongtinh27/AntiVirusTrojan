package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import virusanalyzer.VirusAnalyzer;

public class Controller implements ActionListener {
	
	MainView view ;
	File file ;
	ArrayList<Object[]> listVirus;
	
	public Controller(MainView view) {
		 this.view = view;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(view.btn_scan_simple == e.getSource()) {
			 file = folderChooser();
			 listVirus = new ArrayList<>();
			 runProgress();
		}
		if(view.btn_scan_all == e.getSource()) {
			scanSystem();
		}
		if(view.btn_quit == e.getSource() || view.jMenuItem_quit == e.getSource()) {
			System.exit(0);
		}
		if(view.btn_view == e.getSource()) {
			updateDataTable(VirusAnalyzer.readVirusFromFile(fileChooser()));
		}
		
		if(view.btn_deltete == e.getSource()) {
			for(int i = 0; i < view.table_view.getRowCount(); i++) {
				Boolean checked = view.table_view.getValueAt(i, 3) != null ? Boolean.valueOf(view.table_view.getValueAt(i, 3).toString()) : false;
				String col = view.table_view.getValueAt(i, 0).toString();
				if(checked) {
					// to do
					System.out.println("Đã chọn " + view.btn_deltete.getText() + " Chọn dòng " + col);
				}
			}
		}
		if(view.btn_isolation == e.getSource()) {
			for(int i = 0; i < view.table_view.getRowCount(); i++) {
				Boolean checked = view.table_view.getValueAt(i, 3) != null 
						? Boolean.valueOf(view.table_view.getValueAt(i, 3).toString()) : false;
				String col = view.table_view.getValueAt(i, 0).toString();
				if(checked) {
					
					//to do 
					System.out.println("Đã chọn " + view.btn_isolation.getText() + " Chọn dòng " + col);
				}
			}
		}
		if(view.btn_update == e.getSource()) {
			//todo update
		}
		
	}
	
	public void scanSystem() {
		File[] roots = File.listRoots();
		this.listVirus = new ArrayList<>();
		for (File file : roots) {
			this.file = file;
			runProgress();
		}
	}
	
	public void updateDataTable(ArrayList<Object[]> listVirus) {
		if(listVirus != null) {
			for (Object[] object : listVirus) {
				view.table_model.addRow(object);		
				view.table_view.revalidate();
				view.repaint();
				view.sp.revalidate();
				view.sp.repaint();
			}
		}
	}
	
	public File fileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select the file to detect");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	public File folderChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select the file to detect");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	public void runProgress() {
	    final JDialog progressDialog = new JDialog(view, "Progress Dialog", true);
	    final JProgressBar progressBar = new JProgressBar(0, 100);
	    final JLabel progressLabel = new JLabel("Please wait...");
	    final JButton cancelButton = new JButton("Cancel");

	    JPanel panel = new JPanel();
	    panel.setLayout(new FlowLayout());
	    panel.add(BorderLayout.CENTER, progressBar);
	    panel.add(BorderLayout.NORTH, progressLabel);
	    panel.add(BorderLayout.SOUTH,cancelButton);

	    progressDialog.add(panel);
	    progressDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    progressDialog.setSize(new Dimension(250, 85));
	    progressDialog.setLocationRelativeTo(view);

	    final SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
	        @Override
	        protected Void doInBackground() throws Exception {
	            int progress = 0;
	            boolean isUpdating = false;
	            while (progress < 100 && !isCancelled()) {
	            	progress++;
	            	 if (!isUpdating) {
		                    updateDataTable(VirusAnalyzer.scanFolder(file));
	                }
		                isUpdating = true;
	                try {
	                	Thread.sleep(10);
                } catch (InterruptedException e) {
                }
	                publish(progress);
	            }
	            return null;
	        }
	        @Override
	        protected void process(List<Integer> chunks) {
	            for (int progress : chunks) {
	                progressBar.setValue(progress);
	                progressLabel.setText("Progress: " + String.valueOf(progress) + "%");
	            }
	        }
	        @Override
	        protected void done() {
	            progressDialog.dispose();
	        }
	    };
	    
	    cancelButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            worker.cancel(true);
	            progressDialog.dispose();
	        }
	    });

	    worker.addPropertyChangeListener(new PropertyChangeListener() {
	        @Override
	        public void propertyChange(PropertyChangeEvent evt) {
	            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
	                progressBar.setValue(100);
	                progressLabel.setText("Complete!");
	            }
	        }
	    });

	    worker.execute();
	    progressDialog.setVisible(true);
	}
}
