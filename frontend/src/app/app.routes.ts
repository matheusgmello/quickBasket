import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list.component';
import { BasketDetailsComponent } from './components/basket-details.component';

export const routes: Routes = [
  { path: '', redirectTo: 'products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'basket', component: BasketDetailsComponent },
];
