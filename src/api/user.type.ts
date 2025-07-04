export interface User {
  id: string;
  name: string;
  email: string;
  createAt: string;
}
//요청 타입: id createdAt 빼고 -> Omit 특정 속성만 제거한 타입 정의
export type CreateUserReq = Omit<User, 'id' | 'createAt'>;
// 응답 타입
export type UserRes = User;

//리스트 조회 요청
export interface GetUsersReq {
  page?: number;
  limit?: number;
  search?: string;
}
export type GetUsersRes = User[]
