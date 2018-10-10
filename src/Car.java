public class Car {
//OR CAN DO LIKE THIS
//    int doors = 2;
//    int speed = 200;
    int doors;
    int speed;
    String color;

    public Car(int doors, int speed){
        this.doors = doors;
        this.speed = speed;
    }

    public Car(){
        color = "blue";
    }

    @Override
    public String toString() {
        return "Car{" +
                "doors=" + doors +
                ", speed=" + speed +
                ", color='" + color + '\'' +
                '}';
    }

    //function drive(){}

//    public int drive

}
