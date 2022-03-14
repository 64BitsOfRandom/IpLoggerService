package com.example.iplogger.domain.suppliers;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public class RandomCharSequenceSupplier implements Supplier<String> {
    private final int SEQ_SIZE;
    private final Random RANDOM;

    public RandomCharSequenceSupplier(int size) {
        this(new SecureRandom(), size);
    }

    public RandomCharSequenceSupplier(Random random, int size) {
        this.RANDOM = random;
        this.SEQ_SIZE = size;
    }

    @Override
    public String get() {
        IntPredicate predicate = new AlphaNumericPredicate();
        return RANDOM.ints(46, 256)
                .filter(predicate)
                .limit(SEQ_SIZE)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private class AlphaNumericPredicate implements IntPredicate {
        @Override
        public boolean test(int value) {
            return value > 64 && value < 91 || // nums
                   value > 96 && value < 123 || //A-Z
                   value > 47 && value < 58;
        }
    }
}
