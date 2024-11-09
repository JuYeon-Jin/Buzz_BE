package com.buzz.community.service.user;

import com.buzz.community.dao.user.UserDAO;
import com.buzz.community.dto.user.req.RequestJoinDTO;
import com.buzz.community.dto.user.req.RequestLoginDTO;
import com.buzz.community.dto.user.rsp.UserInfoDTO;
import com.buzz.community.exception.user.DuplicateUsernameException;
import com.buzz.community.exception.user.InvalidInputException;
import com.buzz.community.exception.user.InvalidPasswordException;
import com.buzz.community.exception.user.UserNotFoundException;
import com.buzz.community.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private JWTUtils token;
    private UserDAO userDAO;


    // TODO 생성자와 Autowired 에 대해 공부 필요
    public UserService(JWTUtils token, UserDAO userDAO) {
        this.token = token;
        this.userDAO = userDAO;
    }


    /**
     * 회원가입 요청을 처리합니다.
     * 입력된 회원 정보를 검증하고, 유효한 경우 회원 정보를 저장한 후 JWT 토큰을 생성하여 반환합니다.
     *
     * @param dto 회원가입 요청 DTO (username, password, name 등의 정보 포함)
     * @return 생성된 JWT 토큰 (유효하지 않은 입력값이 있는 경우 null 반환)
     */
    public String join(RequestJoinDTO dto) {

        if (!validateUsername(dto.getUsername())) {
            throw new InvalidInputException("유효하지 않은 사용자 이름입니다.");
        }
        if (!validatePassword(dto.getPassword())) {
            throw new InvalidInputException("유효하지 않은 비밀번호입니다.");
        }
        if (!validateName(dto.getName())) {
            throw new InvalidInputException("유효하지 않은 닉네임입니다.");
        }

        checkIdDuplicate(dto.getUsername());

        String userId = UUID.randomUUID().toString();
        dto.setUserId(userId);
        dto.setBCryptPassword(encryptPassword(dto.getPassword()));

        userDAO.join(dto);
        String name = dto.getName();

        return token.createToken(userId, name);
    }



    /**
     * 로그인 요청을 처리합니다.
     * 입력된 사용자 이름과 비밀번호를 검증하고, 유효한 경우 JWT 토큰을 생성하여 반환합니다.
     *
     * @param dto 로그인 요청 DTO (username, password 포함)
     * @return 생성된 JWT 토큰 (유효하지 않은 경우 null 반환)
     */
    public String login(RequestLoginDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        UserInfoDTO userInfoDTO = userDAO.login(username);

        if (userInfoDTO == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. 사용자 ID: " + username + "에 해당하는 사용자가 존재하지 않습니다.");
        }

        if (!checkPassword(password, userInfoDTO.getBCryptPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }


        String name = userInfoDTO.getName();
        return token.createToken(userInfoDTO.getUserId(), name);
    }



    /**
     * 제공된 평문 문자열을 BCrypt 해싱 알고리즘을 사용하여 암호화합니다.
     *
     * @param password 암호화할 평문 비밀번호.
     * @return BCrypt로 해시된 비밀번호.
     */
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }



    /**
     * 평문 비밀번호가 해시된 비밀번호와 일치하는지 확인합니다.
     *
     * @param plainTextPassword 평문 비밀번호.
     * @param encryptPassword   해시된 비밀번호
     * @return 비밀번호 일치 여부 (true: 일치, false: 불일치).
     */
    public boolean checkPassword(String plainTextPassword, String encryptPassword) {
        return BCrypt.checkpw(plainTextPassword, encryptPassword);
    }



    /**
     * 입력값의 길이를 검증합니다.
     *
     * @param value 검증할 값
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @return 입력값이 최소 길이 이상, 최대 길이 이하인지 여부
     */
    public boolean validateLength(String value, int minLength, int maxLength) {
        return value.length() >= minLength && value.length() <= maxLength;
    }



    /**
     * username 입력값을 검증합니다.
     * 4자 이상 11자 이하, 영문 소문자, 숫자, 하이픈, 밑줄만 허용합니다.
     *
     * @param value 검증할 String 문자
     * @return 유효한 username 인지 여부
     */
    public boolean validateUsername(String value) {
        return value.matches("^[a-z0-9\\-_]+$") && validateLength(value, 4, 11);
    }



    /**
     * password 입력값을 검증합니다.
     * password 는 4자 이상 11자 이하, 영문 소문자, 숫자, 특수문자를 허용하며,
     * 동일한 문자가 3번 이상 연속으로 나올 수 없습니다.
     *
     * @param value 검증할 String 문자
     * @return 유효한 password 인지 여부
     */
    public boolean validatePassword(String value) {
        return value.matches("^[a-z0-9!\"#$%&'()*+,\\-./:;<=>?@\\\\^_`{|}~]+$")
                && validateLength(value, 4, 11)
                && !value.matches("(.)\\1{2,}");  // 동일한 문자가 3번 이상 연속되지 않아야 함
    }



    /**
     * name 입력값을 검증합니다.
     * name 은 2자 이상 5자 이하, 한글, 영문 소문자, 숫자 허용를 허용합니다.
     *
     * @param value 검증할 String 문자
     * @return 유효한 name 인지 여부
     */
    public boolean validateName(String value) {
        return value.matches("^[a-z0-9가-힣]+$") && validateLength(value, 2, 5);
    }



    /**
     * username 이 중복되어 있는지 검증합니다.
     * 중복이라면 true, 아니라면 false 를 반환합니다.
     *
     * @param username 검증할 String 문자
     * @return 중복된 username 인지 여부
     */
    public boolean checkIdDuplicate(String username) {
        if (userDAO.checkUsernameExists(username)) {
            throw new DuplicateUsernameException("이미 존재하는 ID 입니다.");
        }
        return false;
    }

}
