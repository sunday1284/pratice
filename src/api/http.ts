// src/api/http.ts
import type { HttpStatusCode } from './http-status-code'

/**
 * RequestConfig<D>:
 * - headers: 요청 헤더를 담는 객체 (예: Authorization 토큰)
 * - params: URL 쿼리 파라미터 (예: ?page=1&limit=10)
 * - data: POST/PUT 요청의 본문(body)에 들어갈 데이터
 *
 * 모두 값이 없을 수도 있으므로 `| undefined` 를 붙여줍니다.
 */
export interface RequestConfig<D = unknown> {
  headers?: Record<string, string> | undefined
  params?: object | undefined
  data?: D | undefined
}

/**
 * HttpResponse<T>:
 * - data: 서버에서 실제 꺼내 쓸 데이터
 * - status: HTTP 상태 코드 (200, 404 등)
 * - headers: 응답 헤더
 */
export interface HttpResponse<T = unknown> {
  data: T
  status: HttpStatusCode
  headers: Headers
}

/**
 * Http 클래스:
 * - baseURL: API 서버의 기본 주소
 * - defaultHeaders: 모든 요청에 자동으로 붙일 헤더
 */
export class Http {
  constructor(
    private readonly baseURL: string,
    private readonly defaultHeaders: Record<string, string> = {}
  ) {}

  /**
   * 공통 요청 처리 함수
   * @param method HTTP 메서드 (GET/POST/PUT/DELETE)
   * @param url API 경로 (예: '/api/users')
   * @param config 요청 설정 (headers, params, data)
   */
  private async request<T = unknown, D = unknown>(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH',
    url: string,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    // 1) baseURL 이 빈 문자열일 수도 있으니, window.location.origin 을 기본으로 삼습니다
    const origin = this.baseURL || window.location.origin

    // 2) URL 인스턴스를 무조건 만듭니다
    const fullUrl = new URL(url, origin)


    // params 가 있다면 URL에 key=value 형식으로 붙여준다
    if (config.params) {
      Object.entries(config.params).forEach(([k, v]) =>
        fullUrl.searchParams.append(k, String(v))
      )
    }

    // 2) fetch 호출
    const response = await fetch(fullUrl.toString(), {
      method,
      headers: {
        'Content-Type': 'application/json', // JSON 형식 고정
        ...this.defaultHeaders,             // 기본 헤더 추가
        ...config.headers                  // 호출 시 추가 헤더
      },
      // body: GET 요청에는 무시되고, POST/PUT 요청 시 JSON 문자열로 변환
      body: config.data != null ? JSON.stringify(config.data) : null
    })

    // 3) HTTP 오류 처리
    if (!response.ok) {
      // 200~299 범위가 아니면 예외 발생
      throw new Error(`HTTP Error: ${response.status} ${response.statusText}`)
    }

    // 4) JSON 파싱 후 결과 리턴
    const data = await response.json()
    return {
      data,
      status: response.status as HttpStatusCode,
      headers: response.headers
    }
  }

  /**
   * GET 요청
   * @param url API 경로
   * @param config RequestConfig (headers, params)
   */
  public get<T = unknown>(
    url: string,
    config: RequestConfig = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T>('GET', url, config)
  }

  /**
   * POST 요청
   * @param url API 경로
   * @param data 전송할 데이터
   * @param config 추가 요청 설정
   */
  public post<T = unknown, D = unknown>(
    url: string,
    data: D,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T, D>('POST', url, { ...config, data })
  }

  /**
   * PUT 요청
   * @param url API 경로
   * @param data 전송할 데이터
   * @param config 추가 요청 설정
   */
  public put<T = unknown, D = unknown>(
    url: string,
    data: D,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T, D>('PUT', url, { ...config, data })
  }

  /**
   * DELETE 요청
   * @param url API 경로
   * @param config 추가 요청 설정
   */
  public delete<T = unknown>(
    url: string,
    config: RequestConfig = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T>('DELETE', url, config)
  }

  /**
   * ✨ PATCH 요청 (완성된 부분)
   * @param url API 경로
   * @param data 전송할 데이터 (부분 업데이트)
   * @param config 추가 요청 설정
   */
  public patch<T = unknown, D = unknown>(
    url: string,
    data: D,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    // 내부 request 메서드를 'PATCH' 타입으로 호출합니다.
    return this.request<T, D>('PATCH', url, { ...config, data });
  }
}

/**
 * httpClient 인스턴스 생성
 * import.meta.env.VITE_API_BASE_URL 환경변수가 없으면
 * 기본값으로 'http://localhost:8080' 을 사용합니다.
 */
export const httpClient = new Http(
  import.meta.env.VITE_API_BASE_URL || window.location.origin
)
