package br.estudo._FirstStepsJavaWithSpringBoot;

// import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.estudo._FirstStepsJavaWithSpringBoot.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController {
  
  // private static final String template = "Hello, %s";
  // private final AtomicLong counter = new AtomicLong();

  @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
  public Double sum(
    @PathVariable(value = "numberOne") String numberOne,
    @PathVariable(value = "numberTwo") String numberTwo
  ) throws Exception  {
    if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    return convertDouble(numberOne) + convertDouble(numberTwo);
  }

  @RequestMapping(value = "/sub/{numberOne}/{numberTwo}", method=RequestMethod.GET)
  public Double sub(
    @PathVariable(value = "numberOne") String numberOne,
    @PathVariable(value = "numberTwo") String numberTwo
  ) throws Exception  {
    if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    return convertDouble(numberOne) - convertDouble(numberTwo);
  }

  @RequestMapping(value = "/mult/{numberOne}/{numberTwo}", method=RequestMethod.GET)
  public Double mult(
    @PathVariable(value = "numberOne") String numberOne,
    @PathVariable(value = "numberTwo") String numberTwo
  ) throws Exception  {
    if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    return convertDouble(numberOne) * convertDouble(numberTwo);
  }

  @RequestMapping(value = "/div/{numberOne}/{numberTwo}", method=RequestMethod.GET)
  public Double div(
    @PathVariable(value = "numberOne") String numberOne,
    @PathVariable(value = "numberTwo") String numberTwo
  ) throws Exception  {
    if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    Double numberTwoAux = convertDouble(numberTwo);
    if (numberTwoAux == 0.0) {
      throw new UnsupportedMathOperationException("Not divide to zero");
    }
    return convertDouble(numberOne) / numberTwoAux;
  }

  @RequestMapping(value = "/root/{numberOne}", method=RequestMethod.GET)
  public Double root(
    @PathVariable(value = "numberOne") String numberOne
  ) throws Exception  {
    if (!isNumeric(numberOne)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    return Math.sqrt(convertDouble(numberOne));
  }

  @RequestMapping(value = "/avg/{numberOne}/{numberTwo}", method=RequestMethod.GET)
  public Double avg(
    @PathVariable(value = "numberOne") String numberOne,
    @PathVariable(value = "numberTwo") String numberTwo
  ) throws Exception  {
    if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
      throw new UnsupportedMathOperationException("Please set a numeric value");
    }
    return (convertDouble(numberOne) + convertDouble(numberTwo)) / 2;
  }

  private Double convertDouble(String strNumber) {
    if (strNumber == null) return 0D;
    String number = strNumber.replaceAll(",", ".");
    if (isNumeric(number)) return Double.parseDouble(number);
    return null;
  }

  private boolean isNumeric(String strNumber) {
    if (strNumber == null) return false;
    String number = strNumber.replaceAll(",", ".");
    return number.matches("[-+]?[0-9]*\\.?[0-9]+");
  }

}
