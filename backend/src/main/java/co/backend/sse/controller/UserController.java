package co.backend.sse.controller;

import co.backend.sse.dto.User;
import co.backend.sse.repository.UserRepository;
import co.backend.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final SseService sseService; // SSE 서비스 주입

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // 프론트엔드에서 SSE 연결을 요청하는 주소
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {
        return sseService.createEmitter(userId);
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> payload) {
        User newUser = new User(null, payload.get("name"), payload.get("email"));
        User savedUser = userRepository.save(newUser);

        // ✨ 데이터 변경 후 SSE 이벤트 발생
        sseService.broadcastEvent("usersUpdated", "사용자 추가됨");

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);

        // ✨ 데이터 변경 후 SSE 이벤트 발생
        sseService.broadcastEvent("usersUpdated", "사용자 삭제됨");

        return ResponseEntity.noContent().build();
    }

    // PATCH (수정)

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @RequestBody Map<String, String> payload
    ){
        return userRepository.findById(id)
                .map(existingUser -> {
                    // 요청에 'name' 필드가 있으면 업데이트 함
                    if (payload.containsKey("name")){
                        existingUser.setName(payload.get("name"));
                    }
                    // 요청이 'email' 필드가 있으면 업데이트
                    if (payload.containsKey("email")){
                        existingUser.setEmail(payload.get("email"));
                    }
                    //변경된 정보로 사용자를 저장합니다.
                    User updatedUser = userRepository.save(existingUser);

                    //데이터 변경 후 SSE 이벤트를 발생
                    sseService.broadcastEvent("userUpdated", "사용자 정보 수정됨");

                    return ResponseEntity.ok(updatedUser);
                })
                // 사용자를 찾지 못하면 404 not found
                .orElse(ResponseEntity.notFound().build());
    }
}
