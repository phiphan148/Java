public class Product{
    private String name;
    private int price;
    private int quantity;
    private int free;

    Product(String name, int price){
        this.name = name;
        this.price = price;
    }

//    Product(String name, int price, int quantity, int free){
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//        this.free = free;
//    }

    @Override
    public String toString(){
        return "Product{" +
                "name: " + name +
                ", price: " + price +
                ", quantity: " + quantity +
                ", free product: " + free +
                "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(int quantity){
        this.quantity += quantity;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}