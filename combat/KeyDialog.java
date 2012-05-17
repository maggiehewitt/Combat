package combat;

/**
 * @author DevC
 * @version $Id: KeyDialog.java,v 1.5 2012/04/08 03:50:18 DevA Exp $
 * 
 *          KeyDialog allows users the option to map key commands to their
 *          liking
 * 
 *          Revision History:
 *          $Log: KeyDialog.java,v $
 *          Revision 1.5 2012/04/08 03:50:18 DevA
 *          Cleaned up the code to run with Java 1.6: removed unused imports,
 *          fixed some UI focus issues (introduced by new focus "features" in
 *          Java since
 *          our original implementation), and made the CommandInterpreter not a
 *          Singleton
 * 
 *          Revision 1.4 2003/05/30 18:46:45 DevB
 *          Reformatted.
 * 
 *          Revision 1.3 2000/05/09 04:14:16 DevC
 *          worked further; replaced textFields w/ specialized
 *          keyBoxes, and implemented the listeners
 * 
 *          Revision 1.2 2000/05/09 02:22:35 DevC
 *          implemented the createDialog method to build the dialog;
 *          set up the stubs for the button listeners
 * 
 *          Revision 1.1 2000/05/09 01:33:39 DevC
 *          Initial revision
 * 
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyDialog extends JFrame {
    private static final long serialVersionUID = -1;

    public static final String BINDINGS_FILE = "bindings.properties";

    KeyBox p1Up;
    KeyBox p1Down;
    KeyBox p1Right;
    KeyBox p1Left;
    KeyBox p1Fire;

    KeyBox p2Up;
    KeyBox p2Down;
    KeyBox p2Right;
    KeyBox p2Left;
    KeyBox p2Fire;

    JPanel mainPanel;

    private Properties bindings = new Properties();
    private CommandInterpreter ci;

    /**
     * Creates this dialog.
     * 
     * @param mainFrame The frame of the game (which I am part of).
     */
    public KeyDialog(CommandInterpreter ci) {
        // call the parent constructor
        super("Key Mapping");
        this.ci = ci;

        // Make exiting possible
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // fill the frame with the fields, and then construct it
        createDialog();

        // Load existing bindings
        loadExistingBindings();
    }

    /**
     * Handles building the dialog box.
     */
    public void createDialog() {
        // create the components and add them to the panel
        mainPanel = new JPanel();

        p1Up = new KeyBox(this.bindings, "p1-up");
        p1Down = new KeyBox(this.bindings, "p1-down");
        p1Right = new KeyBox(this.bindings, "p1-right");
        p1Left = new KeyBox(this.bindings, "p1-left");
        p1Fire = new KeyBox(this.bindings, "p1-fire");

        p2Up = new KeyBox(this.bindings, "p2-up");
        p2Down = new KeyBox(this.bindings, "p2-down");
        p2Right = new KeyBox(this.bindings, "p2-right");
        p2Left = new KeyBox(this.bindings, "p2-left");
        p2Fire = new KeyBox(this.bindings, "p2-fire");

        mainPanel.setLayout(new GridLayout(6, 3));

        // add the test fields to the panel
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel("Player 1"));
        mainPanel.add(new JLabel("Player 2"));
        mainPanel.add(new JLabel("Up"));
        mainPanel.add(p1Up);
        mainPanel.add(p2Up);
        mainPanel.add(new JLabel("Down"));
        mainPanel.add(p1Down);
        mainPanel.add(p2Down);
        mainPanel.add(new JLabel("Left"));
        mainPanel.add(p1Left);
        mainPanel.add(p2Left);
        mainPanel.add(new JLabel("Right"));
        mainPanel.add(p1Right);
        mainPanel.add(p2Right);
        mainPanel.add(new JLabel("Fire"));
        mainPanel.add(p1Fire);
        mainPanel.add(p2Fire);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        // ActionListener cbl = new CancelButtonListener();
        // ActionListener obl = new OKButtonListener();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentBindings();
                publishBindings();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadExistingBindings();
            }
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        paint(getGraphics());
    }

    public void loadExistingBindings() {
        File bindingsFile = new File(BINDINGS_FILE);

        try {
            Reader bindingsReader = new BufferedReader(new FileReader(bindingsFile));
            this.bindings.load(bindingsReader);
            bindingsReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.p1Up.setKeyCode(Integer.parseInt(this.bindings.getProperty("p1-up")));
        this.p1Down.setKeyCode(Integer.parseInt(this.bindings.getProperty("p1-down")));
        this.p1Left.setKeyCode(Integer.parseInt(this.bindings.getProperty("p1-left")));
        this.p1Right.setKeyCode(Integer.parseInt(this.bindings.getProperty("p1-right")));
        this.p1Fire.setKeyCode(Integer.parseInt(this.bindings.getProperty("p1-fire")));

        this.p2Up.setKeyCode(Integer.parseInt(this.bindings.getProperty("p2-up")));
        this.p2Down.setKeyCode(Integer.parseInt(this.bindings.getProperty("p2-down")));
        this.p2Left.setKeyCode(Integer.parseInt(this.bindings.getProperty("p2-left")));
        this.p2Right.setKeyCode(Integer.parseInt(this.bindings.getProperty("p2-right")));
        this.p2Fire.setKeyCode(Integer.parseInt(this.bindings.getProperty("p2-fire")));
    }

    public Map<Integer, String> getBindings() {
        Map<Integer, String> mapping = new HashMap<Integer, String>();

        mapping.put(this.p1Up.getKeyCode(), "p1-up");
        mapping.put(this.p1Down.getKeyCode(), "p1-down");
        mapping.put(this.p1Left.getKeyCode(), "p1-left");
        mapping.put(this.p1Right.getKeyCode(), "p1-right");
        mapping.put(this.p1Fire.getKeyCode(), "p1-fire");

        mapping.put(this.p2Up.getKeyCode(), "p2-up");
        mapping.put(this.p2Down.getKeyCode(), "p2-down");
        mapping.put(this.p2Left.getKeyCode(), "p2-left");
        mapping.put(this.p2Right.getKeyCode(), "p2-right");
        mapping.put(this.p2Fire.getKeyCode(), "p2-fire");

        return mapping;
    }

    private void publishBindings() {
        ci.useKeyMap(getBindings());
    }

    public void saveCurrentBindings() {
        File bindingsFile = new File(BINDINGS_FILE);

        Writer writer = null;

        try {
            writer = new PrintWriter(bindingsFile);
            this.bindings.store(writer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTestingMode() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
