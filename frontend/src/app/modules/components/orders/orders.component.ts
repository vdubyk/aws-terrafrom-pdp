import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';

@Component({
    selector: 'app-orders',
    templateUrl: './orders.component.html',
    styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
    orders: Order[] = [];
    allProducts: Product[] = [];
    orderForm: FormGroup;
    displayCreateOrderDialog: boolean = false;
    productOptions: any[] = [];
    constructor(
        private productService: ProductService,
        private orderService: OrderService,
        private fb: FormBuilder
    ) {
        this.orderForm = this.fb.group({
            products: this.fb.array([]),
            contactInfo: ['', [Validators.required, this.validateContactInfo]]
        });
    }

    ngOnInit(): void {
        this.loadProducts();
        this.loadOrders();
    }

    loadProducts() {
        this.productService.getAllProducts().subscribe(
            data => {
                this.allProducts = data;
                this.productOptions = data.map(product => ({
                    label: product.name,
                    value: product.id
                }));
                // Ініціалізуємо форму з одним продуктом
                this.addProductControl();
            },
            error => console.error('Error loading products:', error)
        );
    }

    removeProductControl(index: number) {
        this.products.removeAt(index);
    }

    loadOrders() {
        this.orderService.getAllOrders().subscribe(
            (data: Order[]) => {
                this.orders = data ? data.map((order: Order) => {
                    order.orderProducts = order.orderProducts.map((item: OrderProduct) => {
                        const product = this.allProducts.find(p => p.id === item.productId);
                        return {
                            ...item,
                            productName: product ? product.name : 'Unknown Product'
                        };
                    });
                    return order;
                }) : [];
            },
            error => {
                console.error('Error loading orders:', error);
                this.orders = [];
            }
        );
    }



    get products(): FormArray {
        return this.orderForm.get('products') as FormArray;
    }

    addProductControl() {
        const productControl = this.fb.group({
            productId: ['', Validators.required],
            quantity: [1, [Validators.required, Validators.min(1)]]
        });
        this.products.push(productControl);
    }

    validateContactInfo(control: FormControl): { [key: string]: any } | null {
        const value = control.value;
        const emailValid = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(value);
        const phoneValid = /^(\+\d{1,3}[- ]?)?\d{10}$/.test(value);
        return emailValid || phoneValid ? null : { 'invalidContactInfo': 'Provide a valid email or phone number' };
    }

    onSubmit() {
        if (this.orderForm.valid) {
            this.orderService.createOrder(this.orderForm.value).subscribe(
                data => {
                    console.log('Order created:', data);
                    this.orders.push(data);
                    this.displayCreateOrderDialog = false;
                    this.orderForm.reset();
                    this.products.clear();
                    this.addProductControl(); // Додаємо один початковий продукт
                    this.loadOrders();
                },
                error => console.error('Error creating order:', error)
            );
        }
    }


    updateOrder(order: any) {
        console.log('Update order:', order);
    }
}


interface Product {
    id: number;
    name: string;
    price: number;
}

interface OrderProduct {
    productId: number;
    quantity: number;
    productName?: string;
}

interface Order {
    id: number;
    orderDate: Date;
    totalPrice: number;
    status: string;
    orderProducts: OrderProduct[];
}
