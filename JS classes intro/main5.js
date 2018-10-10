console.log("Objects containing other objects");

// Let's remember our Product class

class Product {

    constructor(name, price) {
        this.name = name;
        this.price = price;
    }

    toString() {
        return this.name + " " + this.price;
    }
}

// And create a couple of products
let p1 = new Product("vacuum cleaner", 100);
let p2 = new Product("bike6", 600);
let p3 = new Product("pencil", 3);
let p4 = new Product("bike3", 300);
let p5 = new Product("moto", 500);


// Now, let's create a ShoppingCart class, that will contain products

class ShoppingCart {

    constructor() {
        this.products = []; // empty at first
    }


    addProduct(product) {
        let check = false;
        if (this.products.length == 0) {
            this.products.push(product);
        } else {
            for (let i = 0; i < this.products.length; i++) {
                if (this.products[i].name === product.name && this.products[i].price === product.price) {
                    check = true;
                    if (this.products[i].quantity == undefined) {
                        this.products[i].quantity = 2;
                    } else {
                        this.products[i].quantity += 1;
                    }
                }
            }
            if (!check) {
                this.products.push(product);
            }
        }
    }

    totalPrice() {
        let total =0;
        let productNum = 0;
        for (let i = 0; i < this.products.length; i++) {
            if (this.products[i].quantity == undefined) {
                total += this.products[i].price;
                productNum += 1;
            } else{
                productNum += this.products[i].quantity;
                total += (this.products[i].price * this.products[i].quantity);
            }
        }
        return productNum >= 5 ? total * 0.9 : total;
    }

    totalPrice2() {
        let total =0;
        let productNum = 0;
        for (let i = 0; i < this.products.length; i++) {
            if (this.products[i].quantity == undefined) {
                total += this.products[i].price;
                productNum += 1;
            } else if(this.products[i].quantity <3){
                total += (this.products[i].price * this.products[i].quantity);
                productNum += this.products[i].quantity;
            } else if(this.products[i].quantity == 3){
                total += (this.products[i].price * this.products[i].quantity);
                productNum += this.products[i].quantity;
                this.products[i].quantity += 1;
            }else {
                total += this.products[i].price * (this.products[i].quantity - (Math.trunc(this.products[i].quantity/(3+1))*1))
                productNum += this.products[i].quantity;
            }
        }
        return productNum >= 5 ? total * 0.9 : total;
    }

    replace(productName, replacementProduct){
        if(this.products.length>0){
            for(let i=0; i<this.products.length; i++){
                if(this.products[i].name == productName){
                    this.products[i].name = replacementProduct.name;
                    this.products[i].price = replacementProduct.price;
                }
            }
        }
    }

    toString() {
        return "cart with: " + this.products;
    }
}


// We can create a cart and add the products we created before

let cart = new ShoppingCart();
cart.addProduct(p1);
cart.addProduct(p1);
cart.addProduct(p2);
cart.addProduct(p3);
cart.addProduct(p3);
cart.addProduct(p4);
cart.addProduct(p1);
cart.addProduct(p1);
cart.addProduct(p1);
cart.addProduct(p2);
cart.addProduct(p1);
cart.addProduct(p3);
cart.addProduct(p5);
cart.addProduct(p1);
cart.addProduct(p1);
cart.addProduct(p1);
cart.replace('vacuum cleaner',new Product('teeth brush',20));
console.log(cart)
// console.log(cart.totalPrice());
console.log(cart.totalPrice2());
// console.log("We have a " + cart);



