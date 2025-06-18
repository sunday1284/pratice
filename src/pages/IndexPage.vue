<template>
  <q-page padding>
    <q-btn label="Load Users" @click="loadUsers" />
    <div v-if="users.length">
      <div v-for="u in users" :key="u.id">
        {{ u.id }} – {{ u.name }} – {{ u.email }}
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { httpClient } from 'src/api/http'      // ← 직접 httpClient 사용
import type { GetUsersRes, CreateUserReq } from 'src/api/user.type'

const users = ref<GetUsersRes['users']>([])

async function loadUsers() {
  try {
    // ✔️ GET /api/users?page=1&limit=10 은 이제 8080으로 간다
    const res = await httpClient.get<GetUsersRes>('/api/users', {
      params: { page: 1, limit: 10 }
    })

    users.value = res.data.users

    // (선택) 새 사용자 POST 테스트
    const newUser: CreateUserReq = {
      name: '테스트',
      email: 'foo@bar.com'
    }
    const createRes = await httpClient.post<
      CreateUserReq & { id: string; createdAt: string },
      CreateUserReq
    >('/api/users', newUser)

    console.log('Created user:', createRes.data)
  }
  catch (err) {
    // 여기에 들어오면 fetch 자체가 실패한 거니,
    // 대부분 baseURL/prxy 설정이 잘못된 상태일 때입니다.
    console.error('API 요청 중 오류 발생:', err)
  }
}
</script>
