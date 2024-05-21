package gs.chippo.travel.service;

import gs.chippo.travel.entity.UserEntity;
import gs.chippo.travel.exception.ErrorCode;
import gs.chippo.travel.exception.RestApiException;
import gs.chippo.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create (final UserEntity userEntity){
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RestApiException(ErrorCode.FORBIDDEN_ACCESS,"이메일을 입력해주세요.");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new RestApiException(ErrorCode.FORBIDDEN_ACCESS,"이메일이 존재합니다.");
        }
        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {

        final UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser !=null &&
                encoder.matches(password,
                        originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

}
