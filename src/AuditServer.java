import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.File;

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
    	  System.out.println("ERROR: " + e.getMessage());
    	  System.exit(-1);
      }
       
    }
    
    public int chckSSHD() {
    	// This method checks to make sure SSH is configured for a bit more security
		try {
    		FileInputStream fstream = new FileInputStream("/etc/ssh/sshd_config");
    		DataInputStream in = new DataInputStream(fstream);
    			BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		String line;
    		int portStatus = 1;
    		int prlStatus = 1;
    		int paStatus = 1;
    		int pepStatus = 1;
    		int craStatus = 1;
    		int upStatus = 1;
    		int agStatus = 1;
    		
    		// walk through buffer and find the line that assigns the value to the Port variable
    		// and converts it to an integer then returns it.
    		while ((line = br.readLine()) != null) {
    			if (line.startsWith("Port")) {
    			  int p = Integer.parseInt(line.substring(5).trim());
    			  if (p == 5511) {
    				portStatus = 0;
    			  }
    			} else if (line.startsWith("PermitRootLogin")) {
    				String prl = line.substring(16).trim();
    				if (prl.equalsIgnoreCase("no")) {
    					prlStatus = 0; 
    				}
    			} else if (line.startsWith("PasswordAuthentication")) {
    				String pa = line.substring(23).trim();
    				if (pa.equalsIgnoreCase("no")) {
    					paStatus = 0;
    				}
    			} else if (line.startsWith("PermitEmptyPasswords")) {
    				String pep = line.substring(21).trim();
    				if (pep.equalsIgnoreCase("no")) {
    					pepStatus = 0;
    				}
    			} else if (line.startsWith("ChallengeResponseAuthentication")) {
    				String cra = line.substring(32).trim();
    				if (cra.equalsIgnoreCase("no")) {
    					craStatus = 0;
    				}
    			} else if (line.startsWith("UsePAM")) {
    				String up = line.substring(7).trim();
    				if (up.equalsIgnoreCase("no")) {
    					upStatus = 0;
    				}
    			} else if (line.startsWith("AllowGroups")) {
    				String ag = line.substring(11).trim();
    				if (ag.equalsIgnoreCase("avetti-users")) {
    					agStatus = 0;
    				}
    			}
    			
    		}

    		in.close();
    		if (portStatus == 0 && prlStatus == 0 && paStatus == 0 && pepStatus == 0 && craStatus == 0 && upStatus == 0 && agStatus == 0) {
    			return 0;
    		} else {
    			return 1;
    		}
    		
    	} catch (IOException e) {
			return 1;
    	}
    }
    
    public int chckDHosts() {
    	// Checks to see if denyhosts is available and if so checks specific settings for us
    	// we also confirm that our trusted networks are set as not to be locked out.
    	
    	try {
    		FileInputStream fstream = new FileInputStream("/etc/denyhosts.conf");
    		DataInputStream in = new DataInputStream(fstream);
            	BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int wdStatus = 1;
            String wd = null;
            
            while ((line = br.readLine()) != null) {
            	if (line.startsWith("WORK_DIR")) {
            		wd = line.substring(10).trim();
            		wdStatus = 0;
            	}
            }
            in.close();
            
            if (wdStatus == 0) {
            	FileInputStream dh = new FileInputStream(wd + "/allowed-hosts");
            	DataInputStream dhIn = new DataInputStream(dh);
            		BufferedReader dhBr = new BufferedReader(new InputStreamReader(dhIn));
            	String dhLine;
            
            	wdStatus = 1;
            	while ((dhLine = dhBr.readLine()) != null) {
            		if (dhLine.trim().equalsIgnoreCase("200.46.29.66")) {
            			wdStatus = 0;
            		}
            		if (dhLine.trim().equalsIgnoreCase("123.71.192.76")) {
            			wdStatus = 0;
            		}
            	}
    		}
            
            if (wdStatus == 0) {
            	return 0;
            } else {
            	return 1;
            }
    	} catch (IOException e) {
    		return 1;
        }
    }
}