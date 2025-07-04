package co.backend.sse.repository;

import co.backend.sse.dto.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UserRepository {
    // 동시성 문제를 피하기 위해 CopyOnWriteArrayList 사용
    private final List<User> users = new CopyOnWriteArrayList<>();

    //초기 데이터 설정 (db.json과 유사하게)
    public UserRepository(){
        users.add(new User("hWdDz1I", "테스트", "foo@bar.com"));
        users.add(new User("0678", "테스트", "foo@bar.com"));
        users.add(new User("9bdd", "테스트", "foo@bar.com"));
    }

    public List<User> findAll() {
        return users;
    }

    // id로 사용자를 찾는 메서드
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString().substring(0, 4)); //새 iD 생성
        }else {
            // 기존 사용자 삭제 후 새로 추가 (수정 로직 간소화)
            users.removeIf(u -> u.getId().equalsIgnoreCase(user.getId()));
        }
        users.add(user);
        return user;
    }
    public void deleteById(String id) {
        users.removeIf(u -> u.getId().equalsIgnoreCase(id));
    }
}
