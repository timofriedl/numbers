package com.timofriedl.numbers.complex;

import com.timofriedl.numbers.bigrational.BigRational;
import com.timofriedl.numbers.complexrational.ComplexRational;

/**
 * Immutable non-exact complex numbers, stored as a pair of <code>double</code>
 * values.
 * 
 * @see BigRational for exact rational numbers
 * @see ComplexRational for exact complex rational numbers
 * @see ComplexMath for common complex functions
 * @author Timo Friedl
 */
public class Complex {

	/**
	 * the complex number constant 0
	 */
	public static final Complex ZERO = new Complex(0.0);

	/**
	 * the complex number constant 1/2
	 */
	public static final Complex ONE_HALF = new Complex(0.5);

	/**
	 * the complex number constant 1/10
	 */
	public static final Complex ONE_TENTH = new Complex(0.1);

	/**
	 * the complex number constant 1
	 */
	public static final Complex ONE = new Complex(1.0);

	/**
	 * the complex number constant 2
	 */
	public static final Complex TWO = new Complex(2.0);
	
	/**
	 * the euler constant e
	 */
	public static final Complex E = new Complex(Math.E);
	
	/**
	 * the circle constant pi
	 */
	public static final Complex PI = new Complex(Math.PI);

	/**
	 * the complex number constant 10
	 */
	public static final Complex TEN = new Complex(10.0);

	/**
	 * the imaginary constant i where i^2 = -1
	 */
	public static final Complex I = new Complex(0.0, 1.0);

	/**
	 * the real and imaginary part of this complex number
	 */
	private final double real, imaginary;

	/**
	 * Creates a new complex number instance.
	 * 
	 * @param real      the real part of this complex number
	 * @param imaginary the imaginary part of this complex number
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Creates a new complex number instance from a real number
	 * (<code>double</code>).
	 * 
	 * @param real the real part of this complex number
	 */
	public Complex(double real) {
		this(real, 0.0);
	}

	/**
	 * Calculates the arithmetic sum with another {@link Complex}.
	 * 
	 * @param addend the number to add to this number
	 * @return a new complex number that is equal to the sum of <code>this</code>
	 *         and <code>addend</code>
	 */
	public Complex add(Complex addend) {
		return new Complex(real + addend.real, imaginary + addend.imaginary);
	}

	/**
	 * Calculates the arithmetic difference to another {@link Complex}.
	 * 
	 * @param subtrahend the number to subtract from this number
	 * @return a new complex number that is equal to the difference of
	 *         <code>this</code> and <code>subtrahend</code>
	 */
	public Complex subtract(Complex subtrahend) {
		return new Complex(real - subtrahend.real, imaginary - subtrahend.imaginary);
	}

	/**
	 * Calculates the arithmetic product with another {@link Complex}.
	 * 
	 * @param factor the number to multiply with this number
	 * @return a new complex number that is equal to the product of
	 *         <code>this</code> and <code>factor</code>
	 */
	public Complex multiply(Complex factor) {
		return new Complex(real * factor.real - imaginary * factor.imaginary,
				real * factor.imaginary + imaginary * factor.real);
	}

	/**
	 * Calculates the arithmetic quotient with another {@link Complex}.
	 * 
	 * @param divisor the number to divide this number by
	 * @return a new complex number that is equal to the quotient of
	 *         <code>this</code> and <code>divisor</code>
	 */
	public Complex divide(Complex divisor) {
		final double denom = divisor.real * divisor.real + divisor.imaginary * divisor.imaginary;

		return new Complex((real * divisor.real + imaginary * divisor.imaginary) / denom,
				(imaginary * divisor.real - real * divisor.imaginary) / denom);
	}

	/**
	 * Raises this complex number to the power of a given <code>int</code> value and
	 * returns the result.
	 * 
	 * The time complexity of this function is O(log |exponent|).
	 * 
	 * Special case: If <code>this</code> is an element of {-1, 0, 1, i} or
	 * <code>exponent</code> is equal to zero, time complexity is O(1).
	 * 
	 * This function is implemented iteratively, not recoursively. Therefore, large
	 * number calculations only consume constant amount of storage.
	 * 
	 * @return this number to the power of the exponent
	 */
	public Complex pow(int exponent) {
		if (exponent == 0)
			return ONE;
		else if (exponent < 0)
			return invert().pow(-exponent);
		else if (equals(ONE))
			return ONE;
		else if (equals(I))
			switch (exponent % 4) {
			case 0:
				return ONE;
			case 1:
				return I;
			case 2:
				return ONE.negate();
			default:
				return I.negate();
			}
		else if (equals(ONE.negate()))
			return exponent % 2 == 0 ? ONE : this;
		else if (equals(ZERO))
			return ZERO;
		else {
			Complex base = this;
			Complex result = ONE;

			while (exponent > 0) {
				if (exponent % 2 == 1) // if exponent is odd
					result = result.multiply(base);

				exponent >>= 1;
				base = base.multiply(base);
			}

			return result;
		}
	}

	/**
	 * Calculates the negative value of this complex number.
	 * 
	 * @return a new complex number that is equal to -(</code>this</code>)
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
	}

	/**
	 * Calculates the multiplicative inverse of this complex number.
	 * 
	 * @return a new complex number that is equal to 1 / (<code>this</code>)
	 */
	public Complex invert() {
		final double denom = real * real + imaginary * imaginary;

		return new Complex(real / denom, -imaginary / denom);
	}

	/**
	 * Calculates the complex conjugate of this number.
	 * 
	 * @return a new complex number that is equal to (<code>real</code>) -
	 *         (<code>imaginary</code>)i
	 */
	public Complex conjugate() {
		return new Complex(real, -imaginary);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o == null || !(o instanceof Complex))
			return false;

		final Complex c = (Complex) o;

		return real == c.real && imaginary == c.imaginary;
	}

	@Override
	public String toString() {
		if (isReal())
			return Double.toString(real);
		else if (isImaginary())
			return imaginary == 1.0 ? "i" : imaginary == -1.0 ? "-i" : imaginary + "i";
		else if (imaginary < 0)
			return real + " - " + (imaginary == -1.0 ? "" : -imaginary) + "i";
		else
			return real + " + " + (imaginary == 1.0 ? "" : imaginary) + "i";
	}

	/**
	 * @return true if and only if the imaginary part of this complex number is
	 *         equal to exact zero
	 */
	public boolean isReal() {
		return imaginary == 0.0;
	}

	/**
	 * @return true if and only if the real part of this complex number is equal to
	 *         exact zero. Zero itself is an imaginary number.
	 */
	public boolean isImaginary() {
		return real == 0.0;
	}

	/**
	 * @return the real part of this complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * @return the imaginary part of this complex number
	 */
	public double getImaginary() {
		return imaginary;
	}

}
