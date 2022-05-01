package com.mpp.ProtoBuff.protobuffprotocol;

import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;

public class ProtoUtils {
    public static ChatProtobufs.ChatRequest createLoginRequest(Arbitru user){
        ChatProtobufs.Arbitru userDTO=ChatProtobufs.Arbitru.newBuilder().setId(String.valueOf(user.getId()))
                .setNume(user.getNume())
                .setParola(user.getParola())
                .setUsername(user.getUsername())
                .setProba(String.valueOf(user.getProba()))
                .build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder().setType(ChatProtobufs.ChatRequest.Type.Login)
                .setUser(userDTO).build();
        return request;
    }
    public static ChatProtobufs.ChatRequest createLogoutRequest(Arbitru user){
        ChatProtobufs.Arbitru userDTO=ChatProtobufs.Arbitru.newBuilder().setId(String.valueOf(user.getId()))
                .setNume(user.getNume())
                .setParola(user.getParola())
                .setUsername(user.getUsername())
                .setProba(String.valueOf(user.getProba()))
                .build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder().setType(ChatProtobufs.ChatRequest.Type.Logout)
                .setUser(userDTO).build();
        return request;
    }

    public static ChatProtobufs.ChatRequest createSendMesssageRequest(Rezultat message){
        ChatProtobufs.Rezultat messageDTO=ChatProtobufs.Rezultat.newBuilder().setProbaID(String.valueOf(message.getProbaID()))
                .setParticipantID(String.valueOf(message.getParticipantID()))
                .setPunctaj(String.valueOf(message.getPuncte()))
                .setId(String.valueOf(message.getId())).build();

        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder()
                .setType(ChatProtobufs.ChatRequest.Type.SendMessage)
                .setMessage(messageDTO).build();
        return request;
    }



    public static ChatProtobufs.ChatResponse createOkResponse(){
        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.Ok).build();
        return response;
    }

    public static ChatProtobufs.ChatResponse createErrorResponse(String text){
        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.Error)
                .setError(text).build();
        return response;
    }



    public static ChatProtobufs.ChatResponse createFriendLoggedInResponse(Arbitru user){
        ChatProtobufs.Arbitru userDTO=ChatProtobufs.Arbitru.newBuilder().setId(String.valueOf(user.getId()))
                .setNume(user.getNume())
                .setParola(user.getParola())
                .setUsername(user.getUsername())
                .setProba(String.valueOf(user.getProba()))
                .build();

        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.FriendLoggedIn)
                .setUser(userDTO).build();
        return response;
    }
    public static ChatProtobufs.ChatResponse createNewMessageResponse(Rezultat message){

        ChatProtobufs.Rezultat messageDTO=ChatProtobufs.Rezultat.newBuilder().setProbaID(String.valueOf(message.getProbaID()))
                .setParticipantID(String.valueOf(message.getParticipantID()))
                .setPunctaj(String.valueOf(message.getPuncte()))
                .setId(String.valueOf(message.getId())).build();

        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.NewMessage)
                .setMessage(messageDTO).build();
        return response;
    }



    public static String getError(ChatProtobufs.ChatResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Arbitru getUser(ChatProtobufs.ChatResponse response){
        Arbitru user=new Arbitru();
        user.setId(Long.valueOf(response.getUser().getId()));
        user.setParola(response.getUser().getParola());
        user.setUsername(response.getUser().getUsername());
        user.setNume(response.getUser().getNume());
        user.setProba(Long.valueOf(response.getUser().getProba()));
        return user;
    }

    public static Rezultat getMessage(ChatProtobufs.ChatResponse response){
        Rezultat message=new Rezultat();
        message.setId(Long.valueOf(response.getMessage().getId()));
        message.setParticipantID(Long.valueOf(response.getMessage().getParticipantID()));
        message.setProbaID(Long.valueOf(response.getMessage().getProbaID()));
        message.setPuncte(Long.valueOf(response.getMessage().getPunctaj()));
        return message;
    }

    public static Arbitru[] getFriends(ChatProtobufs.ChatResponse response){
        Arbitru[] friends=new Arbitru[response.getFriendsCount()];
        for(int i=0;i<response.getFriendsCount();i++){
            ChatProtobufs.Arbitru userDTO=response.getFriends(i);
            Arbitru user=new Arbitru();
            user.setId(Long.valueOf(userDTO.getId()));
            user.setParola(userDTO.getParola());
            user.setUsername(userDTO.getUsername());
            user.setNume(userDTO.getNume());
            user.setProba(Long.valueOf(userDTO.getProba()));
            friends[i]=user;
        }
        return friends;
    }
    public static Arbitru getUser(ChatProtobufs.ChatRequest request){
        Arbitru user=new Arbitru();
//        user.setId(Long.valueOf(request.getUser().getId()));
        user.setParola(request.getUser().getParola());
        user.setUsername(request.getUser().getUsername());
        user.setNume(request.getUser().getNume());
        //user.setProba(Long.valueOf(request.getUser().getProba()));
        return user;
    }

    public static Rezultat getMessage(ChatProtobufs.ChatRequest request){
        Rezultat message=new Rezultat();
        message.setId(Long.valueOf(request.getMessage().getId()));
        message.setParticipantID(Long.valueOf(request.getMessage().getParticipantID()));
        message.setProbaID(Long.valueOf(request.getMessage().getProbaID()));
        message.setPuncte(Long.valueOf(request.getMessage().getPunctaj()));
        return message;
    }

    public static ChatProtobufs.ChatResponse createLoggedFriendsResponse(Arbitru[] friends) {
        ChatProtobufs.ChatResponse.Builder response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.GetLoggedFriends);
        for (Arbitru user: friends){
            ChatProtobufs.Arbitru userDTO=ChatProtobufs.Arbitru.newBuilder().setId(String.valueOf(user.getId()))
                    .setNume(user.getNume())
                    .setParola(user.getParola())
                    .setUsername(user.getUsername())
                    .setProba(String.valueOf(user.getProba()))
                    .build();
            response.addFriends(userDTO);
        }

        return response.build();
    }

    public static ChatProtobufs.ChatRequest createLoggedFriendsRequest() {
        //ChatProtobufs.Arbitru userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder()
                .setType(ChatProtobufs.ChatRequest.Type.GetLoggedFriends)
                .build();
        return request;
    }
}
