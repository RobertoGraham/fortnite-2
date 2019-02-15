package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.FilterableStatisticV2;
import io.github.robertograham.fortnite2.domain.enumeration.InputType;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

final class DefaultFilterableStatisticV2 extends StatisticFiltererV2 implements FilterableStatisticV2 {

    private static final Function<InputType[], Predicate<RawStatisticV2>> RAW_STATISTIC_PREDICATE_FACTORY_INPUT_TYPE_ARRAY =
        (final var inputTypes) ->
            (final var rawStatistic) -> Arrays.stream(inputTypes)
                .map(InputType::code)
                .anyMatch((final var code) -> code.equals(rawStatistic.inputType()));
    private static final Function<String[], Predicate<RawStatisticV2>> RAW_STATISTIC_PREDICATE_FACTORY_MODE_ARRAY =
        (final var modes) ->
            (final var rawStatistic) -> Arrays.stream(modes)
                .anyMatch((final var mode) -> mode.equals(rawStatistic.mode()));

    DefaultFilterableStatisticV2(final Set<RawStatisticV2> rawStatistics) {
        super(rawStatistics);
    }

    public FilterableStatisticV2 byInputTypes(final InputType... inputTypes) {
        Objects.requireNonNull(inputTypes, "inputTypes cannot be null");
        for (final InputType inputType : inputTypes)
            Objects.requireNonNull(inputType, "inputTypes cannot contain a null value");
        return newFilteredStatistic(
            RAW_STATISTIC_PREDICATE_FACTORY_INPUT_TYPE_ARRAY.apply(inputTypes),
            DefaultFilterableStatisticV2::new
        );
    }

    @Override
    public FilterableStatisticV2 byModes(final String... modes) {
        Objects.requireNonNull(modes, "modes cannot be null");
        for (final String mode : modes)
            Objects.requireNonNull(mode, "modes cannot contain a null value");
        return newFilteredStatistic(
            RAW_STATISTIC_PREDICATE_FACTORY_MODE_ARRAY.apply(modes),
            DefaultFilterableStatisticV2::new
        );
    }

    @Override
    public String toString() {
        return "DefaultFilterableStatisticV2{} " + super.toString();
    }
}
