package com.buzz.community.dto.user.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestJoinDTO {
    String userId;
    String username;
    String password;
    String bCryptPassword;
    String name;
}
