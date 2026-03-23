import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="container">
        <a routerLink="/" class="logo">BasketService Store</a>
        <div class="nav-links">
          <a routerLink="/products" routerLinkActive="active">Produtos</a>
          <a routerLink="/basket" routerLinkActive="active">Carrinho</a>
        </div>
      </div>
    </nav>

    <div class="container main-content">
      <router-outlet />
    </div>
  `,
  styles: [`
    .navbar {
      background: #2c3e50;
      color: #fff;
      padding: 1rem 0;
      margin-bottom: 2rem;
    }
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .logo {
      font-size: 1.5rem;
      font-weight: bold;
      color: #fff;
      text-decoration: none;
    }
    .nav-links a {
      color: #fff;
      text-decoration: none;
      margin-left: 1.5rem;
      padding-bottom: 5px;
    }
    .active {
      border-bottom: 2px solid #3498db;
    }
    .main-content {
      display: block;
    }
  `],
})
export class App {
  protected readonly title = signal('frontend');
}
