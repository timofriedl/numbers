package com.timofriedl.numbers.complexrational;

import java.math.BigInteger;

import com.timofriedl.numbers.bigrational.BigRational;

/**
 * Immutable big exact complex numbers that are built from a pair of two
 * {@link BigRational}s.
 * 
 * Only supports complex numbers that can be represented as (x + yi) where x, y
 * are instances of {@link BigRational}.
 * 
 * @see BigRational
 * @see Complex
 * @author Timo Friedl
 */
public class ComplexRational {

	/**
	 * the complex rational number constant 0
	 */
	public static final ComplexRational ZERO = new ComplexRational(BigRational.ZERO);

	/**
	 * the complex rational number constant 1/2
	 */
	public static final ComplexRational ONE_HALF = new ComplexRational(BigRational.ONE_HALF);

	/**
	 * the complex rational number constant 1/10
	 */
	public static final ComplexRational ONE_TENTH = new ComplexRational(BigRational.ONE_TENTH);

	/**
	 * the complex rational number constant 1
	 */
	public static final ComplexRational ONE = valueOf(1);

	/**
	 * the complex rational number constant 2
	 */
	public static final ComplexRational TWO = valueOf(2);

	/**
	 * the complex rational number constant 10
	 */
	public static final ComplexRational TEN = valueOf(10);

	/**
	 * the imaginary constant i where i^2 = -1
	 */
	public static final ComplexRational I = valueOf(0, 1);

	/**
	 * the real and imaginary part of this complex number
	 */
	private final BigRational real, imaginary;

	/**
	 * Creates a new complex number that is limited to a rational real and imaginary
	 * part, stored as a {@link BigRational} each.
	 * 
	 * @param real      the real part of this complex number
	 * @param imaginary the imaginary part of this complex number
	 */
	public ComplexRational(BigRational real, BigRational imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Creates a new complex number from one {@link BigRational} number.
	 * 
	 * The result will be a rational number stored as a complex number.
	 * 
	 * @param real the real part of this complex number
	 */
	public ComplexRational(BigRational real) {
		this.real = real;
		this.imaginary = BigRational.ZERO;
	}

	/**
	 * Creates a new complex number from a {@link BigInteger}.
	 * 
	 * The result will be an integer stored as a complex number with rational parts.
	 * 
	 * @param integer the integer value of this complex number
	 */
	public ComplexRational(BigInteger integer) {
		this(new BigRational(integer));
	}

	/**
	 * Creates a new complex number from one <code>long</code> value.
	 * 
	 * The result will be a rational number stored as a complex number.
	 * 
	 * @param real the real part of this complex number
	 * @return the created complex number
	 */
	public static ComplexRational valueOf(long real) {
		return new ComplexRational(BigRational.valueOf(real));
	}

	/**
	 * Creates a new complex number from <code>long</code> values.
	 * 
	 * @param real      the real part of this complex number
	 * @param imaginary the imaginary part of this complex number
	 * @return the created complex number
	 */
	public static ComplexRational valueOf(long real, long imaginary) {
		return new ComplexRational(BigRational.valueOf(real), BigRational.valueOf(imaginary));
	}

	/**
	 * Creates a new complex number from <code>double</code> values.
	 * 
	 * @param real      the real part of this complex number
	 * @param imaginary the imaginary part of this complex number
	 * @return the created complex number
	 */
	public static ComplexRational valueOf(double real, double imaginary) {
		return new ComplexRational(BigRational.valueOf(real), BigRational.valueOf(imaginary));
	}

	/**
	 * Creates a new complex number from one <code>double</code> value.
	 * 
	 * The result will be a rational number stored as a complex number.
	 * 
	 * @param real the real part of this complex number
	 * @return the created complex number
	 */
	public static ComplexRational valueOf(double real) {
		return new ComplexRational(BigRational.valueOf(real));
	}

	/**
	 * Calculates the arithmetic sum with another {@link ComplexRational}.
	 * 
	 * @param addend the number to add to this number
	 * @return a new complex number that is equal to the sum of <code>this</code>
	 *         and <code>addend</code>
	 */
	public ComplexRational add(ComplexRational addend) {
		return new ComplexRational(real.add(addend.real), imaginary.add(addend.imaginary));
	}

	/**
	 * Calculates the arithmetic difference to another {@link ComplexRational}.
	 * 
	 * @param subtrahend the number to subtract from this number
	 * @return a new complex number that is equal to the difference of
	 *         <code>this</code> and <code>subtrahend</code>
	 */
	public ComplexRational subtract(ComplexRational subtrahend) {
		return new ComplexRational(real.subtract(subtrahend.real), imaginary.subtract(subtrahend.imaginary));
	}

	/**
	 * Calculates the arithmetic product with another {@link ComplexRational}.
	 * 
	 * @param factor the number to multiply with this number
	 * @return a new complex number that is equal to the product of
	 *         <code>this</code> and <code>factor</code>
	 */
	public ComplexRational multiply(ComplexRational factor) {
		return new ComplexRational(real.multiply(factor.real).subtract(imaginary.multiply(factor.imaginary)),
				real.multiply(factor.imaginary).add(imaginary.multiply(factor.real)));
	}

	/**
	 * Calculates the arithmetic quotient with another {@link ComplexRational}.
	 * 
	 * @param divisor the number to divide this number by
	 * @return a new complex number that is equal to the quotient of
	 *         <code>this</code> and <code>divisor</code>
	 */
	public ComplexRational divide(ComplexRational divisor) {
		final BigRational denom = divisor.real.multiply(divisor.real)
				.add(divisor.imaginary.multiply(divisor.imaginary));

		return new ComplexRational(real.multiply(divisor.real).add(imaginary.multiply(divisor.imaginary)).divide(denom),
				imaginary.multiply(divisor.real).subtract(real.multiply(divisor.imaginary)).divide(denom));
	}

	/**
	 * Raises this complex number to the power of a given {@link BigInteger} and
	 * returns the result.
	 * 
	 * The time complexity of this function is O(log |exponent|).
	 * 
	 * Special case: If <code>this</code> is an element of {-1, 0, 1, i} or
	 * <code>exponent</code> is equal to zero, time complexity is O(1).
	 * 
	 * This function is implemented iteratively, not recoursively. Therefore, large
	 * number calculations only consume constant amount of storage. But:
	 * {@link BigInteger} has a max value, therefore arbitrary large numbers are not
	 * possible.
	 * 
	 * @return this number to the power of the exponent
	 */
	public ComplexRational pow(BigInteger exponent) {
		final int signum = exponent.signum();

		if (signum == 0)
			return ONE;
		else if (signum == -1)
			return invert().pow(exponent.negate());
		else if (equals(ONE))
			return ONE;
		else if (equals(I))
			switch (exponent.mod(BigInteger.valueOf(4)).intValue()) {
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
			return exponent.getLowestSetBit() == 0 ? this : ONE;
		else if (equals(ZERO))
			return ZERO;
		else {
			ComplexRational base = this;
			ComplexRational result = ONE;

			while (exponent.signum() == 1) {
				if (exponent.getLowestSetBit() == 0) // if exponent is odd
					result = result.multiply(base);

				exponent = exponent.shiftRight(1);
				base = base.multiply(base);
			}

			return result;
		}
	}

	/**
	 * Raises this complex number to the power of a given long value and returns the
	 * result.
	 * 
	 * @return this number to the power of the exponent
	 */
	public ComplexRational pow(long exponent) {
		return pow(BigInteger.valueOf(exponent));
	}

	/**
	 * Calculates the negative value of this complex number.
	 * 
	 * @return a new complex number that is equal to -(</code>this</code>)
	 */
	public ComplexRational negate() {
		return new ComplexRational(real.negate(), imaginary.negate());
	}

	/**
	 * Calculates the multiplicative inverse of this complex number.
	 * 
	 * @return a new complex number that is equal to 1 / (<code>this</code>)
	 */
	public ComplexRational invert() {
		final BigRational denom = real.multiply(real).add(imaginary.multiply(imaginary));

		return new ComplexRational(real.divide(denom), imaginary.divide(denom).negate());
	}

	/**
	 * Calculates the complex conjugate of this number.
	 * 
	 * @return a new complex number that is equal to (<code>real</code>) -
	 *         (<code>imaginary</code>)i
	 */
	public ComplexRational conjugate() {
		return new ComplexRational(real, imaginary.negate());
	}

	/**
	 * Reduces the fractions of this tuple of {@link BigRational}.
	 * 
	 * @return the reduced representation of this number
	 */
	public ComplexRational reduce() {
		return new ComplexRational(real.reduce(), imaginary.reduce());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o == null || !(o instanceof ComplexRational))
			return false;

		final ComplexRational c = (ComplexRational) o;

		return real.equals(c.real) && imaginary.equals(c.imaginary);
	}

	@Override
	public String toString() {
		if (isReal())
			return real.toString();
		else if (isImaginary())
			return imaginary.equals(BigRational.ONE) ? "i"
					: imaginary.equals(BigRational.ONE.negate()) ? "-i" : imaginary + "i";
		else if (imaginary.signum() < 0) {
			return real + " - " + (imaginary.equals(BigRational.ONE.negate()) ? "" : imaginary.negate()) + "i";
		} else
			return real + " + " + (imaginary.equals(BigRational.ONE) ? "" : imaginary) + "i";
	}

	/**
	 * Returns true if and only if the imaginary part of this complex number is
	 * equal to zero.
	 * 
	 * Zero is defined to be both real and imaginary.
	 * 
	 * @return true if this complex number is a real number, false else
	 */
	public boolean isReal() {
		return imaginary.signum() == 0;
	}

	/**
	 * Returns true if and only if the real part of this complex number is equal to
	 * zero.
	 * 
	 * Zero is defined to be both real and imaginary.
	 * 
	 * @return true if this complex number is a multiple of i, false else
	 */
	public boolean isImaginary() {
		return real.signum() == 0;
	}

	/**
	 * @return the real part of this complex number
	 */
	public BigRational getReal() {
		return real;
	}

	/**
	 * @return the imaginary part of this complex number
	 */
	public BigRational getImaginary() {
		return imaginary;
	}

}
