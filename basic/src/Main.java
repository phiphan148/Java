public class Main {

    public static void main(String[] args) {
        Product apple = new Product("Apple",1.25);
        apple.setPrice(1);

        System.out.println(apple.getName());
        System.out.println(apple.getPrice());
    }

}
