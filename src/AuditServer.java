import java.io.*;
import java.util.ArrayList;


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
}