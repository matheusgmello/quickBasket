export interface Product {
  id: number;
  title: string;
  price: number;
  description?: string;
  category?: {
    id: number;
    name: string;
    image: string;
  };
  images?: string[];
}

export interface BasketProduct {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

export interface Basket {
  id: string;
  client: string;
  status: string;
  totalPrice: number;
  paymentMethod?: string;
  products: BasketProduct[];
}

export interface BasketRequest {
  clientId: string;
  products: { id: number; quantity: number }[];
}

export interface PaymentRequest {
  paymentMethod: string;
}
