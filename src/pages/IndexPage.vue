<template>
  <q-page padding>

   <div class="q-mb-md">
     <q-btn
      label="Load Users"
      color="primary"
      @click="() => void userStore.fetchUsers()"
      :loading="userStore.loading"
     />
   </div>
  <!-- 에러가 있으면 빨간 텍스트로    -->
  <div v-if="userStore.error" class="text-negative q-mb-md">
    {{userStore.error}}
  </div>

    <!-- users 가 빈 배열이면 no-data-label, 데이터가 들어오면 바로 렌더링됩니다 -->
    <q-table
      :columns="columns"
      :rows="userStore.users"
      row-key="id"
      flat
      bordered
      no-data-label="데이터가 없습니다."
    />
  </q-page>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import {useUserStore, type User} from 'stores/user';
import type { QTableColumn } from 'quasar'
// import type { GetUsersRes, CreateUserReq } from 'src/api/user.type'
// 1) User 타입
// type User = GetUsersRes[number]
//1) store 인스턴스 생성할 때
const userStore = useUserStore()

//userStore -> pinia 사용 x 일 때 코드
// 2) 반응형 users 배열 (초기값은 빈 배열)
// const users = ref<User[]>([])

// 2) 컬럼 정의
const columns: QTableColumn<User>[] = [
  { name: 'id',    label: 'ID',    field: 'id',    align: 'left', required: true },
  { name: 'name',  label: 'Name',  field: 'name',  align: 'left', required: true },
  { name: 'email', label: 'Email', field: 'email', align: 'left', required: true }
]
//3) onMounted 시 한 번 로드
onMounted(async () => {
   await userStore.fetchUsers()
})


// pinia -> UserStore를 사용 안 했을 때의 코드
/*// 4) loadUsers: GET /api/users → rows 에 바로 User[] 할당
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
}*/
</script>
