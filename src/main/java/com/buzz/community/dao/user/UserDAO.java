package com.buzz.community.dao.user;

import com.buzz.community.dto.user.req.RequestJoinDTO;
import com.buzz.community.dto.user.rsp.UserInfoDTO;

public interface UserDAO {

    // · User 테이블에 데이터 Insert 합니다.
    // [Parameter]  JoinDTO (byte[] userId, String username, String bCryptPassword, String name)
    // [Response]   none
    void join(RequestJoinDTO dto);

    // · User 테이블에서 UserId 를 조회합니다.
    // [Parameter]  String username
    // [Response]   ResponseLoginDTO (byte[] userId, String bCryptPassword)
    UserInfoDTO login(String username);

    // . User 테이블에서 ID 의 존재 유무를 확인합니다.
    // [Parameter]  String username
    // [Response]   boolean (true 중복 / false 신규)
    boolean checkUsernameExists(String username);

}
