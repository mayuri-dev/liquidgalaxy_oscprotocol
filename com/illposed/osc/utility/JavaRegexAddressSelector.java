package com.illposed.osc.utility;

import com.illposed.osc.AddressSelector;
import java.util.regex.Pattern;

public class JavaRegexAddressSelector implements AddressSelector {
    private final Pattern selector;

    public JavaRegexAddressSelector(Pattern selector2) {
        this.selector = selector2;
    }

    public JavaRegexAddressSelector(String selectorRegex) {
        this(Pattern.compile(selectorRegex));
    }

    public boolean matches(String messageAddress) {
        return this.selector.matcher(messageAddress).matches();
    }
}
