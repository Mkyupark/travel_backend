package gs.chippo.travel.sse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository  {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);  // emitter를 저장
    public void saveEventCache(String eventCacheId, Object event); // 이벤트를 저장
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);  // 해당 회원과 관련된 모든 이벤트를 찾음
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);
    public void deleteById(String id);// emitter를 지움
    public void deleteAllEmitterStartWithId(String memberId); // 해당 회원과 관련된 모든 emitter를 지움
    public void deleteAllEventCacheStartWithId(String memberId);
}
