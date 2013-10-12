#include <iostream>
int gcd(int a, int b){
	return (a==0)?b:gcd(b%a,a);
}
struct fraction{
	int numerator;
	int denominator;

	void reduce(){
		int g = gcd(numerator, denominator);
		numerator /= g;
		denominator /= g;
	}

	//adds a fraction to this fraction and returns the result
	fraction operator+(const fraction augend) {
		fraction sum;

		//determine a common denominator
		sum.denominator = denominator*augend.denominator;
		sum.numerator = numerator*augend.denominator + denominator*augend.numerator;

		//rewrite in lowest terms
		sum.reduce();
		return sum;
	}

	//subtracts a fraction from this fraction and returns the result
	fraction operator-(const fraction subtrahend) {
		fraction diff;
		//determine a common denominator
		diff.denominator = denominator*subtrahend.denominator;
		diff.numerator = numerator*subtrahend.denominator - denominator*subtrahend.numerator;

		//rewrite in lowest terms
		diff.reduce();
		return diff;
	}

	fraction operator*(const fraction multiplicand) {
		fraction product;
		product.denominator = denominator*multiplicand.denominator;
		product.numerator = numerator*multiplicand.numerator;
		product.reduce();
		return product;
	}
};

/****
takes a list of numbers and determines if it is possible to obtain the target
number using the manipulations allowed in 163.

numbers: the list of numbers the user is allowed to manipulate
target: the target number the user must obtain by manipulating a list of numbers
*/

bool check(int sizeNum, fraction *numbers , int target ){


}

int main(){
	fraction a;
	a.numerator = 1;
	a.denominator = 3;
	fraction b;
	b.numerator = 2;
	b.denominator = 12;
	fraction c = b+a;
	c.reduce();
	std::cout << c.numerator << " " << c.denominator << std::endl;
	return 0;
}
