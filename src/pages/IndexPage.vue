<template>
  <q-page padding>

    <div class="q-my-md">
      <q-btn label="Load Users" color="primary" @click="loadUsers" />
    </div>

    <!-- users 가 빈 배열이면 no-data-label, 데이터가 들어오면 바로 렌더링됩니다 -->
    <q-table
      :columns="columns"
      :rows="users"
      row-key="id"
      flat
      bordered
      no-data-label="데이터가 없습니다."
    />

  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { QTableColumn } from 'quasar'
import { httpClient } from 'src/api/http'
import type { GetUsersRes, CreateUserReq } from 'src/api/user.type'

// 1) User 타입
type User = GetUsersRes[number]

// 2) 반응형 users 배열 (초기값은 빈 배열)
const users = ref<User[]>([])

// 3) 컬럼 정의
const columns: QTableColumn<User>[] = [
  { name: 'id',    label: 'ID',    field: 'id',    align: 'left', required: true },
  { name: 'name',  label: 'Name',  field: 'name',  align: 'left', required: true },
  { name: 'email', label: 'Email', field: 'email', align: 'left', required: true }
]

// 4) loadUsers: GET /api/users → rows 에 바로 User[] 할당
async function loadUsers() {
  try {
    // GetUsersRes 를 User[] 로 정의했으니 res.data 에 바로 배열이 옵니다
    const res = await httpClient.get<GetUsersRes>('/api/users', {
      params: { page: 1, limit: 10 }
    })
    users.value = res.data

    // POST 테스트(선택)
    const newUser: CreateUserReq = {
      name: '테스트',
      email: 'foo@bar.com'
    }
    const created = await httpClient.post<
      CreateUserReq & { id: string; createdAt: string },
      CreateUserReq
    >('/api/users', newUser)
    console.log('Created:', created.data)

  } catch (err) {
    console.error('API 요청 중 오류 발생:', err)
  }
}
</script>
