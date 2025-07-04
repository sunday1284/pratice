import { boot } from 'quasar/wrappers';

// ✨ 1. eventSource 변수를 미리 선언만 합니다.
let eventSource: EventSource;

export default boot(() => {
  // ✨ 2. boot 함수 안에서 EventSource 인스턴스를 생성하고 변수에 할당합니다.
  // 이 시점에서 백엔드 연결이 시작됩니다.
  eventSource = new EventSource('http://localhost:8080/users/connect/main-user-list');

  // ✨ 3. 인스턴스 생성 직후에 핸들러를 등록하여 레이스 컨디션을 방지합니다.
  eventSource.onopen = () => {
    // 이제 이 로그가 콘솔에 정상적으로 출력될 것입니다.
    console.log('✅ SSE Connection established successfully!');
  };

  eventSource.onerror = (error: Event) => {
    console.error('SSE Connection error:', error);
    // EventSource는 자동으로 재연결을 시도하므로, 에러 시 무조건 닫지 않는 것이 더 안정적일 수 있습니다.
    // 필요 시에만 주석 해제: eventSource.close();
  };
});

// ✨ 4. 선언된 변수를 export 하는 것은 그대로 유지하여 다른 파일(스토어 등)에서 사용할 수 있게 합니다.
export { eventSource };
