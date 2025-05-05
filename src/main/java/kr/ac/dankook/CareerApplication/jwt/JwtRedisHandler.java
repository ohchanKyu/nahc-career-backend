package kr.ac.dankook.CareerApplication.jwt;

import kr.ac.dankook.CareerApplication.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static kr.ac.dankook.CareerApplication.jwt.JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRedisHandler implements RefreshTokenRepository {

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void save(String userId,String refreshToken){

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        String saveUserId = "NaHC-Career"+userId;
        String existingToken = operations.get(saveUserId);
        if (existingToken != null && !existingToken.isEmpty()) {
            log.info("Update RefreshToken. User Id - {} Delete Token - {}", saveUserId, existingToken);
            delete(saveUserId);
        }
        operations.set(saveUserId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("Save Refresh Token. User Id - {} Save Token - {}", saveUserId, refreshToken);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Optional<String> findByUserId(String userId){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String saveUserId = "NaHC-Career"+userId;
        String refreshToken = valueOperations.get(saveUserId);
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void delete(String userId){
        redisTemplate.delete("NaHC-Career"+userId);
    }
}
