package combat;

/**
 * @author DevC
 * @version $Id: KeyBox.java,v 1.4 2012/04/08 03:50:18 DevA Exp $
 * 
 *          KeyBox holds the keyCode for the most recent input
 * 
 *          Revision History:
 *          $Log: KeyBox.java,v $
 *          Revision 1.4 2012/04/08 03:50:18 DevA
 *          Cleaned up the code to run with Java 1.6: removed unused imports,
 *          fixed some UI focus issues (introduced by new focus "features" in
 *          Java since
 *          our original implementation), and made the CommandInterpreter not a
 *          Singleton
 * 
 *          Revision 1.3 2003/05/30 18:43:30 DevB
 *          Reformatted.
 * 
 *          Revision 1.2 2000/05/09 14:05:46 DevC
 *          tracking bugs
 * 
 *          Revision 1.1 2000/05/09 04:14:09 DevC
 *          Initial revision
 * 
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;

import javax.swing.JTextField;

public class KeyBox extends JTextField {
    private static final long serialVersionUID = -1;

    int keyCode;

    Properties map;

    String id;

    /**
     * Sets the key code for the value in the box.
     * 
     * @param KeyCode The key code value.
     */
    public KeyBox(Properties map, String id) {
        super();

        this.map = map;
        this.id = id;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char kchar = e.getKeyChar();
                int kcode = e.getKeyCode();
                System.out.println(e.getKeyCode());
                setKeyCode(kcode);
            }
        });
    }

    /**
     * Gets the key code for the value in the box.
     * 
     * @return KeyCode
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Sets the key code for the value in the box.
     * 
     * @param KeyCode The key code value.
     */
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        setText((new Integer(keyCode)).toString());
        this.map.put(this.id, Integer.toString(this.keyCode));
    }

    public class TFListener implements KeyListener {
        private KeyBox kb;

        public TFListener(KeyBox kb) {
            this.kb = kb;
        }

        public void keyPressed(KeyEvent e) {

        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {

        }
    }

}
