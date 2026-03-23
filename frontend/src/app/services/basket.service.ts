import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Basket, BasketRequest, PaymentRequest } from '../models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/basket';

  getBaskets(): Observable<Basket[]> {
    return this.http.get<Basket[]>(this.apiUrl);
  }

  getBasketById(id: string): Observable<Basket> {
    return this.http.get<Basket>(`${this.apiUrl}/${id}`);
  }

  createBasket(request: BasketRequest): Observable<Basket> {
    return this.http.post<Basket>(this.apiUrl, request);
  }

  updateBasket(id: string, request: BasketRequest): Observable<Basket> {
    return this.http.put<Basket>(`${this.apiUrl}/${id}`, request);
  }

  payBasket(id: string, request: PaymentRequest): Observable<Basket> {
    return this.http.put<Basket>(`${this.apiUrl}/${id}/payment`, request);
  }

  clearBasket(id: string): Observable<Basket> {
    return this.http.patch<Basket>(`${this.apiUrl}/${id}/clear`, {});
  }

  deleteBasket(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
