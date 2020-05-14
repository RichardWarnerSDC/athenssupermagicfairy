package server;


import java.io.DataInputStream;

public class ServerMonitor implements Runnable{

	public void run(){
		DataInputStream userIn = new DataInputStream(System.in);
		String userInput;
		try{
		while((userInput=userIn.readLine())!=null){
			System.out.println();
			System.out.println("****************SERVER STATUS****************");
			System.out.println("Active user list size: " + Server.activeUserList.size());
			Server.activeUserList.forEach(u->{System.out.println(u.getUsername() + " status: " + u.getStatus() + " rank: " +u.getIntegerRank() + " avatar: " + u.getAvatar() + " score: " + u.getTotalScore());});
			System.out.println("Game list size: " + Server.gameList.size());
			int count = 0;
			for(Lobby i : Server.gameList)
				if(i.isStarted)
					count++;
			System.out.println("Active list size: " + count);
			System.out.println("****************SERVER STATUS****************");
			System.out.println();
			}
		}catch(Exception e){
			;
		}
	}
}
