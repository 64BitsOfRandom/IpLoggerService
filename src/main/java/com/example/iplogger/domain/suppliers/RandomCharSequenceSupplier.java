package com.example.iplogger.domain.suppliers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

@Component
public class RandomCharSequenceSupplier implements Supplier<String> {
    @Value("${application.short-link-supplier.length}")
    private int sequenceLength;
    private final Random random;

    public RandomCharSequenceSupplier() {
        random = new SecureRandom();
    }

    @Override
    public String get() {
        IntPredicate predicate = new AlphaNumericPredicate();
        return random.ints(46, 256)
                .filter(predicate)
                .limit(sequenceLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private class AlphaNumericPredicate implements IntPredicate {
        @Override
        public boolean test(int value) {
            return value > 64 && value < 91 || // nums
                   value > 96 && value < 123 || //A-Z
                   value > 47 && value < 58; //a-z
        }
    }
}
