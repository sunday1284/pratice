import { defineStore}  from "pinia";


import type { GetUsersRes, CreateUserReq } from 'src/api/user.type';
import { httpClient } from 'src/api';

/*
*  User는 응답 배열의 아이템 타입
* */
export type User = GetUsersRes[number];

export const useUserStore = defineStore('user', {
  state: () => ({
    users: [] as User[],
    loading: false as boolean,
    error: '' as string
  }),
  getters: {
    // ex) 이름만 모아서 문자열 배열로
    userNames: (state) => state.users.map(u => u.name)
  },
  actions: {
    async fetchUsers(page=1, limit=10) {
      this.loading = true;
      this.error = ''
      try {
        const res = await httpClient.get<User[]>(
          '/users',
          { params: { page, limit } }
        )
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


      /** (선택) 한 명 생성하고 목록 다시 불러오기 */
      async createUser(payload: CreateUserReq) {
        this.loading = true
        this.error = ''
        try {
          await httpClient.post<{ id: string; createdAt: string }, CreateUserReq>(
            '/api/users',
            payload
          )
          // 성공하면 목록 갱신
          await this.fetchUsers()
        }
        catch (err: unknown) {
          if (err instanceof Error) {
            this.error = err.message;
          } else {
            this.error = String(err);
          }
        }
        finally {
          this.loading = false
        }

    }
  }
})
