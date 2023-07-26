package ca.com.arnon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.com.arnon.exeptions.UnsuportedMathOperationException;
import ca.com.arnon.utils.MathOperations;
import ca.com.arnon.utils.MathValidators;

@RestController
public class MathController {
	
	@GetMapping("/sum/{number1}/{number2}")
	public Double sum(@PathVariable(value = "number1") String number1, @PathVariable(value = "number2") String number2) throws Exception {
		if (!MathValidators.isNumeric(number1) || !MathValidators.isNumeric(number2))
			throw new UnsuportedMathOperationException("Please set a numeric value!");
		return new MathOperations().sum(number1, number2);	
	}
	
	@GetMapping("/sub/{number1}/{number2}")
	public Double sub(@PathVariable(value = "number1") String number1, @PathVariable(value = "number2") String number2) throws Exception {
		if (!MathValidators.isNumeric(number1) || !MathValidators.isNumeric(number2))
			throw new UnsuportedMathOperationException("Please set a numeric value!");

		return new MathOperations().sub(number1, number2);
	}
	
	@GetMapping("/multiply/{number1}/{number2}")
	public Double multiply(@PathVariable(value = "number1") String number1, @PathVariable(value = "number2") String number2) throws Exception {
		if (!MathValidators.isNumeric(number1) || !MathValidators.isNumeric(number2))
			throw new UnsuportedMathOperationException("Please set a numeric value!");

		return new MathOperations().multiply(number1, number2);
	}
	
	@GetMapping("/division/{number1}/{number2}")
	public Double division(@PathVariable(value = "number1") String number1, @PathVariable(value = "number2") String number2) throws Exception {
		if (!MathValidators.isNumeric(number1) || !MathValidators.isNumeric(number2))
			throw new UnsuportedMathOperationException("Please set a numeric value!");
		if (MathValidators.convertoToDouble(number2) == 0D)
			throw new UnsuportedMathOperationException("Cannot divide by zero!");

		return new MathOperations().division(number1, number2);
	}
	
	@GetMapping("/average/{number1}/{number2}")
	public Double average(@PathVariable(value = "number1") String number1, @PathVariable(value = "number2") String number2) throws Exception {
		if (!MathValidators.isNumeric(number1) || !MathValidators.isNumeric(number2))
			throw new UnsuportedMathOperationException("Please set a numeric value!");

		return new MathOperations().average(number1, number2);
	}
	
	@GetMapping("/squareroot/{number}")
	public Double squareroot(@PathVariable(value = "number") String number) throws Exception {
		if (!MathValidators.isNumeric(number))
			throw new UnsuportedMathOperationException("Please set a numeric value!");
		if (MathValidators.convertoToDouble(number) < 0) {
			throw new UnsuportedMathOperationException("Cannot calculate square root of negative numbers");
		}
		return new MathOperations().squareroot(number);	
	}
}