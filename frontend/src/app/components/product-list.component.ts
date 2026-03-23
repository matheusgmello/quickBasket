import { Component, OnInit, inject, signal, computed } from '@angular/core';
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
    <div class="product-container">
      <div class="product-grid">
        @for (product of products(); track product.id) {
          <div class="product-card">
            <div class="image-wrapper">
              <img [src]="getImageUrl(product)" [alt]="product.title" class="product-image" (error)="onImageError($event)" />
            </div>
            <div class="product-info">
              <h3 class="title" [title]="product.title">{{ product.title }}</h3>
              <p class="description">{{ product.description }}</p>
              <p class="price">{{ product.price | currency }}</p>
              <button (click)="addToBasket(product)" [disabled]="isAdding()">
                {{ isAdding() ? 'Adicionando...' : 'Adicionar ao Carrinho' }}
              </button>
            </div>
          </div>
        } @empty {
          <p class="loading">Buscando produtos...</p>
        }
      </div>

      <div class="pagination">
        <button [disabled]="offset() === 0" (click)="prevPage()">Anterior</button>
        <span class="page-info">Página {{ currentPage() }}</span>
        <button (click)="nextPage()">Próxima</button>
      </div>
    </div>
  `,
  styles: [`
    .product-container { padding: 20px; }
    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 25px;
      margin-bottom: 40px;
    }
    .product-card {
      border-radius: 12px;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      background: #fff;
      box-shadow: 0 4px 12px rgba(0,0,0,0.08);
      transition: transform 0.2s ease, box-shadow 0.2s ease;
      height: 480px;
    }
    .product-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 24px rgba(0,0,0,0.12);
    }
    .image-wrapper {
      width: 100%;
      height: 220px;
      background: #f8f9fa;
    }
    .product-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    .product-info {
      padding: 20px;
      flex-grow: 1;
      display: flex;
      flex-direction: column;
    }
    .title {
      font-size: 1.1rem;
      font-weight: 700;
      margin: 0 0 10px 0;
      color: #2d3436;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      height: 2.8rem;
    }
    .description {
      font-size: 0.9rem;
      color: #636e72;
      margin-bottom: 15px;
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
      height: 3.2rem;
    }
    .price {
      font-size: 1.4rem;
      font-weight: 800;
      color: #00b894;
      margin: auto 0 20px 0;
    }
    button {
      background: #0984e3;
      color: #fff;
      border: none;
      padding: 12px;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 700;
      transition: background 0.2s;
    }
    button:hover:not(:disabled) { background: #74b9ff; }
    button:disabled { background: #b2bec3; cursor: not-allowed; }
    
    .pagination {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 20px;
      margin-top: 20px;
    }
    .pagination button { padding: 8px 20px; }
    .page-info { font-weight: bold; font-size: 1.1rem; }
    .loading { text-align: center; font-size: 1.2rem; color: #636e72; }
  `]
})
export class ProductListComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly basketService = inject(BasketService);
  private readonly authService = inject(AuthService);

  products = signal<Product[]>([]);
  isAdding = signal(false);
  offset = signal(0);
  limit = signal(12);
  currentPage = computed(() => Math.floor(this.offset() / this.limit()) + 1);

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts(this.offset(), this.limit()).subscribe(products => {
      this.products.set(products);
      window.scrollTo(0, 0);
    });
  }

  nextPage() {
    this.offset.update(v => v + this.limit());
    this.loadProducts();
  }

  prevPage() {
    this.offset.update(v => Math.max(0, v - this.limit()));
    this.loadProducts();
  }

  getImageUrl(product: Product): string {
    if (!product.images || product.images.length === 0) {
      return 'https://via.placeholder.com/300x200?text=No+Image';
    }
    
    let url = product.images[0];
    
    // Platzi API quirk: sometimes images are ["[\"https://...\"]"]
    if (url.startsWith('[') && url.endsWith(']')) {
      try {
        const parsed = JSON.parse(url);
        if (Array.isArray(parsed) && parsed.length > 0) {
          return parsed[0];
        }
      } catch (e) {
        // Not JSON, continue with cleaned string
        url = url.replace(/[\[\]"]/g, '');
      }
    }
    
    // Some URLs are returned with double quotes
    url = url.replace(/"/g, '');
    
    return url;
  }

  onImageError(event: any) {
    event.target.src = 'https://via.placeholder.com/300x200?text=Image+Load+Error';
  }

  addToBasket(product: Product) {
    this.isAdding.set(true);
    const request: BasketRequest = {
      clientId: this.authService.userId,
      products: [{ id: product.id, quantity: 1 }]
    };
    this.basketService.createBasket(request).subscribe({
      next: () => {
        this.isAdding.set(false);
        alert('✅ Produto adicionado ao carrinho com sucesso!');
      },
      error: (err) => {
        this.isAdding.set(false);
        console.error('Erro ao adicionar produto', err);
        alert('❌ Erro ao adicionar produto ao carrinho. Verifique o console.');
      }
    });
  }
}
