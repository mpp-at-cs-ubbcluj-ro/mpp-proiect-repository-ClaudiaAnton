package Server;

import com.mpp.ProtoBuff.protobuffprotocol.utils.AbstractServer;
import com.mpp.ProtoBuff.protobuffprotocol.utils.ChatProtobuffConcurrentServer;
import com.mpp.networkUtils.ObjectConcurrentServer;
import com.mpp.repository.ArbitruRepo;
import com.mpp.repository.ParticipantRepo;
import com.mpp.repository.ProbaRepo;
import com.mpp.repository.RezultatRepo;
import com.mpp.service.ArbitruService;
import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import com.mpp.services.IChatServices;

import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;


public class StartObjectServer {
    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();

            Properties props = new Properties();
            try {
                props.load(new FileReader("bd.config"));
            } catch (
                    IOException e) {
                System.out.println("Cannot find bd.config " + e);
            }

            Properties serverProps=new Properties();
            try {
                serverProps.load(StartObjectServer.class.getResourceAsStream("/chatserver.properties"));
                System.out.println("Server properties set. ");
                serverProps.list(System.out);
            } catch (IOException e) {
                System.err.println("Cannot find chatserver.properties "+e);
                return;
            }

            ArbitruRepo arbitruRepo = new ArbitruRepo(props);
            ArbitruService arbitruService = new ArbitruService(arbitruRepo);

            ParticipantRepo participantRepo = new ParticipantRepo(props);
            ParticipantService participantService = new ParticipantService(participantRepo);

            ProbaRepo probaRepo = new ProbaRepo(props);
            ProbaService probaService = new ProbaService(probaRepo);

            RezultatRepo rezultatRepo = new RezultatRepo(props);
            RezultatService rezultatService = new RezultatService(rezultatRepo);

            IChatServices chatServerImpl = new ChatServicesImpl(arbitruService, participantService, probaService, rezultatService);
            int chatServerPort = defaultPort;
            try {
                chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
            } catch (NumberFormatException nef) {
                System.err.println("Wrong  Port Number" + nef.getMessage());
                System.err.println("Using default port " + defaultPort);
            }

            System.out.println("Starting server on port: " + chatServerPort);
            AbstractServer server = new ChatProtobuffConcurrentServer(chatServerPort, chatServerImpl);

            try {
                server.start();
            } catch (com.mpp.ProtoBuff.protobuffprotocol.utils.ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());

            }

    }
}
