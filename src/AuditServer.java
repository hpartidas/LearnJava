import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;

public class AuditServer {
    // AuditServer houses various methods one might need to obtain information from a server's current configuration
   
    public ArrayList<String> displayTmpUsers() {
        // displayTmpUsers basically parses '/etc/passwd' file and checks for any temp user accounts, then returns them.
        try {
            FileInputStream fstream = new FileInputStream("/etc/passwd");
            DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            ArrayList<String> users = new ArrayList<String>();
           
            // Read file line by line, split each line using ':' delimiter and return element 0 if it matches our RegEx
            while ((strLine = br.readLine()) != null) {
                String[] t = strLine.split(":");
               
                if (t[0].matches("^[Tt]mp_.*|[Tt]emp_.*")) {
                    users.add(t[0]);                  
                }
            }
            in.close();
            return users;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
    
    public void delTUsers(String user) {
    	// This method takes in a username and removes it using the Linux userdel command
      String s = null;
      
      try {
    	  Process p = Runtime.getRuntime().exec("/usr/sbin/userdel " + user );
    	  
    	  BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    	  
    	  // Check command output
    	  while ((s = stdInput.readLine()) != null) {
    		  System.out.println(s);
    	  }
    	  
    	  // Check for errors
    	  while ((s = stdError.readLine()) != null) {
    		  System.out.println(s);
    	  }
    	  
      } catch (IOException e) {
    	  System.out.println(e.getStackTrace());
    	  System.exit(-1);
      }
       
    }
    
    public int chckSSHD() {
    	// This method checks to make sure SSH is running on our standard port
		try {
    		FileInputStream fstream = new FileInputStream("/etc/ssh/sshd_config");
    		DataInputStream in = new DataInputStream(fstream);
    			BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		String line;
    		
    		// walk through buffer and find the line that assigns the value to the Port variable
    		// and converts it to an integer then returns it.
    		while ((line = br.readLine()) != null) {
    			if (line.startsWith("Port")) {
    			  int p = Integer.parseInt(line.substring(5).trim());
    			  return p;
    			} else if (line.startsWith("#Port")) {
    				int p = Integer.parseInt(line.substring(6).trim());
    				return p;
    			}
    		}
    		
    	} catch (IOException e) {
			System.err.println("There's been an error: " + e.getMessage());
			System.exit(-1);
    	}
		return 1;
    }
}