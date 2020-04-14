package com.timofriedl.numbers.complex;

/**
 * A utility class for common complex functions.
 * 
 * These functions are non-exact.
 * 
 * @see Complex
 * @author Timo Friedl
 */
public abstract class ComplexMath {

	/**
	 * Raises a {@link Complex} number to the power of another given complex number
	 * and returns the result.
	 * 
	 * @param base     the base of this power
	 * @param exponent the exponent of this power
	 * @return the base number to the power of the exponent
	 */
	public static Complex pow(Complex base, Complex exponent) {
		if (exponent.equals(Complex.ZERO))
			return Complex.ONE;
		if (base.equals(Complex.ONE))
			return Complex.ONE;
		else if (base.equals(Complex.E))
			return exp(exponent);

		return exp(exponent.multiply(log(base)));
	}

	/**
	 * Calculates the square root of a given complex number.
	 * 
	 * @param z the given complex number
	 * @return the square root of <code>z</code>
	 */
	public static Complex sqrt(Complex z) {
		if (z.isReal())
			return z.getReal() > 0.0 ? new Complex(Math.sqrt(z.getReal())) : new Complex(0.0, Math.sqrt(-z.getReal()));

		return root(2, z);
	}

	/**
	 * Calculates the cube root of a given complex number.
	 * 
	 * @param z the given complex number
	 * @return the cube root of <code>z</code>
	 */
	public static Complex cbrt(Complex z) {
		return root(3, z);
	}

	/**
	 * Calculates the <code>n</code>-th root of a given complex number
	 * <code>z</code>.
	 * 
	 * @param n
	 * @param z the given complex number
	 * @return the <code>n</code>-th root of <code>z</code>
	 */
	private static Complex root(int n, Complex z) {
		return pow(z, new Complex(1.0 / n));
	}

	/**
	 * The complex exponential function.
	 * 
	 * exp(z) = exp(a + bi) := exp(a) * (cos b + i sin b)
	 * 
	 * @param exponent the exponent value
	 * @return the result of e^<code>exponent</code>
	 */
	public static Complex exp(Complex exponent) {
		if (exponent.equals(Complex.ZERO))
			return Complex.ONE;
		if (exponent.equals(Complex.ONE))
			return Complex.E;
		if (exponent.isImaginary())
			return euler(exponent.getImaginary());

		return new Complex(Math.exp(exponent.getReal()))
				.multiply(new Complex(Math.cos(exponent.getImaginary()), Math.sin(exponent.getImaginary())));
	}

	/**
	 * Returns the result of exp(i*x) where
	 * 
	 * exp(i*x) = cos x + i sin x
	 * 
	 * @param x the input value x
	 * @return exp(i*<code>x</code>
	 */
	private static Complex euler(double x) {
		if (x % (Math.PI * 2.0) == 0.0)
			return Complex.ONE;
		if (x % (Math.PI * 2.0) == 0.5 * Math.PI)
			return Complex.I;
		if (x % (Math.PI * 2.0) == Math.PI)
			return Complex.ONE.negate();
		if (x % (Math.PI * 2.0) == 1.5 * Math.PI)
			return Complex.I.negate();

		return new Complex(Math.cos(x), Math.sin(x));
	}

	/**
	 * Calculates the complex natural logarithm of a given complex number.
	 * 
	 * Log(z) := log|z| + Arg(z)i
	 * 
	 * @return
	 */
	public static Complex log(Complex z) {
		if (z.equals(Complex.ZERO))
			throw new ArithmeticException("Log(0) is undefined.");

		return new Complex(Math.log(abs(z)), arg(z));
	}

	/**
	 * Calculates the complex logarithm with a given base of a given complex number.
	 * 
	 * @param base the base of this logarithm
	 * @param z    the argument of this logarithm
	 * @return log_<code>base</code>(<code>z</code>)
	 */
	public static Complex log(Complex base, Complex z) {
		if (base.equals(Complex.ONE))
			throw new ArithmeticException("Log_1(z) is undefined.");

		return log(z).divide(log(base));
	}

	/**
	 * Calculates the absolute value of a given complex number.
	 * 
	 * |z| = |a + bi| := sqrt(a^2 + b^2)
	 * 
	 * @param z the given complex number
	 * @return the absolute value of <code>z</code>
	 */
	public static double abs(Complex z) {
		return Math.sqrt(z.getReal() * z.getReal() + z.getImaginary() * z.getImaginary());
	}

	/**
	 * Calculates the angle of a given complex number.
	 * 
	 * Arg(z) = Arg(a + bi) := atan2(b, a)
	 * 
	 * @param z the given complex number
	 * @return the argument of <code>z</code>
	 */
	public static double arg(Complex z) {
		return Math.atan2(z.getImaginary(), z.getReal());
	}

	/**
	 * Calculates the sine of a given complex number.
	 * 
	 * sin(z) = sin(a + bi) = sin(a)cosh(b) + i cos(a)sinh(b)
	 * 
	 * @param z the given complex number
	 * @return the sine of <code>z</code>
	 */
	public static Complex sin(Complex z) {
		return new Complex(Math.sin(z.getReal()) * Math.cosh(z.getImaginary()),
				Math.cos(z.getReal()) * Math.sinh(z.getImaginary()));
	}

	/**
	 * Calculates the cosine of a given complex number.
	 * 
	 * cos(z) = cos(a + bi) = cos(a)cosh(b) - i sin(a)*sinh(b)
	 * 
	 * @param z the given complex number
	 * @return the cosine of <code>z</code>
	 */
	public static Complex cos(Complex z) {
		return new Complex(Math.cos(z.getReal()) * Math.cosh(z.getImaginary()),
				-Math.sin(z.getReal()) * Math.sinh(z.getImaginary()));
	}

	/**
	 * Calculates the tangent of a given complex number.
	 * 
	 * ..........sin(2 Re(z)) + i sinh(2 Im(z))....</br>
	 * tan(z) = --------------------------------...</br>
	 * ............cos(2 Re(z) + cosh(2 Im(z)......</br>
	 * 
	 * @param z the given complex number
	 * @return the tangent of <code>z</code>
	 */
	public static Complex tan(Complex z) {
		final double denom = Math.cos(2 * z.getReal()) + Math.cosh(2 * z.getImaginary());

		if (denom == 0.0)
			throw new ArithmeticException("tan(" + z + ") is undefined.");

		return new Complex(Math.sin(2 * z.getReal()) / denom, Math.sinh(2 * z.getImaginary()) / denom);
	}

	/**
	 * Calculates the arc sine of a given complex number.
	 * 
	 * asin(z) := -i Log(iz + sqrt(1 - z^2))
	 * 
	 * @param z the given complex number
	 * @return the arc sine of <code>z</code>
	 */
	public static Complex asin(Complex z) {
		return Complex.I.negate().multiply(log(z.multiply(Complex.I).add(sqrt(Complex.ONE.subtract(z.pow(2))))));
	}

	/**
	 * Calculates the arc cosine of a given complex number.
	 * 
	 * asin(z) := -i Log(z + i sqrt(1 - z^2))
	 * 
	 * @param z the given complex number
	 * @return the arc cosine of <code>z</code>
	 */
	public static Complex acos(Complex z) {
		return Complex.I.negate().multiply(log(z.add(Complex.I.multiply(sqrt(Complex.ONE.subtract(z.pow(2)))))));
	}

	/**
	 * Calculates the arc tangent of a given complex number.
	 * 
	 * atan(z) := 1/2 i Log(1 - iz) - 1/2 i Log(1 + iz)
	 * 
	 * @param z the given complex number
	 * @return the arc cosine of <code>z</code>
	 */
	public static Complex atan(Complex z) {
		return halfILog(
				Complex.ONE.subtract(Complex.I.multiply(z))).subtract(halfILog(Complex.ONE.add(Complex.I.multiply(z))));
	}

	/**
	 * Helper function.
	 * 
	 * @see #atan(Complex)
	 * @param z
	 * @return 1/2 * i * Log z
	 */
	private static Complex halfILog(Complex z) {
		return new Complex(0.0, 0.5).multiply(log(z));
	}

	/**
	 * Calculates the hyperbolic sine of a given complex number.
	 * 
	 * sinh(z) := e^z / 2 - e^-z / 2
	 * 
	 * @param z the given complex number
	 * @return the hyperbolic sine of <code>z</code>
	 */
	public static Complex sinh(Complex z) {
		return exp(z).subtract(exp(z.negate())).multiply(Complex.ONE_HALF);
	}

	/**
	 * Calculates the hyperbolic cosine of a given complex number.
	 * 
	 * cosh(z) := e^z / 2 + e^-z / 2
	 * 
	 * @param z the given complex number
	 * @return the hyperbolic cosine of <code>z</code>
	 */
	public static Complex cosh(Complex z) {
		return exp(z).add(exp(z.negate())).multiply(Complex.ONE_HALF);
	}

	/**
	 * Calculates the hyperbolic tangent of a given complex number.
	 * 
	 * .............tanh Re(z) + i tan Im(z).........</br>
	 * tanh(z) := ----------------------------.......</br>
	 * ............1 + i tanh Re(z) tan Im(z)........</br>
	 * 
	 * @param z the given complex number
	 * @return the hyperbolic tangent of <code>z</code>
	 */
	public static Complex tanh(Complex z) {
		final double tanhReZ = Math.tanh(z.getReal());
		final double tanImZ = Math.tan(z.getImaginary());

		return new Complex(tanhReZ, tanImZ).divide(new Complex(1.0, tanhReZ * tanImZ));
	}

	public static void main(String[] args) {
		System.out.println(new Complex(3, 5).conjugate());
	}

}
