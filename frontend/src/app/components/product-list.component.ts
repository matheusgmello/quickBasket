import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../services/product.service';
import { BasketService } from '../services/basket.service';
import { Product, BasketRequest } from '../models';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="product-grid">
      @for (product of products(); track product.id) {
        <div class="product-card">
          <img [src]="product.images?.[0]" [alt]="product.title" class="product-image" />
          <div class="product-info">
            <h3>{{ product.title }}</h3>
            <p class="price">{{ product.price | currency }}</p>
            <button (click)="addToBasket(product)">Adicionar ao Carrinho</button>
          </div>
        </div>
      } @empty {
        <p>Carregando produtos...</p>
      }
    </div>
  `,
  styles: [`
    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 20px;
      padding: 20px;
    }
    .product-card {
      border: 1px solid #ddd;
      border-radius: 8px;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      background: #fff;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .product-image {
      width: 100%;
      height: 200px;
      object-fit: cover;
    }
    .product-info {
      padding: 15px;
      flex-grow: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .price {
      font-size: 1.2rem;
      font-weight: bold;
      color: #27ae60;
    }
    button {
      background: #3498db;
      color: #fff;
      border: none;
      padding: 10px;
      border-radius: 4px;
      cursor: pointer;
      font-weight: bold;
    }
    button:hover {
      background: #2980b9;
    }
  `]
})
export class ProductListComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly basketService = inject(BasketService);
  private readonly authService = inject(AuthService);

  products = signal<Product[]>([]);

  ngOnInit() {
    this.productService.getProducts().subscribe(products => this.products.set(products));
  }

  addToBasket(product: Product) {
    const request: BasketRequest = {
      clientId: this.authService.user,
      products: [{ id: product.id, quantity: 1 }]
    };
    this.basketService.createBasket(request).subscribe({
      next: () => alert('Produto adicionado ao carrinho!'),
      error: (err) => console.error('Erro ao adicionar produto', err)
    });
  }
}
