export interface User {
  id: string;
  name: string;
  email: string;
  createAt: string;
}
//요청 타입: id createdAt 빼고
export type CreateUserReq = Omit<User, 'id' | 'createAt'>;
// 응답 타입
export type UserRes = User;

//리스트 조회 요청
export interface GetUsersReq {
  page?: number;
  limit?: number;
  search?: string;
}

//리스트 조회 응답
export interface GetUsersRes{
  users: UserRes[];
  total: number;
  page: number;
  limit: number;
}
