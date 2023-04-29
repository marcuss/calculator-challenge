import axios, { AxiosInstance } from 'axios';

export default class ActivateService {
  private axios: AxiosInstance;

  constructor() {
    this.axios = axios;
  }

  public activateAccount(key: string): Promise<any> {
    return this.axios.get(`api/v1/activate?key=${key}`);
  }
}
