// src/api/user.ts
import { httpClient } from './index'
import type {
  CreateUserReq,
  GetUsersReq,
  GetUsersRes,
  UserRes
} from './user.type'   // 실제 파일명이 user.type.ts라면 이대로, 아니면 user.types.ts로 맞춰주세요.

export async function getUsers(
  params?: GetUsersReq
): Promise<GetUsersRes> {
  const res = await httpClient.get<GetUsersRes>(
    '/api/users', { params }    // params?: GetUsersReq → Record<string, any> 이므로 OK
  )
  return res.data
}

export async function createUser(
  user: CreateUserReq
): Promise<UserRes> {
  const res = await httpClient.post<UserRes, CreateUserReq>(
    '/api/users',
    user
  )
  return res.data
}
