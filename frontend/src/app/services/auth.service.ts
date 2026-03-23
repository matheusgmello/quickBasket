import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly userSignal = signal('user');
  private readonly passSignal = signal('password');
  private readonly userIdSignal = signal(1); // Added a numeric user ID

  get credentials() {
    return btoa(`${this.userSignal()}:${this.passSignal()}`);
  }

  get user() {
    return this.userSignal();
  }

  get userId() {
    return this.userIdSignal();
  }
}
