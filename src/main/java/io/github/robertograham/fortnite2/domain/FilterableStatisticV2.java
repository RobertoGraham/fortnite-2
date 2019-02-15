package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.InputType;

/**
 * A {@link Statistic} that can be filtered by {@link io.github.robertograham.fortnite2.domain.enumeration.InputType} and by mode
 *
 * @since 3.1.0
 */
public interface FilterableStatisticV2 extends Statistic {

    /**
     * @param inputTypes the types of input used when the stats were collected
     * @return a {@link FilterableStatisticV2} with values scoped to {@code inputTypes}
     * @throws NullPointerException if {@code inputTypes} is {@code null} or any of {@code inputTypes}' values are {@code null}
     * @since 3.1.0
     */
    FilterableStatisticV2 byInputTypes(final InputType... inputTypes);

    /**
     * @param modes the types of modes the returned stats should be related to
     * @return a {@link FilterableStatisticV2} with values scoped to {@code modes}
     * @throws NullPointerException if {@code modes} is {@code null} or any of {@code modes}' values are {@code null}
     * @since 3.1.0
     */
    FilterableStatisticV2 byModes(final String... modes);
}
