
console.log("Example with class");

// Instead of a function that creates a car,
// we define a class Car

// A class looks much cleaner, and has many other advantages.

class Car {

	// The constructor initializes the properties.
	// It gets called when we do: new Car(some_brand)
	constructor(brand, model) {
		this.brand = brand;
		this.speed = 0;
		this.model = model;
	}

	// methods:

	// (notice that now we don't say "function" and that we
	//  don't put a comma after closing brace of the function)

	accelerate(amount) {
		this.speed += amount;
	}

	brake(amount) {
		this.speed -= amount;
		if(this.speed<0){
			this.speed = 0;
		}
	}

	status() {
		return this.brand + " with " + this.model + " running at " + this.speed + " km/h";
	}

	stop(){
        this.speed = 0;
	}

	checkCarRun(){
		if(this.speed>0){
			return true;
		} else {
			return false;
		}
	}
}


// Now we use create a Car object using the class

var car = new Car("Ford","Mondeo");

console.log(car.status());
car.accelerate(50);
car.brake(30);
console.log(car.status());
car.accelerate(100);
console.log(car.status());
car.brake(25);
console.log(car.status());


// We may create other cars easily

var car2 = new Car("Ferrari","Mondeo");
car2.accelerate(200);
console.log(car2.status());


class TV {
	constructor(brand){
		this.brand = brand;
		this.channel = 1;
		this.volume = 50;
	}

	increaseVolumn(amount){
		this.volume += amount;
		if(this.volume>100){
			this.volume =100;
		}
	}

	decreaseVolumn(amount){
		this.volume -= amount;
        if(this.volume<0){
            this.volume =0;
        }
	}

	setChannel(channel){
		if(channel>50 || channel<0){
			this.channel = this.channel;
		} else if(this.channel >0 && this.channel<50) {
            this.channel = channel;
		}
	}

	reset(brand){
		return constructor(brand);
	}

	status(){
		return this.brand + " at channel " + this.channel +" , volume " + this.volume;
	}
}

var myTV = new TV("Panasonic");

console.log(myTV.status());
myTV.increaseVolumn(50);
myTV.decreaseVolumn(20);
console.log(myTV.status());