import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';

@Component({
    selector: 'app-products',
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
    products: any[] = [];
    displayProductForm: boolean = false;
    displayEditProductForm: boolean = false;
    newProduct: any = { name: '', price: 0 };
    editProduct: any = { name: '', price: 0 };
    productIdToDelete: number = 0;

    constructor(private productService: ProductService) {}

    ngOnInit(): void {
        this.loadProducts();
    }

    loadProducts() {
        this.productService.getAllProducts().subscribe(data => {
            this.products = data;
        });
    }

    showProductForm() {
        this.newProduct = { name: '', price: 0 };
        this.displayProductForm = true;
    }
    onSubmit() {
        this.productService.addProduct(this.newProduct).subscribe(() => {
            this.loadProducts();
            this.displayProductForm = false;
        });
    }

    cancelProductForm() {
        this.displayProductForm = false;
    }

    showEditProductForm(product: any) {
        this.editProduct = { ...product };
        this.displayEditProductForm = true;
    }

    onEditSubmit() {
        this.productService.updateProduct(this.editProduct).subscribe(() => {
            this.loadProducts();
            this.displayEditProductForm = false;
        });
    }

    cancelEditProductForm() {
        this.displayEditProductForm = false;
    }
}