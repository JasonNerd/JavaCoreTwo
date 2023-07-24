package optional;

import java.util.Optional;

public class OpCreate {
    public static Optional<Double> inverse(double d){
        return d == 0 ? Optional.empty() : Optional.of(1/d);
    }

    public static Optional<Double> squareRoot(double d){
        return d < 0 ? Optional.empty() : Optional.of(Math.sqrt(d));
    }

    public static void main(String[] args) {
        System.out.println(squareRoot(12));
        System.out.println(inverse(12));
        System.out.println(squareRoot(-12));
        double d = 3.14;
        Optional<Double> res = inverse(d)
                .flatMap(OpCreate::squareRoot)
                .flatMap(OpCreate::inverse);
        System.out.println(res);
    }
}
