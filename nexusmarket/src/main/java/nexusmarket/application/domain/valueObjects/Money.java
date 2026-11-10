package nexusmarket.application.domain.valueobjects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nexusmarket.application.domain.exceptions.CurrencyMismatchException;
import nexusmarket.application.domain.exceptions.InvalidMoneyException;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new InvalidMoneyException("El monto no puede ser nulo.");
        }
        if (currency == null) {
            throw new InvalidMoneyException("La moneda no puede ser nula.");
        }
        if (amount.signum() < 0) {
            throw new InvalidMoneyException("El monto no puede ser negativo.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean hasSameCurrency(Currency other) {
        return currency.equals(other);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new InvalidMoneyException("El factor de multiplicacion no puede ser nulo.");
        }
        if (factor.signum() < 0) {
            throw new InvalidMoneyException("El factor de multiplicacion no puede ser negativo.");
        }
        return new Money(amount.multiply(factor), currency);
    }

    private void requireSameCurrency(Money other) {
        if (other == null) {
            throw new InvalidMoneyException("El monto a operar no puede ser nulo.");
        }
        if (!hasSameCurrency(other.currency)) {
            throw new CurrencyMismatchException("Las monedas no coinciden: "
                    + currency.getCode() + " vs " + other.currency.getCode() + ".");
        }
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCode();
    }
}
