import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Car myCar = new Car();


//        //JS var age = 12.43
//        Integer age = 5;
//
//        //Js var name = 'name'
//        String text = "Mike";
//
//        // var myArray = []
//
//
//        List<String> myList = new ArrayList<>();
//        myList.add("Mike");
//        myList.add("Kate");
//
//
//        //Lops for(let i=0; ....
//
//        for(int i=0; i < myList.size(); i++ ){
//
//            System.out.println(myList.get(i));
//
//        }

        Car myFirstCar = new Car(2, 200);


        Car mySecCar = new Car();

        System.out.println(myCar.toString());
    }

}
