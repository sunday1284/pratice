import {defineStore } from 'pinia';
import {ref, computed} from 'vue';

export const useCounterSetupStore = defineStore('counterSetup', () => {
  // reactive state
  const counter = ref(0);

  // computed getter
  const doubleCount = computed(() => counter.value * 2);

  // action
  function increment(){
    counter.value++;
  }

  return {counter, doubleCount, increment};
})
