import java.util.ArrayList;
import java.util.Iterator;

public class delTmpUsers {
    public static void main(String[] args) {
        AuditServer myObj = new AuditServer();
       
        ArrayList<String> x = new ArrayList<String>();
        x = myObj.displayTmpUsers();
        Iterator<String> iterator = x.iterator();
       
        while(iterator.hasNext()) {
        	String u = iterator.next().toString();
        	
        	System.out.println("Deleting: " + u);
        	myObj.delTUsers(u);
        }
    }
}