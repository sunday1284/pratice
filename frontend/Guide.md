🔍 초보자를 위한 요약 포인트
pinia 정리 
state: 데이터를 저장하는 공간 (users, loading, error 등).

getters: state를 가공해서 계산된 값을 반환.

actions: API 요청 같은 로직 담당. 여기선 사용자 불러오기(fetch)와 생성(create)을 수행.

defineStore('user', {...}): user라는 이름의 store를 등록. 컴포넌트에서 useUserStore()로 사용.