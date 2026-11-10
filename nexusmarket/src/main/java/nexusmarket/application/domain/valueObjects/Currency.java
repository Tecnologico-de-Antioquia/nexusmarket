package nexusmarket.application.domain.valueobjects;

import lombok.Getter;

import java.util.List;

@Getter
public final class Currency extends DomainCatalog {

    public static final Currency COP = new Currency("COP", "Peso colombiano", "COP", "$");
    public static final Currency USD = new Currency("USD", "Dolar estadounidense", "USD", "$");
    public static final Currency EUR = new Currency("EUR", "Euro", "EUR", "EUR");

    private final String isoCode;
    private final String symbol;

    private Currency(String code, String name, String isoCode, String symbol) {
        super(code, name, name);
        this.isoCode = isoCode;
        this.symbol = symbol;
    }

    public static List<Currency> values() {
        return List.of(COP, USD, EUR);
    }

    public static Currency fromCode(String code) {
        if (code == null) {
            return null;
        }
        return values().stream()
                .filter(currency -> currency.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Currency code: " + code));
    }
}
