package portscanner;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class PortScanner defines whether given host ports are open or close
 * 
 * @author Artyom Tsanda
 * @version 1.0
 */
public class PortScanner {

	private static final int MIN_PORT_NUM = 0;
	private static final int MAX_PORT_NUM = 65535;

	/**
	 * defines whether given host port is open
	 * 
	 * @param host
	 * @param port
	 * @return true if port is open<br/>
	 *         false in other cases
	 */
	public boolean isOpen(String host, int port) {
		boolean ret;
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 100);
			ret = true;
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	/**
	 * Prints out to console status of host ports
	 * 
	 * @param host
	 * @param portBegin
	 * @param portEnd
	 * @throws IllegalArgumentException
	 */
	public void printHostPortsInf(String host, int portBegin, int portEnd)
			throws IllegalArgumentException {
		// checking arguments
		if (portBegin < PortScanner.MIN_PORT_NUM || portBegin > PortScanner.MAX_PORT_NUM)
			throw new IllegalArgumentException("Invalid initial port value");
		if (portEnd < PortScanner.MIN_PORT_NUM || portEnd > PortScanner.MAX_PORT_NUM)
			throw new IllegalArgumentException("Invalid final port value");
		if (portBegin > portEnd)
			throw new IllegalArgumentException("Initial port value can't be bigger than final one");

		InetSocketAddress addr = new InetSocketAddress(host, portBegin);
		System.out.println("Scanning ports of "
				+ addr.getAddress().getHostName() + " ("
				+ addr.getAddress().getHostAddress() + ")");
		System.out.println("From " + portBegin + " to " + portEnd);

		System.out.format("%-30s%-50s\n", "Port number", "Open/close");

		for (int i = portBegin; i <= portEnd; i++)
			System.out.format("%-30s%-50s\n", i, (isOpen(host, i) ? ("open")
					: ("close")));

		System.out.println("Scanning finished");
	}

	/**
	 * Prints out to console help information
	 */
	public void printHelp() {
		System.out
				.println("---------------------------------HELP---------------------------");
		System.out
				.println("[Host_name] [port_begin] [port_end] - command to scan host ports");
		System.out
				.println("In case of problems with internet connection and other errors   ");
		System.out.println("All port or ports will be marked as \"closed\"");
	}

	public static void main(String[] args) {

		PortScanner a = new PortScanner();
		try {
			a.printHostPortsInf(args[0], Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));
		} catch (Exception e) {
			System.out.println(e.toString());
			a.printHelp();
		}

	}
}
