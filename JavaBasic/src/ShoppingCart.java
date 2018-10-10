import java.util.ArrayList;

public class ShoppingCart{
    private ArrayList<Product> products;

    ShoppingCart(){
        this.products = new ArrayList<>();
    }

    public void addProducts(Product product, int quantity){
        boolean check = false;
        if(this.products.size() == 0){
            this.products.add(product);
            product.setQuantity(quantity);
        } else {
            for (Product product1 : this.products) {
                if (product1.getName().equals(product.getName()) && product1.getPrice() == product.getPrice()) {
                    check = true;
                    product1.increaseQuantity(quantity);
                }
            }
            if(!check){
                this.products.add(product);
                product.setQuantity(quantity);
            }
        }
    }

    public double calculateTotalPrice(){
        int total = 0;
        int productQuantity = 0;
        for (Product product : this.products) {
            if (product.getQuantity() < 3) {
                total += (product.getPrice() * product.getQuantity());
                productQuantity += product.getQuantity();
            } else if (product.getQuantity() == 3) {
                total += (product.getPrice() * product.getQuantity());
                productQuantity += product.getQuantity();
                product.setFree(1);
            } else {
                total += product.getPrice() * (product.getQuantity() - (((double) (product.getQuantity() / (3 + 1))) * 1));
                productQuantity += product.getQuantity();
            }
        }
        return productQuantity >= 5 ? total * 0.9 : total;
    }

    public void replaceProduct(String name, Product product){
        if(this.products.size()>0){
            for (Product product1 : this.products)
                if (product1.getName().equals(name)) {
                    product1.setName(product.getName());
                    product1.setPrice(product.getPrice());
                }
        }
    }

    @Override
    public String toString(){
        return "We have a shopping cart with{" +
                this.products +
                " with TOTAL PRICE(include discount and buy 3 get 1 free): " + calculateTotalPrice() +
                "}";
    }

}