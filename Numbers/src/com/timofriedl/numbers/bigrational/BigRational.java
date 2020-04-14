package com.timofriedl.numbers.bigrational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import com.timofriedl.numbers.complex.Complex;
import com.timofriedl.numbers.complexrational.ComplexRational;

/**
 * Immutable exact big rational numbers, represented as a fraction of two
 * {@link BigInteger}s.
 * 
 * @see BigInteger
 * @see ComplexRational for exact complex rational numbers
 * @see Complex for approximate complex numbers
 * @author Timo Friedl
 */
public class BigRational extends Number implements Comparable<BigRational> {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = -1676321225962000059L;

	/**
	 * the BigRational constant zero
	 */
	public static final BigRational ZERO = BigRational.valueOf(0l);

	/**
	 * the BigRational constant 1/10
	 */
	public static final BigRational ONE_TENTH = BigRational.valueOf(1l, 10l);

	/**
	 * the BigRational constant 1/2
	 */
	public static final BigRational ONE_HALF = BigRational.valueOf(1l, 2l);

	/**
	 * the BigRational constant one
	 */
	public static final BigRational ONE = BigRational.valueOf(1l);

	/**
	 * the BigRational constant two
	 */
	public static final BigRational TWO = BigRational.valueOf(2l);

	/**
	 * the BigRational constant ten
	 */
	public static final BigRational TEN = BigRational.valueOf(10l);

	/**
	 * the numerator and denominator of this fraction
	 */
	private final BigInteger numerator, denominator;

	/**
	 * Creates a new immutable rational number instance as a fraction of two
	 * {@link BigInteger}s.
	 * 
	 * @param numerator   the divident of this fraction
	 * @param denominator the divisor of this fraction
	 */
	public BigRational(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;

		if (denominator.equals(BigInteger.ZERO))
			throw new IllegalArgumentException("Rational numbers must not have a denominator of zero.");
		else
			this.denominator = denominator;
	}

	/**
	 * Creates a new immutable rational number instance from two number
	 * {@link String}s.
	 * 
	 * Example: <code>new BigRational("7", "10")</code> creates the fraction 7/10.
	 * 
	 * @param numerator   the divident of this fraction
	 * @param denominator the divisor of this fraction
	 */
	public BigRational(String numerator, String denominator) {
		this(new BigInteger(numerator), new BigInteger(denominator));
	}

	/**
	 * Creates a new immutable rational number instance from a {@link BigInteger}.
	 * 
	 * @param integer the integer value of this fraction
	 */
	public BigRational(BigInteger integer) {
		this(integer, BigInteger.ONE);
	}

	/**
	 * Creates a new immutable rational number instance from two long integers.
	 * 
	 * Example: <code>BigRational.valueOf(7l, 10l)</code> creates the fraction 7/10.
	 * 
	 * @param numerator   the divident of this fraction
	 * @param denominator the divisor of this fraction
	 * @return a new rational number, represented as a fraction of the fraction of
	 *         {@code numerator} and {@code denominator}
	 */
	public static BigRational valueOf(long numerator, long denominator) {
		return new BigRational(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a new immutable rational number instance with the value of a given
	 * <code>long</code>.
	 * 
	 * Example: <code>BigRational.valueOf(3l)</code> creates the fraction 3/1.
	 * 
	 * @param value the double value of the rational number
	 * @return a new rational number, represented as a fraction of {@code value}
	 *         divided by one
	 */
	public static BigRational valueOf(long value) {
		return valueOf(value, 1l);
	}

	/**
	 * Creates a new immutable rational number instance with the value of a given
	 * <code>double</code>.
	 * 
	 * Example: <code>BigRational.valueOf(.33)</code> creates the fraction 33/100.
	 * 
	 * @param value the double value of the rational number
	 * @return a new rational number, represented as a fraction of two
	 *         {@link BigInteger}s
	 */
	public static BigRational valueOf(double value) {
		final String s = String.valueOf(value);
		final int digitsDec = s.length() - 1 - s.indexOf('.');

		BigInteger denominator = BigInteger.ONE;

		for (int i = 0; i < digitsDec; i++) {
			value *= 10;
			denominator = denominator.multiply(BigInteger.TEN);
		}

		final BigInteger numerator = BigInteger.valueOf((long) Math.round(value));

		return new BigRational(numerator, denominator);
	}

	/**
	 * Reduces this fraction using the GCD algorithm of {@link BigInteger}.
	 * 
	 * Example: <code>new BigRational(-4l, -2l).reduce()</code> evaluates to the
	 * fraction 2/1.
	 * 
	 * @return the reduced fraction
	 */
	public BigRational reduce() {
		final BigInteger gcd = numerator.gcd(denominator);

		return new BigRational(numerator.abs().divide(gcd).multiply(BigInteger.valueOf(signum())),
				denominator.abs().divide(gcd));
	}

	/**
	 * Adds this number to another given {@link BigRational} and returns the result
	 * as a new instance.
	 * 
	 * @return the sum of the two numbers
	 */
	public BigRational add(BigRational addend) {
		return new BigRational(numerator.multiply(addend.denominator).add(addend.numerator.multiply(denominator)),
				denominator.multiply(addend.denominator));
	}

	/**
	 * Subtracts another given {@link BigRational} from this one and returns the
	 * result as a new instance.
	 * 
	 * @return the difference of the two numbers
	 */
	public BigRational subtract(BigRational subtrahend) {
		return new BigRational(
				numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(denominator)),
				denominator.multiply(subtrahend.denominator));
	}

	/**
	 * Multiplies this number to another another given {@link BigRational} and
	 * returns the result as a new instance.
	 * 
	 * @return the product of the two numbers
	 */
	public BigRational multiply(BigRational factor) {
		return new BigRational(numerator.multiply(factor.numerator), denominator.multiply(factor.denominator));
	}

	/**
	 * Divides this number through another another given {@link BigRational} and
	 * returns the result as a new instance.
	 * 
	 * @return the quotient of the two numbers
	 */
	public BigRational divide(BigRational divisor) {
		if (divisor.equals(ZERO))
			throw new ArithmeticException("Tried to divide a rational number by zero.");

		return new BigRational(numerator.multiply(divisor.denominator), denominator.multiply(divisor.numerator));
	}

	/**
	 * Raises this number to the power of a given {@link BigInteger} and returns the
	 * result.
	 * 
	 * The time complexity of this function is O(log |exponent|).
	 * 
	 * Special case: If <code>this</code> is an element of {-1, 0, 1} or
	 * <code>exonent</code> is equal to zero, time complexity is O(1).
	 * 
	 * This function is implemented iteratively, not recoursively. Therefore, large
	 * number calculations only consume constant amount of storage. But:
	 * {@link BigInteger} has a max value, therefore arbitrary large numbers are not
	 * possible.
	 * 
	 * @return this number to the power of the exponent
	 */
	public BigRational pow(BigInteger exponent) {
		final int signum = exponent.signum();

		if (signum == 0)
			return ONE;
		else if (signum == -1)
			return invert().pow(exponent.negate());
		else if (equals(ONE))
			return ONE;
		else if (equals(ONE.negate()))
			return exponent.getLowestSetBit() == 0 ? ONE : this;
		else if (equals(ZERO))
			return ZERO;
		else {
			BigRational base = this;
			BigRational result = ONE;

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
	 * Raises this number to the power of a given long value and returns the result.
	 * 
	 * @return this number to the power of the exponent
	 */
	public BigRational pow(long exponent) {
		return pow(BigInteger.valueOf(exponent));
	}

	/**
	 * Returns a {@link BigRational} with the absolute value of this number.
	 * 
	 * @return the absolute value number
	 */
	public BigRational abs() {
		return signum() == -1 ? negate() : this;
	}

	/**
	 * Returns a {@link BigRational} with the negative value of this number.
	 * 
	 * @return the negative value number
	 */
	public BigRational negate() {
		return new BigRational(numerator.negate(), denominator);
	}

	/**
	 * Returns a {@link BigRational} with the inverted value of this number. Won't
	 * work if the number is equal to zero.
	 * 
	 * @return the inverted value number
	 */
	public BigRational invert() {
		if (numerator.equals(BigInteger.ZERO))
			throw new ArithmeticException("Tried to invert zero.");

		return new BigRational(denominator, numerator);
	}

	/**
	 * Returns the signum of this number.
	 *
	 * @return -1, 0 or 1 as the value of this {@link BigRational} is negative, zero
	 *         or positive
	 */
	public int signum() {
		return numerator.signum() * denominator.signum();
	}

	/**
	 * Returns the minimum of this BigRational and {@code number}.
	 *
	 * If they are equal, either may be returned.
	 *
	 * @param number value with which the minimum is to be computed
	 * @return the BigRational whose value is the lesser of this BigRational and
	 *         {@code number}
	 */
	public BigRational min(BigRational number) {
		return (compareTo(number) < 0 ? this : number);
	}

	/**
	 * Returns the maximum of this BigRational and {@code number}.
	 *
	 * If they are equal, either may be returned.
	 *
	 * @param number value with which the maximum is to be computed
	 * @return the BigRational whose value is the greater of this BigRational and
	 *         {@code number}
	 */
	public BigRational max(BigRational number) {
		return (compareTo(number) > 0 ? this : number);
	}

	/**
	 * Converts this rational number to a {@link BigInteger}, failing if this
	 * rational number does not represent an integer value.
	 * 
	 * @see #roundToBigInteger()
	 * @return an integer that is equal to this rational number, if possible
	 */
	public BigInteger toBigInteger() {
		if (!denominator.equals(BigInteger.ONE) && numerator.divideAndRemainder(denominator)[1].signum() != 0)
			throw new ArithmeticException(toString() + " cannot be represented as an integer.");

		return roundToBigInteger();
	}

	/**
	 * Rounds this rational number down to a {@link BigInteger}, similar to a
	 * <code>double</code>-to-<code>int</code> cast.
	 * 
	 * @return an integer that is equal to or smaller than this rational number
	 */
	public BigInteger roundToBigInteger() {
		return numerator.divide(denominator);
	}

	/**
	 * Converts this rational number to a {@link BigDecimal}, failing if this
	 * fraction is periodically.
	 * 
	 * @see #roundToBigDecimal(MathContext)
	 * @return a decimal number that is equal to this rational number
	 */
	public BigDecimal toBigDecimal() {
		try {
			return new BigDecimal(numerator).divide(new BigDecimal(denominator));
		} catch (ArithmeticException e) {
			throw new ArithmeticException(toString() + " cannot be represented in decimal.");
		}
	}

	/**
	 * Rounds this rational number to a {@link BigDecimal} with a given precision.
	 * 
	 * @param precision the precision as a {@link MathContext} instance, e.g.
	 *                  <code>MathContext.DECIMAL64</code> for 64 bit precision
	 * @return a decimal number that is equal or close to this rational number
	 */
	public BigDecimal roundToBigDecimal(MathContext precision) {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), precision);
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return numerator.divide(denominator).longValue();
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public double doubleValue() {
		return roundToBigDecimal(MathContext.DECIMAL64).doubleValue();
	}

	@Override
	public int compareTo(BigRational number) {
		return subtract(number).signum();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof BigRational))
			return false;

		return compareTo((BigRational) o) == 0;
	}

	@Override
	public String toString() {
		return denominator.equals(BigInteger.ONE) ? numerator.toString()
				: numerator.signum() == 0 ? "0" : numerator.toString() + "/" + denominator.toString();
	}

	/**
	 * Returns the divident of this fraction.
	 * 
	 * @return the numerator of this fraction
	 */
	public BigInteger getNumerator() {
		return numerator;
	}

	/**
	 * Returns the divisor of this fraction.
	 * 
	 * @return the denominator of this fraction
	 */
	public BigInteger getDenominator() {
		return denominator;
	}

}
