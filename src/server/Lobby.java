package server;

import database.Queries;
import general.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import server.*;


/**
 * Main class for the game
 * @author Team Athens
 *
 */

public class Lobby {
    private Map<User, ObjectOutputStream> userList;
    private String lobbyTopic;
    public boolean isStarted;
    private boolean inRound;
    private boolean firstChoose;
    private boolean isCountDownStart;
    private boolean breakGameFlag;
    private Object lock = new Object();

    private List<Question> questions;
    private Map<User, Integer> choices;
    public Map<User, Integer> scores;
    private Map<User, Double> factor;
    private int p;

    private Timer t = new Timer();
    private Queries db;

    // Configuration

    private int roundDuration;
    private int roundBreak;
    private int gameStartCountDown;
    private int lobbyMaxPlayers;

    // Configuration



    // **INNER TIMER CLASS** //

    private long start = 0;
    private long end = 0;
    private boolean timerFlag = false;

    /**
     * Start the stop watch
     */
    private void startTimer(){
        timerFlag = true;
        start = System.nanoTime();
    }

    /**
     * STOP!
     * @return the interval
     * @throws Exception if the stop watch wasn't started
     */
    private double endTimer() throws Exception{
        if(timerFlag){
            end = System.nanoTime();
            return (end - start)/1000.0/1000.0/1000.0;
        }else
            throw new Exception();
    }

    // **INNER TIMER CLASS** //

    public Lobby(String lobbyTopic, int roundDuration, int roundBreak, int gameStartCountDown, int lobbyMaxPlayers)
    throws IOException {
        this.userList = new HashMap<>();
        this.questions = new ArrayList<Question>();
        this.lobbyTopic = lobbyTopic;
        this.isStarted = false;
        this.inRound = false;
        this.isCountDownStart = false;
        this.firstChoose = false;
        this.breakGameFlag = false;
        this.choices = new HashMap<>();
        this.scores = new HashMap<>();
        this.factor = new HashMap<>();
        this.p = 0;
        this.roundDuration = roundDuration;
        this.roundBreak = roundBreak;
        this.gameStartCountDown = gameStartCountDown;
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.db = new Queries();
    }

    public Map getUserList(){
        return this.userList;
    }

    /**
     * put a user into a lobby
     * @param user is the user that request joining a game
     * @param topic is the topic that user choose
     * @param toClient is the ObjectOutputStream of this user
     * @return
     */
    public boolean joinUser(User user, String topic, ObjectOutputStream toClient){
        if((!isStarted) && lobbyTopic.equals(topic) && this.userList.size() < 5){
            try{
                //TODO maybe add synchronized lock here
                if(this.containUser(user)){
                    return false;
                }
                userList.put(user, toClient);
                User[] us = this.userList.keySet().toArray(new User[0]);
                broadcast(new Message(Server.PLAYER_JOINED, null, "Player joined", null, null, -1, us), user);
                sendToUser(new Message(Server.QUEUEING, null, "welcome", null, null, -1, us), user);
                return true;
            }catch(Exception e) {
                e.printStackTrace();
            }
            finally{
                // After every user joins, check if the lobby should start the game
                checkStart();
            }
        }
        return false;
    }

    /**
     * remove one user from the looby
     * if the user num == 0, the destroy this lobby
     * @param user is the user being removed
     */
    public void removeUser(User user){
        if(this.isStarted)
            broadcast(new Message(Server.PLAYER_QUIT, user, "Player " + user.getUsername() +" has escaped the lobby", null, null,-1, null), null);
        else
            broadcast(new Message(Server.PLAYER_QUIT, user, "Player " + user.getUsername() +" has left the lobby", null, null,-1, null), null);

        for(Iterator<User> it = userList.keySet().iterator();it.hasNext();){
            User u = it.next();
            if(user.equals(u))
                it.remove();
        }
        if(userList.size()==0){
            if(this.isStarted){
                t.cancel();
            }
            Server.gameList.remove(this);
        }
        if(userList.size()==1&&!isCountDownStart){
            if(this.isStarted){
                t.cancel();
                breakGameFlag = true;
                endRound();
            }
        }
        if(userList.size()==1&&isCountDownStart){
            t.cancel();
            t = new Timer();
            User[] us = this.userList.keySet().toArray(new User[0]);
            broadcast(new Message(Server.QUEUEING, user, "Someone quits in the countdown", null, null, -1, us), null);
        }
    }

    /**
     * Check if a user is in the lobby
     * @param user is the user being checked
     * @return a boolean value indicates that if the user is in the lobby
     */
    public boolean containUser(User user){
        for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();)
            if(it.next().getUsername().equals(user.getUsername()))
                return true;
        return false;
    }

    /**
     * Called after each user joins the lobby
     * Check if the game could start now
     */
    public void checkStart(){
//        synchronized (lock){
//            if(userList.size() == 2 ){
//                t.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        isStarted = true;
//                        for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
//                            scores.put(it.next(), 0);
//                        }
//                        try{
//                            questions = db.createQuiz(lobbyTopic, 0);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//
//                        startGame();
//                    }
//                }, 60 * 1000);
//            }
//        }

        if(userList.size()==lobbyMaxPlayers){
            this.isStarted = true;
            this.isCountDownStart = true;
            broadcast(new Message(Server.GAME_COUNTDOWN, null, null, null, null, -1, null), null);
            for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
                scores.put(it.next(), 0);
            }
            try{
                questions = db.createQuiz(lobbyTopic, 1);
                ;
            }catch(Exception e){
                e.printStackTrace();
            }
            t.cancel();
            t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    isCountDownStart = false;
                    startGame();
                }
            }, gameStartCountDown);
        }
    }

    /**
     * Round start handling function
     */
    public void startGame() {
        Message question = new Message(Server.Q_AND_A, null, null, questions.get(p).question, questions.get(p).answers, questions.get(p).answer);
        broadcast(question, null);
        this.inRound = true;
        t.cancel();
        t = new Timer();
        startTimer(); // Start the inner timer
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                endRound();
            }
        }, this.roundDuration);
    }

    /**
     * Answer handler function
     * @param user is the user who is giving a choice
     * @param answer is the answer that user has given
     */
    public void acceptChoice(User user, int answer){
        if(!inRound)
            return;

        double fac = 0.0;
        try{
            fac = endTimer();
        }catch(Exception e){
            System.out.println("You didn't start the inner timer");
        }

        User u = findUser(user);
        
        synchronized (lock){
        	if(choices.containsKey(u)) {
                sendToUser(new Message(Server.OPTION_TAKEN, user, null, null, null, -1, null), user);
                return;
        	} else if(!this.firstChoose) {
                this.firstChoose = true;
                this.choices.put(u, answer);
                this.factor.put(u, ((double)(roundDuration) - 1000.0 * fac)/(double)(roundDuration));
                broadcast(new Message(Server.FIRST_PLAYER_CLAIMED, user, null, null, null, answer, null), null);
/*                t.cancel();
                t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        endRound();
                    }
                }, 5 * 1000);*/
            }
            else{
                synchronized (lock){
                    if(this.choices.containsValue(answer))
                        sendToUser(new Message(Server.OPTION_TAKEN, user, null, null, null, -1, null), user);
                    else{
                        this.choices.put(user, answer);
                        this.factor.put(user, ((double)(roundDuration) - fac)/(double)(roundDuration));
                        broadcast(new Message(Server.PLAYER_CLAIMED, user, null, null, null, answer, null), null);
                        if(choices.size()==userList.size()){
                            t.cancel();
                            t = new Timer();
                            endRound();
                        }
                    }
                }
            }
        }
    }

    /**
     * Round ending handling function
     */
    public void endRound(){
        synchronized (lock){
            this.firstChoose = false;
            this.inRound = false;
            for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
                User u = it.next();
                if(choices.containsKey(u)){
                    if(choices.get(u).equals(questions.get(p).answer)){
                        double f = -99999;
                        for(Iterator<User> it2 = factor.keySet().iterator(); it2.hasNext(); ){
                            User u2 = it2.next();
                            if(u2.equals(u)){
                                f = factor.get(u2);
                            }

                        }
                        scores.put(u, (int)(scores.get(u) + 100.0 * f));
                    }
                    else{
                        scores.put(u, (scores.get(u) - 50));
                    }
                }
            }
            //tell players that this round has finished
            broadcast(new Message(Server.UPDATE, this.scores), null);
            this.choices.clear();
            this.factor.clear();
            this.p++;
        }
        if(p>=questions.size()||breakGameFlag){
            scores = sortScoreMap(scores);
            int bonus = 200;
            for(Iterator<User> it = scores.keySet().iterator(); it.hasNext();){
                User u = it.next();
                try{
                    if(this.containUser(u)){
                        scores.put(u, scores.get(u)+bonus);
                        bonus/=2;
                        if(db.updateScoreAndRank(u.getUsername(), scores.get(u), this.lobbyTopic))
                            Server.setUserRank(u, u.getIntegerRank() + 1);
                        Server.setTotalScore(u, db.getTotalScore(u.getUsername()));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            broadcast(new Message(Server.RESULTS, this.scores),null);
            this.isStarted = false;

            // End this lobby
            // Maybe a thread lock is needed here
            this.userList.keySet().forEach(u->{Server.setUserStatus(u, User.IDLE);});
            Server.gameList.remove(this);

            this.scores.clear();
            this.choices.clear();
            this.factor.clear();
            p = 0;
            return;
        }
        else{
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    startGame();
                }
            }, roundBreak);
        }
    }


    /**
     * Send users message in the same lobby
     * There are two kinds of broadcasting mode
     * 1. Simply broadcasting to every player in the lobby
     * 2. Broadcasting to every player other than a specific user
     * @param toSend the message is being sent
     * @param user is to indicate the broadcast mode
     */
    public void broadcast(Message toSend, User user){
        //Broadcast message
        if(user == null){
            for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
                try{
                    ObjectOutputStream temp = userList.get(it.next());
                    temp.writeObject(toSend);
                    temp.reset();
                }catch(Exception e){
                    System.out.println("Client disconnects");
                }
            }
        }
        // Broadcast other than the current user
        else{
            for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
                User u = it.next();
                if(u.equals(user))
                    continue;
                try{
                    userList.get(u).writeObject(toSend);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Client disconnects");
                }
            }
        }
    }

    /**
     * Send message to a specific user
     * @param toSend is the messgae being sent
     * @param user is the user being sent to
     */
    public void sendToUser(Message toSend, User user){
        for(Iterator<User> it = userList.keySet().iterator(); it.hasNext();){
            User u = it.next();
            if(u.equals(user)){
                try{
                    userList.get(u).writeObject(toSend);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Client disconnects");
                }
            }
        }
    }

    /**
     * Find a user in the user list in the lobby
     * @param user is the user being found
     * @return the user object that might contain the user
     */
    private User findUser(User user){
        synchronized (lock){
            for(Iterator<User> it = this.userList.keySet().iterator(); it.hasNext();){
                User u = it.next();
                if(u.equals(user))
                    return u;
            }
        }
        return null;
    }

    /**
     * Sort the final score map based on the score
     * @param scores
     * @return
     */
    public Map<User, Integer> sortScoreMap(Map scores){
        Map<User, Integer> result = new LinkedHashMap<>();

        int maxScore = -9999;
        User u = null;

        while(scores.size()>0){
            for(Iterator<Map.Entry<User, Integer>> it = scores.entrySet().iterator(); it.hasNext();){
                Map.Entry<User, Integer> entry = it.next();
                if(entry.getValue()>maxScore){
                    maxScore = entry.getValue();
                    u = entry.getKey();
                }
            }
            result.put(u, maxScore);
            scores.remove(u);
            u = null;
            maxScore = -9999;
        }

        return result;
    }

}
