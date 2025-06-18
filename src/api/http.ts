// src/api/http.ts
import type { HttpStatusCode } from './http-status-code'

/**
 * RequestConfig:
 *  - headers, params, data 모두 '값이 없을 수도 있다'는 의미로
 *    명시적으로 `| undefined` 를 붙여야
 *    TS 4.9의 exactOptionalPropertyTypes 모드에서도
 *    'undefined' 를 허용합니다.
 */
export interface RequestConfig<D = unknown> {
  headers?: Record<string, string> | undefined
  params?: object | undefined
  data?: D | undefined
}

export interface HttpResponse<T = unknown> {
  data: T
  status: HttpStatusCode
  headers: Headers
}

export class Http {
  constructor(
    private readonly baseURL: string,
    private readonly defaultHeaders: Record<string, string> = {}
  ) {}

  private async request<T = unknown, D = unknown>(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE',
    url: string,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    // fullUrl 예: http://localhost:8080/api/users?page=1&limit=10
    const fullUrl = new URL(url, this.baseURL)

    if (config.params) {
      Object.entries(config.params).forEach(([k, v]) =>
        fullUrl.searchParams.append(k, String(v))
      )
    }

    const response = await fetch(fullUrl.toString(), {
      method,
      headers: {

        'Content-Type': 'application/json',
        ...this.defaultHeaders,
        ...config.headers
      },
      // body: undefined 불가 → null로 대체
      // undefined 대신 반드시 null
      body: config.data != null ? JSON.stringify(config.data) : null
    })

    if (!response.ok) {
      throw new Error(`HTTP Error: ${response.status} ${response.statusText}`)
    }
    const data = await response.json()
    return {
      data,
      status: response.status as HttpStatusCode,
      headers: response.headers
    }
  }

  public get<T = unknown>(
    url: string,
    config: RequestConfig = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T>('GET', url, config)
  }

  public post<T = unknown, D = unknown>(
    url: string,
    data: D,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T, D>('POST', url, { ...config, data })
  }

  public put<T = unknown, D = unknown>(
    url: string,
    data: D,
    config: RequestConfig<D> = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T, D>('PUT', url, { ...config, data })
  }

  public delete<T = unknown>(
    url: string,
    config: RequestConfig = {}
  ): Promise<HttpResponse<T>> {
    return this.request<T>('DELETE', url, config)
  }
}

/**
 * ⚠️ 여기가 핵심 ⚠️
 * import.meta.env.VITE_API_BASE_URL 이 없다면
 * 기본으로 spring-boot가 돌고 있는 8080번 포트를 사용합니다.
 */
export const httpClient = new Http(
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
)
