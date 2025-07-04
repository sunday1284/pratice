import type { RouteRecordRaw } from 'vue-router';

// 라우트는 해당 페이지로 갈 경우 설정해준다.
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') },
      // 사용자 상세 페이지 라우트
      {
        path: 'users/:id', // /user/123 와 같은 경로
        name: 'UserDetail',
        component: () => import('pages/UserDetailPage.vue'), //상세보기 페이지
      },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
