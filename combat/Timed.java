package combat;
/**
 * @author DevA
 * @version $Id: Timed.java,v 1.5 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: Timed.java,v $
 *   Revision 1.5  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.4  2003/05/30 18:57:59  DevB
 *   Reformatted.
 *
 *   Revision 1.3  2000/05/05 20:02:28  DevA
 *   Just some header cleanup.
 *
 *   Revision 1.2  2000/05/04 00:58:28  DevA
 *   Fits a nice looking template now.
 */

//Source file: Timed.java
/**
 * Just a simple interface which all timed objects in the system must
 * implement.
 */
public interface Timed {
    
    /**
     * Executes all pretick actions.
     */
    void pretick();
    
    /**
     * Executes all tick actions.
     */
    void tick();

}//class Timed
