export interface Product {
  id: number;
  title: string;
  price: number;
  description: string;
  images: string[];
  category?: {
    id: number;
    name: string;
    image: string;
  };
}

export interface BasketProduct {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

export interface Basket {
  id: string;
  client: number;
  status: string;
  totalPrice: number;
  paymentMethod?: string;
  products: BasketProduct[];
}

export interface BasketRequest {
  clientId: number;
  products: { id: number; quantity: number }[];
}

export interface PaymentRequest {
  paymentMethod: string;
}
