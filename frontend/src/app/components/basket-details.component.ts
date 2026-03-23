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
              <span>{{ item.title }}</span>
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
      padding: 15px;
      border-bottom: 1px solid #eee;
      align-items: center;
    }
    .basket-summary {
      margin-top: 20px;
      text-align: right;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }
    .basket-summary p {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 20px;
      color: #2d3436;
    }
    .actions {
      display: flex;
      gap: 15px;
      justify-content: flex-end;
    }
    button {
      padding: 12px 25px;
      border-radius: 8px;
      border: none;
      cursor: pointer;
      font-weight: bold;
      transition: opacity 0.2s;
    }
    button:hover { opacity: 0.8; }
    .clear-btn { background: #ff7675; color: #fff; }
    .pay-btn { background: #00b894; color: #fff; }
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
      const currentBasket = baskets.find(b => b.client === this.authService.userId && b.status === 'OPEN');
      this.basket.set(currentBasket || null);
    });
  }

  clearBasket(id: string) {
    if (confirm('Deseja realmente limpar o carrinho?')) {
      this.basketService.clearBasket(id).subscribe(() => this.loadBasket());
    }
  }

  payBasket(id: string) {
    // Corrected PaymentMethod to match Backend Enum: PIX, DEBIT, CREDIT
    const request: PaymentRequest = { paymentMethod: 'CREDIT' };
    this.basketService.payBasket(id, request).subscribe({
      next: () => {
        alert('✅ Pagamento processado com sucesso!');
        this.loadBasket();
      },
      error: (err) => {
        console.error('Erro no pagamento', err);
        alert('❌ Erro ao processar pagamento.');
      }
    });
  }
}
