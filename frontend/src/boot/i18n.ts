import { createI18n } from 'vue-i18n';
import { boot } from 'quasar/wrappers';
import messages from 'src/i18n'
const i18n = createI18n({
  locale: 'ko-KR', //기본 언어 설정
  fallbackLocale: 'en-US',  // 기본 언어 설정 실패 시 사용할 언어
  legacy: false, // Composition API와 함께 사용하려면 false로 설정
  messages, // 1단계에서 만든 번역 메시지
})
export default  boot(({ app  }) => {
  app.use(i18n);
});

export {i18n}
