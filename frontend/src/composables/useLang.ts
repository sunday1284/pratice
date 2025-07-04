
// i18n 관련 기능들을 묶어서 제공하는 공통 함수
import { useI18n } from 'vue-i18n';
import { useQuasar, type QuasarLanguage } from 'quasar';
// 1. 필요한 언어 팩을 import
import langKo from 'quasar/lang/ko-KR';
import langEn from 'quasar/lang/en-US'


type SupportedLang = 'ko-KR' | 'en-US';
//2. import한 언어 팩들을 객체에 담아 쉽게 접근 가능
const langPacks: Record<SupportedLang, QuasarLanguage> ={
  'ko-KR': langKo,
  'en-US': langEn,
}

export function useLang(){
  const { t, locale } = useI18n();
  const $q = useQuasar();
  /**
   * 앱의 언어와 퀘이사 컴포넌트의 언어를 한 번에 변경함
   * @param lang 변경할 언어 코드
   */
    // ✨ 3. setLanguage 함수의 파라미터 타입도 SupportedLang으로 변경합니다.
  const setLanguage = (lang: SupportedLang) => {
      locale.value = lang;

      // 이제 타입스크립트는 lang이 항상 langPacks에 있는 키임을 알기 때문에 오류가 없습니다.
      $q.lang.set(langPacks[lang]);

      localStorage.setItem('lang', lang);
    };
  // 컴포넌트에서 사용할 함수와 상태들을 반환
  return{
    t,
    locale,
    setLanguage
  }
}


