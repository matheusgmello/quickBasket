import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasketService } from '../services/basket.service';
import { Basket, PaymentRequest } from '../models';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-basket-details',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="basket-container">
      <h2>Seu Carrinho</h2>
      @if (basket(); as b) {
        <div class="basket-items">
          @for (item of b.products; track item.id) {
            <div class="basket-item">
              <span>{{ item.name }}</span>
              <span>x{{ item.quantity }}</span>
              <span>{{ item.price | currency }}</span>
              <span>Subtotal: {{ (item.price * item.quantity) | currency }}</span>
            </div>
          }
        </div>
        <div class="basket-summary">
          <p>Total: {{ b.totalPrice | currency }}</p>
          <div class="actions">
            <button class="clear-btn" (click)="clearBasket(b.id)">Limpar Carrinho</button>
            <button class="pay-btn" (click)="payBasket(b.id)">Pagar Agora</button>
          </div>
        </div>
      } @else {
        <p>Você não tem cestas abertas.</p>
      }
    </div>
  `,
  styles: [`
    .basket-container {
      padding: 20px;
      max-width: 800px;
      margin: 0 auto;
    }
    .basket-item {
      display: grid;
      grid-template-columns: 2fr 1fr 1fr 1fr;
      padding: 10px;
      border-bottom: 1px solid #eee;
    }
    .basket-summary {
      margin-top: 20px;
      text-align: right;
      font-size: 1.5rem;
      font-weight: bold;
    }
    .actions {
      display: flex;
      gap: 10px;
      justify-content: flex-end;
      margin-top: 10px;
    }
    button {
      padding: 10px 20px;
      border-radius: 4px;
      border: none;
      cursor: pointer;
      font-weight: bold;
    }
    .clear-btn { background: #e74c3c; color: #fff; }
    .pay-btn { background: #27ae60; color: #fff; }
  `]
})
export class BasketDetailsComponent implements OnInit {
  private readonly basketService = inject(BasketService);
  private readonly authService = inject(AuthService);

  basket = signal<Basket | null>(null);

  ngOnInit() {
    this.loadBasket();
  }

  loadBasket() {
    this.basketService.getBaskets().subscribe(baskets => {
      // For simplicity, take the first open basket for the current user
      const currentBasket = baskets.find(b => b.client === this.authService.user && b.status === 'OPEN');
      this.basket.set(currentBasket || null);
    });
  }

  clearBasket(id: string) {
    this.basketService.clearBasket(id).subscribe(() => this.loadBasket());
  }

  payBasket(id: string) {
    const request: PaymentRequest = { paymentMethod: 'CREDIT_CARD' };
    this.basketService.payBasket(id, request).subscribe(() => {
      alert('Pagamento processado com sucesso!');
      this.loadBasket();
    });
  }
}
