<script setup lang="ts">

import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import type { User } from 'stores/user';
import { httpClient } from 'src/api';

const route = useRoute();
const user = ref<User | null>(null);
const loading = ref(false);
const error = ref('');
onMounted(async () => {
  const userId = route.params.id as string;
  if (!userId) return;

  loading.value = true;
  error.value = '';
  try {
    // Pinina store를 사용하지 않고 직접 API 호출 -> 상세 페이지는 단일임
    const response = await  httpClient.get<User>(`/users/${userId}`);
    user.value = response.data;
  }catch (err:unknown) {
    if (err instanceof Error) error.value = err.message;
    else error.value = String(err);
  }finally {
    loading.value = false;
  }
});
</script>
<template>
<q-page padding>
  <q-btn flat icon="arrow_back" @click="$router.back()" label="뒤로가기" />
  <div v-if="loading" class="text-center">
    <q-spinner-dots color="primary"  size="40px"/>
  </div>
  <q-card v-else-if="user" flat bordered>
    <q-card-section>
      <div class="text-h6">사용자 상세정보</div>
    </q-card-section>
    <q-list bordered separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>ID</q-item-label>
          <q-item-label> {{user.id}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item>
        <q-item-section>
          <q-item-label overline>Name</q-item-label>
          <q-item-label>{{user.name}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item>
        <q-item-section>
          <q-item-label overline>Email</q-item-label>
          <q-item-label>{{user.email}}</q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-card>
<div v-else class="text-center text-grey"> 사용자 정보를 찾을 수 없습니다.</div>
</q-page>
</template>




<style scoped>

</style>
