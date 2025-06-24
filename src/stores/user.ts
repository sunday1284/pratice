import { defineStore } from 'pinia';

import type { GetUsersRes, CreateUserReq } from 'src/api/user.type';
import { httpClient } from 'src/api';

// User 타입은 GetUsersRes 배열의 요소 하나를 의미함
export type User = GetUsersRes[number];

/**
 *  사용자 정보를 관리하는 Pinia 스토어 정의
 * @type {StoreDefinition<'user', {users: User[], loading: boolean, error: string}, {userNames: (state) => any}, {fetchUsers(page?: number, limit?: number): Promise<void>, createUser(payload: CreateUserReq): Promise<void>}>}
 */
export const useUserStore = defineStore('user', {
  // state: 저장할 데이터 정의 (컴포넌트의 data() 비슷)
  state: () => ({
    users: [] as User[], //사용자 목록 (User 타입의 배열)
    loading: false as boolean, // API 호출 중 상태를 나타내는 로딩 플래그
    error: '' as string, // 에러 메시지를 저장할 문자열
  }),
  // getters : 계산된 상태 (computed와 비슷한 개념)
  getters: {
    // 사용자 이름만 모아서 배열로 변환하는 getter
    userNames: (state) => state.users.map((u) => u.name),
  },
  // actions: 비동기 작업이나 복잡한 로직을 담당 (methods와 비슷)
  actions: {
    //사용자 목록을 가져오는 함수
    async fetchUsers(page = 1, limit = 10) {
      this.loading = true; //로딩 시작
      this.error = ''; // 에러 초기화
      try {
        // 서버로부터 사용자 목록을 GET 요청
        const res = await httpClient.get<User[]>('/users', { params: { page, limit } });
        this.users = res.data;
      } catch (err: unknown) {
        if (err instanceof Error) {
          this.error = err.message;
        } else {
          this.error = String(err);
        }
      } finally {
        this.loading = false;
      }
    },

    /**
     *  새로운 사용자를 생성하고 목록을 새로고침함
     * @param payload 목록 새로고침
     * @returns {Promise<void>}
     */
    async createUser(payload: CreateUserReq) {
      this.loading = true;
      this.error = '';
      try {
        // 사용자 생성 요청 (POST 요청)
        await httpClient.post<{ id: string; createdAt: string }, CreateUserReq>(
          '/users',  // API 엔드포인트
          payload, // 보낼 데이터: name, email 등
        );
        // 성공적으로 생성되면 사용자 목록을 다시 불러오기
        await this.fetchUsers();
      } catch (err: unknown) {
        // 에러 처리
        if (err instanceof Error) {
          this.error = err.message;
        } else {
          this.error = String(err);
        }
      } finally {
        this.loading = false;
      }
    },
  },
});
