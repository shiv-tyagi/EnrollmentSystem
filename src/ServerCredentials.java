import java.io.*;
import java.util.*;

public class ServerCredentials {
	public Map<String, String> settings = new HashMap<String, String>();
	
	public ServerCredentials() {
		this.parseFile();
	}
	
	public void parseFile() {
		try {
			Scanner reader = new Scanner(new BufferedReader(new FileReader("db_connect.txt")));
			reader.nextLine();
			while(reader.hasNext()) {
				String[] tokens = reader.nextLine().split(" ");
				if(tokens[0].equalsIgnoreCase("eof"))
						break;
				settings.put(tokens[0].toLowerCase(), tokens[1].trim());	
			}
			reader.close();
			
		} catch (IOException e) {
			System.out.print("Fatal! Error opening db_connect.txt!");
			System.exit(1);
		}
	}
}
