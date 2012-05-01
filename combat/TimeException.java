package combat;
/** 
 * @author	DevB (DevB)
 * @version	$Id: TimeException.java,v 1.5 2012/04/08 03:50:18 DevA Exp $
 *
 * Revision History:
 *   $Log: TimeException.java,v $
 *   Revision 1.5  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.4  2003/05/30 18:59:20  DevB
 *   Reformatted.
 *
 *   Revision 1.3  2000/05/06 19:52:42  DevB
 *   Fixed javadoc class header.
 *
 *   Revision 1.2  2000/05/06 19:42:20  DevB
 *   dos to unix
 *
 *   Revision 1.1  2000/05/06 17:11:49  DevB
 *   Initial revision
 *
 */

/**
 * This class encapsulates the time related exceptions that can be thrown
 * in the system.
 */

public class TimeException extends Throwable {
	private static final long serialVersionUID = -1;

    public static TimeException TimeStop =
        new TimeException("Time Stop Exception");

    public static TimeException NegativeDelay =
        new TimeException("Negative Delay Time");

    private TimeException( String exceptionMsg ) {
        super( exceptionMsg );
    }
}
