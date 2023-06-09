import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { IUser } from '@/shared/model/user.model';

export default class UserManagementService {
  public get(userId: string): Promise<any> {
    return axios.get(`api/v1/admin/users/${userId}`);
  }

  public create(user: IUser): Promise<any> {
    return axios.post('api/v1/admin/users', user);
  }

  public update(user: IUser): Promise<any> {
    return axios.put('api/v1/admin/users', user);
  }

  public remove(userId: string): Promise<any> {
    return axios.delete(`api/v1/admin/users/${userId}`);
  }

  public retrieve(req?: any): Promise<any> {
    return axios.get(`api/v1/admin/users?${buildPaginationQueryOpts(req)}`);
  }

  public retrieveAuthorities(): Promise<any> {
    return axios.get('api/v1/authorities');
  }
}
