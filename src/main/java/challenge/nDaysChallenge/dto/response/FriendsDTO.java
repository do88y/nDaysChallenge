//package challenge.nDaysChallenge.dto.response;
//
//import challenge.nDaysChallenge.domain.Relationship;
//import challenge.nDaysChallenge.domain.RelationshipStatus;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//public class FriendsDTO extends Relationship {
//
//    private Long id;                   //친구아이디
//    private String nickname;     //친구 닉네임
//    private LocalDateTime localDateTime;    //친구 등록된 날짜,시간
//    public List<String>  friendsList = Collections.emptyList();
//
//    public Long getId(){
//        return id;
//    }
//
//    public String setNickname(String nickname){
//        return nickname;
//    }
//
//    public LocalDateTime getLocalDateTime(LocalDateTime dateTime){
//        return dateTime;
//    }
//
//    public List<String> friends() {
//        if(RelationshipStatus.ACCEPT ==getStatus()){
//            return friendsList;
//        }
//        return null;
//    }
//}
