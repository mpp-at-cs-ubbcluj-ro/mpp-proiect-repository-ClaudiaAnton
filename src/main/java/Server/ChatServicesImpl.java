package Server;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.service.ArbitruService;
import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatServicesImpl implements IChatServices {

    private final ArbitruService arbitruService;
    private final ParticipantService participantService;
    private final ProbaService probaService;
    private final RezultatService rezultatService;
    private final Map<Long, IChatObserver> loggedClients;


    public ChatServicesImpl(ArbitruService arbitruService,ParticipantService participantService,ProbaService probaService,RezultatService rezultatService){
        this.arbitruService=arbitruService;
        this.participantService=participantService;
        this.probaService=probaService;
        this.rezultatService=rezultatService;
        loggedClients=new ConcurrentHashMap<>();

    }

    public Arbitru getArbitru(String st){
        return arbitruService.getArbitruByUsername(st);
    }

    public synchronized void login(Arbitru user, IChatObserver client) throws ChatException {
        var arb=arbitruService.getArbitruByUsername(user.getUsername());
        System.out.println(arb.getId());
        if (arb!=null){
            if(loggedClients.get(arb.getId())!=null)
                throw new ChatException("User already logged in.");
            loggedClients.put(arb.getId(), client);
            System.out.println("s-a logat"+arb.getUsername());
        }else
            throw new ChatException("Authentication failed.");
    }

    private final int defaultThreadsNo=5;

    public synchronized void sendPunctaj(Rezultat rezultat) throws ChatException {
        System.out.println("am ajuns in ChatServInp");
        rezultatService.addRezultat(rezultat);

        Iterable<Arbitru> friends=arbitruService.getAll();
        System.out.println("Logged "+friends);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Arbitru us :friends){
            IChatObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId());
                        chatClient.messageReceived(rezultat);
                    } catch (ChatException e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();

    }

    public synchronized void logout(Arbitru user, IChatObserver client) throws ChatException {
        IChatObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ChatException("User "+user.getId()+" is not logged in.");
    }

    public synchronized Arbitru[] getLoggedFriends() throws ChatException {
        Set<Arbitru> result=new TreeSet<Arbitru>();
        Iterable<Arbitru> friends=arbitruService.getAll();
        for (Arbitru friend : friends){

            if (loggedClients.containsKey(friend.getId())){
                System.out.println("are");
                result.add(friend);
            }
        }
        System.out.println(result.size());
        return result.toArray(new Arbitru[result.size()]);
    }

}
