package ca.com.arnon.utils;

public class MathOperations {

	public Double sum(String number1, String number2) {
		return MathValidators.convertoToDouble(number1) + MathValidators.convertoToDouble(number2);
	}

	public Double sub(String number1, String number2) throws Exception {
		return MathValidators.convertoToDouble(number1) - MathValidators.convertoToDouble(number2);
	}

	public Double multiply(String number1, String number2) throws Exception {
		return MathValidators.convertoToDouble(number1) * MathValidators.convertoToDouble(number2);
	}

	public Double division(String number1, String number2) throws Exception {
		return MathValidators.convertoToDouble(number1) / MathValidators.convertoToDouble(number2);
	}

	public Double average(String number1, String number2) throws Exception {
		return (MathValidators.convertoToDouble(number1) + MathValidators.convertoToDouble(number2)) / 2;
	}

	public Double squareroot(String number) throws Exception {
		return Math.sqrt(MathValidators.convertoToDouble(number));
	}

}
