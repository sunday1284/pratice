<script setup lang="ts">

import type { User } from 'stores/user';
import { ref, watch } from 'vue';

// 부모 컴포넌트로부터 받을 props를 정의함
const props = defineProps<{
  modelValue: boolean; // v-model을 위한 prop
  editUser: User  | null; // 수정할 사용자 정보 (없으면 생성 모드)
}>();
//부모 컴포넌트로 보낼 이벤트 정의
const emit = defineEmits(['update:modelValue', 'submit', 'close']);

//폼 데이터를 위한 반응형 객체
const form = ref({
  name: '',
  email: '',
});

// 'editUser' prop이 변경될 때마다 폼의 내용을 업데이트함
watch(() => props.editUser, (newUser) => {
  if(newUser) {
    //수정 모드일 경우 폼을 기존 사용자 정보를 채움
    form.value = {
      name: newUser.name,
      email: newUser.email,
    }
  }else {
    //업데이트 모드가 아닌 경우 비어둠
    form.value = {
      name: '',
      email: '',
    }
  }
  }, { immediate: true});
// 폼 제출 시 실행되는 함수
  function onSubmit() {
    // 'submit' 이벤트를 발생시켜 폼 데이터를 부모 컴포넌트로 전달합니다.
    emit('submit', { ...form.value });
  }
</script>

<template>
  <q-dialog :model-value="modelValue" @update:model-value="$emit('update:modelValue')" persistent>
    <q-card style="min-width: 350px">
      <q-card-section>
        <div class="text-h6"> {{ editUser ? '사용자 수정' : '사용자 추가 '}}</div>
      </q-card-section>

      <q-form @submit.prevent="onSubmit">
        <q-card-section>
          <q-input
            v-model="form.name"
            label="이름"
            lazy-rules
            :rules="[val => !!val || '이름을 입력해주세요.']"
          />
          <q-input
            v-model="form.email"
            label="이메일"
            type="email"
            class="q-mt-sm"
            lazy-rules
            :rules="[val => !!val || '이메일을 입력해주세요.']"
            />
        </q-card-section>

        <q-card-section align="right">
          <q-btn flat label="취소" color="primary" @click="$emit('close')"/>
          <q-btn flat :label="editUser ? '수정' : '추가'" color="primary" type="submit"/>
        </q-card-section>

      </q-form>
    </q-card>

  </q-dialog>
</template>

<style scoped>




</style>
