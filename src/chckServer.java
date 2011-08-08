public class chckServer {
	// Used to audit a server's configuration
    public static void main(String[] args) {
        AuditServer myObj = new AuditServer();
        // Get checkSSHD return value if it is 0 we are ok, if not we need to let the user know.
        int r = myObj.chckSSHD();
        int s = myObj.chckDHosts();
        
    	if (r != 0) {
    		System.out.println("WARNING: SSH not configured properly!");
    	}
    	
    	if (s != 0) {
    		System.out.println("WARNING: Deny Hosts not configured properly!");
    	}
    }
}