<template>
  <q-page padding>
    <div class="q-mb-md row items-center justify-between q-gutter-md">
      <div>
      <q-btn color="primary" label="사용자 추가" @click="openCreateDialog" />
      <q-btn
        v-if="selected.length > 0"
        color = "negative"
        :label="`${selected.length}명 선택 삭제`"
        @click="confirmBulkDelete"
        class="q-ml-sm"
      />
      </div>
      <q-btn
        color="secondary"
        label="Load Users"
        @click="userStore.fetchUsers()"
        :loading="userStore.loading"
      />
    </div>

    <div v-if="userStore.error" class="text-negative q-mb-md">
      {{ userStore.error }}
    </div>

    <q-table
      :columns="columns"
      :rows="userStore.users"
      row-key="id"
      selection="multiple"
      v-model:selected="selected"
      flat
      bordered
      no-data-label="데이터가 없습니다."
      :loading="userStore.loading"
    >

      <template v-slot:body-cell-actions="props">
        <q-td :props="props" class="q-gutter-sm">
          <q-btn
            dense
            round
            flat
            icon="visibility"
            @click="openDetailDialog(props.row)"
            aria-label="View Details"
          />
          <q-btn
            dense
            round
            flat
            color="primary"
            icon="edit"
            @click="openEditDialog(props.row)"
            aria-label="Edit User"
          />
          <q-btn
            dense
            round
            flat
            color="negative"
            icon="delete"
            @click="confirmDelete(props.row)"
            aria-label="Delete User"
          />
        </q-td>
      </template>
    </q-table>

    <UserFormDialog
      v-model="dialog.show"
      :edit-user="dialog.userToEdit"
      @submit="handleSubmit"
      @close="closeDialog"
    />
  </q-page>
  <q-dialog v-model="detailDialog.show">
    <q-card style="width: 500px">
      <q-card-section>
        <div class="text-h6">사용자 상세정보</div>
      </q-card-section>

      <q-list v-if="detailDialog.user" bordered separator>
        <q-item>
          <q-item-section>
            <q-item-label overline>ID</q-item-label>
            <q-item-label>{{detailDialog.user.id}}</q-item-label>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section>
            <q-item-label overline>Name</q-item-label>
            <q-item-label>{{ detailDialog.user.name }}</q-item-label>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section>
            <q-item-label overline>Email</q-item-label>
            <q-item-label>{{detailDialog.user.email}}</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>

      <q-card-section align="right">
        <q-btn flat label="닫기" color="primary" v-close-popup></q-btn>
      </q-card-section>
    </q-card>
  </q-dialog>

  <q-page padding>
    <div class="q-gutter-sm q-mb-md">
      <q-btn label="한국어" @click="setLanguage('ko-KR')"/>
      <q-btn label="English" @click="setLanguage('en-US')"/>
    </div>
    <q-table
      :title="$t('user') + ' ' + $t('details')"
      :rows="userStore.users"
      :columns="columns"
      :no-data-label="t('messages.load_failed')"
    />
  </q-page>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useUserStore, type User } from 'stores/user';
import type { QTableColumn } from 'quasar';
import { useQuasar } from 'quasar';
// 방금 생성한 다이얼로그 컴포넌트를 임포트합니다.
import UserFormDialog from 'components/UserFormDialog.vue';
// 공통 언어 변경 함수
import {useLang} from 'src/composables/useLang';

const {t, setLanguage} = useLang();



const userStore = useUserStore();
const $q = useQuasar(); // Quasar의 다이얼로그와 알림 기능을 사용하기 위함
// ✨ [추가] 선택된 사용자 목록을 담을 ref
const selected = ref<User[]>([]);




// 다이얼로그 상태를 관리하는 객체
const dialog = ref<{
  show: boolean;
  userToEdit: User | null;
}>({
  show: false,
  userToEdit: null,
});

// ✨ [추가] 상세 정보 보기용 다이얼로그 상태
const detailDialog = ref<{
  show: boolean;
  user: User | null;
}>({
  show: false,
  user: null,
});
// 상세 정보 다이얼로그를 여는 함수
function openDetailDialog(user: User){
  detailDialog.value.user = user;
  detailDialog.value.show = true;
}

// ✨ 'Actions' 컬럼을 테이블 정의에 추가
// 'Actions' 컬럼을 테이블 정의에 추가합니다.
const columns: QTableColumn[] = [
  { name: 'id',    label: 'ID',    field: 'id',    align: 'left' },
  { name: 'name',  label: 'Name',  field: 'name',  align: 'left' },
  { name: 'email', label: 'Email', field: 'email', align: 'left' },
  // ✨ 이 줄을 추가해야 합니다.
  { name: 'actions', label: 'Actions', field: '', align: 'center' },
];

// 사용자 추가 다이얼로그 열기
function openCreateDialog() {
  dialog.value.userToEdit = null; // 수정 모드가 아님을 명시
  dialog.value.show = true;
}

// 사용자 수정 다이얼로그 열기
function openEditDialog(user: User) {
  dialog.value.userToEdit = user; // 수정할 사용자 정보 전달
  dialog.value.show = true;
}

// 다이얼로그 닫기
function closeDialog() {
  dialog.value.show = false;
  dialog.value.userToEdit = null;
}

// 폼 제출 처리 (생성 또는 수정)
async function handleSubmit(formData: { name: string; email: string }) {
  if (dialog.value.userToEdit) {
    // 수정 모드일 경우
    await userStore.updateUser(dialog.value.userToEdit.id, formData);
  } else {
    // 생성 모드일 경우
    await userStore.createUser(formData);
  }
  closeDialog(); // 작업 완료 후 다이얼로그 닫기
}

// 사용자 삭제 확인
function confirmDelete(user: User) {
  $q.dialog({
    title: '삭제 확인',
    message: `'${user.name}' 사용자를 정말 삭제하시겠습니까?`,
    cancel: true,
    persistent: true,
  }).onOk(() => {
    // async 함수를 즉시 실행하고 void 연산자로 Promise 반환을 무시
    void (async () => {
      await userStore.deleteUser(user.id);
      $q.notify({
        color: 'positive',
        message: '사용자가 삭제되었습니다.',
        icon: 'check',
      });
    })();
  });
}
// 선택된 사용자들을 일괄 삭제
function confirmBulkDelete(){
  $q.dialog({
    title: '일괄 삭제 확인',
    message: `${selected.value.length}명의 사용자를 정말 삭제하시겠습니까?`,
    cancel: true,
    persistent: true,
  }).onOk(()=>{
    void (async () => {
      //선택된 모든 사용자에 대해 삭제 요청을 동시에 보냄
      const deletePromises = selected.value.map(user => userStore.deleteUser(user.id));
      await  Promise.all(deletePromises);

      $q.notify({
        color: 'positive',
        message: `${selected.value.length}명의 사용자가 삭제되었습니다.`
      });
      // 삭제 후 선택 목록을 비움
      selected.value = [];
    })(); // ()를 추가하는 이유는 "이 레시피대로 당장 요리를 시작하라는 명령"이기 때문이다.
  })
}
// 컴포넌트가 마운트될 때 사용자 목록을 불러옵니다.
  onMounted(async () => {
    await userStore.fetchUsers();
    //2. 사용자 정보 변경에 대한 SSE 이벤트 수신 시작
    userStore.listenForUserUpdates();
  });


</script>
