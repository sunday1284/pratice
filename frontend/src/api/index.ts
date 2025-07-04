import { Http } from 'src/api/http';


export const httpClient = new Http(
  import.meta.env.VITE_API_URL || 'http://localhost:3000',
  {
    Authorization: `Bearer ${localStorage.getItem('token') ?? ''}`,
  }
)
