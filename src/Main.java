import java.util.Scanner;

public class Main 
{

	public static void main(String[] args) 
	{
		//	Ask server or client?
		System.out.print("Are you a server? [y/n] ");
		Scanner scanner = new Scanner(System.in);
		
		String resp = scanner.nextLine();
		
		//	If server, listen for ready on state manager
		if(resp.equalsIgnoreCase("y"))
		{
			new Server();
		}
		else
		{
			new Client();
		}
		
			//	If client, connect to server, and send ready signal.

		
		//	
	}

	//	Called by server after 
	public void bootstrap()
	{
		
	}
}
