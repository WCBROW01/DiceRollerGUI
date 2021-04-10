import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 * D&D dice roller Swing application
 * @author Will Brown
 * @version 1.0-alpha
 */
@SuppressWarnings("serial")
public class DiceRollerGUI extends JFrame {
	
	private int modifier;

	// GUI Stuff
	private JPanel contentPane, inputPanel, outputPanel;
	private JLabel diceSidesLabel, diceNumLabel, modLabel, outputLabel;
	private JTextField diceNumField, diceSidesField, modField;
	private JScrollPane scrollPane;
	private JTextArea outputArea;
	private JButton rollButton;
	private JCheckBox genSeparatelyBox;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DiceRollerGUI frame = new DiceRollerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DiceRollerGUI() {
		setTitle("Dice Roller");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));
		
		inputPanel = new JPanel();
		contentPane.add(inputPanel);
		inputPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		diceNumLabel = new JLabel("Number of Dice");
		inputPanel.add(diceNumLabel);
		
		diceNumField = new JTextField();
		diceNumLabel.setLabelFor(diceNumField);
		inputPanel.add(diceNumField);
		diceNumField.setColumns(10);
		
		diceSidesLabel = new JLabel("Number of Sides");
		inputPanel.add(diceSidesLabel);
		
		diceSidesField = new JTextField();
		diceSidesLabel.setLabelFor(diceSidesField);
		inputPanel.add(diceSidesField);
		diceSidesField.setColumns(10);
		
		modLabel = new JLabel("Modifier");
		inputPanel.add(modLabel);
		
		modField = new JTextField();
		modLabel.setLabelFor(modField);
		inputPanel.add(modField);
		modField.setColumns(10);
		
		rollButton = new JButton("Roll");
		rollButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If I don't want a modifier, I shouldn't have to enter one.
				modifier = 0;
				
				// If an error is found in the text boxes, show this dialog box.
				if (findRollErrors()) JOptionPane.showMessageDialog(getParent(), "Please enter valid values.", "Unable to roll", JOptionPane.ERROR_MESSAGE);
				else generateDiceRolls();
			}
		});
		inputPanel.add(rollButton);
		
		outputPanel = new JPanel();
		contentPane.add(outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWidths = new int[]{0, 0};
		gbl_outputPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_outputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_outputPanel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		outputPanel.setLayout(gbl_outputPanel);
		
		outputLabel = new JLabel("Output");
		GridBagConstraints gbc_outputLabel = new GridBagConstraints();
		gbc_outputLabel.insets = new Insets(0, 0, 5, 0);
		gbc_outputLabel.gridx = 0;
		gbc_outputLabel.gridy = 0;
		outputPanel.add(outputLabel, gbc_outputLabel);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		outputPanel.add(scrollPane, gbc_scrollPane);
		
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		scrollPane.setViewportView(outputArea);
		
		genSeparatelyBox = new JCheckBox("Generate individual rolls");
		genSeparatelyBox.setToolTipText("The modifier will be added to each individual roll if this is checked.");
		GridBagConstraints gbc_genSeparatelyBox = new GridBagConstraints();
		gbc_genSeparatelyBox.gridx = 0;
		gbc_genSeparatelyBox.gridy = 2;
		outputPanel.add(genSeparatelyBox, gbc_genSeparatelyBox);
	}
	
	/**
	 * Generates dice rolls
	 */
	private void generateDiceRolls() {
		Random rng = new Random();
		
		// If the user wants to roll multiple die but not add them up, we need to do something different.
		if (genSeparatelyBox.isSelected()) {
			for (int diceNum = 0; diceNum < Integer.parseInt(diceNumField.getText()); diceNum++) {
				outputArea.append(Integer.toString(rng.nextInt(Integer.parseInt(diceSidesField.getText())) + 1 + modifier) + "\n");
			}
		} else {
			int result = 0;
			
			for (int diceNum = 0; diceNum < Integer.parseInt(diceNumField.getText()); diceNum++) {
				result += rng.nextInt(Integer.parseInt(diceSidesField.getText())) + 1;
			}
			
			outputArea.append(Integer.toString(result + modifier) + "\n");
		}
	}
	
	/**
	 * Error checking algorithm for bad user input.
	 * @return whether an error was found in any values entered by the user
	 */
	private boolean findRollErrors() {
		boolean rollError = false;
		
		try {
			if (diceNumField.getText().length() == 0 || Integer.parseInt(diceNumField.getText()) < 1) {
				throw new NumberFormatException();
			}
			
			diceNumLabel.setForeground(Color.BLACK);
		} catch (NumberFormatException e2) {
			diceNumLabel.setForeground(Color.RED);
			rollError = true;
		}
		
		try {
			if (diceSidesField.getText().length() == 0 || Integer.parseInt(diceSidesField.getText()) < 2) {
				throw new NumberFormatException();
			}
			
			diceSidesLabel.setForeground(Color.BLACK);
		} catch (NumberFormatException e2) {
			diceSidesLabel.setForeground(Color.RED);
			rollError = true;
		}
		
		try {
			if (modField.getText().length() > 0) {
				modifier = Integer.parseInt(modField.getText());
			}
			
			modLabel.setForeground(Color.BLACK);
		} catch (NumberFormatException e2) {
			modLabel.setForeground(Color.RED);
			rollError = true;
		}
		return rollError;
	}
}
