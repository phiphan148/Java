public class Main {
   public static void main(String[] args){
       Product p1 = new Product("vaccum cleaner",100);
       Product p2 = new Product("bike6",600);
       Product p3 = new Product("pencil",3);
       Product p4 = new Product("bike3",300);
       Product p5 = new Product("moto",300);

        System.out.println(p1 + ", " + p2 + ", " + p3 + ", " + p4 + ", " + p5.toString());

        ShoppingCart cart1 = new ShoppingCart();
        cart1.addProducts(p1, 3);
        cart1.addProducts(p2, 5);
        cart1.addProducts(p2, 9);
        cart1.addProducts(p3, 1);
        cart1.addProducts(p4, 2);
        cart1.addProducts(p5, 21);
        cart1.addProducts(p4, 21);
//        cart1.replaceProduct("vaccum cleaner", p2);
        cart1.calculateTotalPrice();
       System.out.println(cart1.toString());
   }


}
