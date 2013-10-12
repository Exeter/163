#include <iostream>
//calculates the greatest common divisor of two numbers a and b
int gcd(int a, int b) {
	return (a==0)?b:gcd(b%a,a);
}

//structure representing a fraction. the operations +, -, *, / are supported and these
//operators do not change the numerator or denominator of the operands. == is supported
//for comparison
struct fraction{

	int numerator;
	int denominator;

	//reduces the fraction into lowest terms (numerator and denominator are relatively prime)
	void reduce(){
		int g = gcd(numerator, denominator);
		numerator /= g;
		denominator /= g;
	}

	//adds a fraction to this fraction and returns the result
	fraction operator+(const fraction addend) {
		fraction sum;

		//determine a common denominator
		sum.denominator = denominator*addend.denominator;
		sum.numerator = numerator*addend.denominator + denominator*addend.numerator;

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

	//multiplies this fraction by another fraction and returns the result
	fraction operator*(const fraction multiplier) {
		fraction product;
		product.denominator = denominator*multiplier.denominator;
		product.numerator = numerator*multiplier.numerator;
		product.reduce();
		return product;
	}

	//divides this fraction by another fraction and returns the result
	fraction operator/(const fraction divisor){
		fraction quotient;
		quotient.numerator = numerator*divisor.denominator;
		quotient.denominator = denominator*divisor.numerator;
		quotient.reduce();
		return quotient;
	}

	//returns if two fractions are equal (i.e. their numerator and denominator are the same)
	int operator==(const fraction f ) {
		return ( numerator == f.numerator ) && ( denominator == f.denominator );
	}
};

/****
takes a list of numbers and determines if it is possible to obtain the target
number using the manipulations allowed in 163.

numbers: the list of numbers the user is allowed to manipulate
target: the target number the user must obtain by manipulating a list of numbers
*/

bool check(int sizeNum, fraction *numbers , fraction target ) {
	if ( sizeNum == 1 ) {
		return (numbers[ 0 ] == target);
	} else {
		//std::cout << sizeNum << std::endl;
		for(int i = 0; i < sizeNum; i++){
			for(int j = i+1; j < sizeNum; j++){
				fraction newNumbers[ sizeNum ];
				int newNumIndex= 0;
				for(int k = 0; k < sizeNum; k++){
					if(k!=j && k!=i){
						newNumbers[newNumIndex] = numbers[k];
						newNumIndex++;
					}
				}

				newNumbers[newNumIndex] = numbers[i]+numbers[j];
				if(check(sizeNum-1, newNumbers, target)){
					return true;
				}

				newNumbers[newNumIndex] = numbers[i]-numbers[j];
				if(check(sizeNum-1, newNumbers, target)){
					return true;
				}

				newNumbers[newNumIndex] = numbers[j]-numbers[i];
				if(check(sizeNum-1, newNumbers, target)){
					return true;
				}

				newNumbers[newNumIndex] = numbers[i]*numbers[j];
				if(check(sizeNum-1, newNumbers, target)){
					return true;
				}

				if(numbers[j].numerator != 0 && numbers[i].denominator != 0){
					newNumbers[newNumIndex] = numbers[i]/numbers[j];
					if(check(sizeNum-1, newNumbers, target)){
						return true;
					}
				}

				if(numbers[i].numerator != 0 && numbers[j].denominator != 0){
					newNumbers[newNumIndex] = numbers[j]/numbers[i];
					if(check(sizeNum-1, newNumbers, target)){
						return true;
					}
				}
			}
		}
		return false;
	}
}

int main(){

	//define the fraction for 163
	fraction onesixtythree;
	onesixtythree.numerator = 163;
	onesixtythree.denominator = 1;
	fraction ten;
	ten.numerator = 10;
	ten.denominator = 1;

	fraction test[ 6 ];
	for(int i = 0; i < 6; i++){
		std::cin >> test[i].numerator;
		std::cin >> test[i].denominator;
	}
	std::cout << check(6,test,ten) << std::endl;

	return 0;
}
