import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly userSignal = signal('user');
  private readonly passSignal = signal('password');

  get credentials() {
    return btoa(`${this.userSignal()}:${this.passSignal()}`);
  }

  get user() {
    return this.userSignal();
  }
}
