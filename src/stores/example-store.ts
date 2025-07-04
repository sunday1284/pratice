import { defineStore, acceptHMRUpdate } from 'pinia';

export const useCounterStore = defineStore('counter', {
  // ① state(): reactive한 상태를 정의
  state: () => ({
    counter: 0,
  }),
// 2. getters: 계산된 상태(computed) 정의
  getters: {
    doubleCount: (state) => state.counter * 2,
  },
// 3. actions: 상태 변경 로직(메서드) 정의
  actions: {
    increment() {
      this.counter++;
    },
  },
});
// HMR(핫 모듈 교체) 지원
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useCounterStore, import.meta.hot));
}
