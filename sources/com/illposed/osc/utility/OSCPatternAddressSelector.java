package com.illposed.osc.utility;

import com.illposed.osc.AddressSelector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OSCPatternAddressSelector implements AddressSelector {
    private final List<String> patternParts;

    public OSCPatternAddressSelector(String selector) {
        this.patternParts = splitIntoParts(selector);
    }

    public boolean matches(String messageAddress) {
        return matches(this.patternParts, 0, splitIntoParts(messageAddress), 0);
    }

    private static List<String> splitIntoParts(String addressOrPattern) {
        String str = "/";
        List<String> parts = new ArrayList<>(Arrays.asList(addressOrPattern.split(str, -1)));
        if (addressOrPattern.startsWith(str)) {
            parts.remove(0);
        }
        if (addressOrPattern.endsWith(str)) {
            parts.remove(parts.size() - 1);
        }
        return Collections.unmodifiableList(parts);
    }

    private static boolean matches(List<String> patternParts2, int ppi, List<String> messageAddressParts, int api) {
        while (true) {
            boolean z = false;
            boolean z2 = true;
            if (ppi < patternParts2.size()) {
                boolean pathTraverser = false;
                while (ppi < patternParts2.size() && ((String) patternParts2.get(ppi)).isEmpty()) {
                    ppi++;
                    pathTraverser = true;
                }
                if (!pathTraverser) {
                    boolean z3 = ppi == patternParts2.size();
                    if (api != messageAddressParts.size()) {
                        z2 = false;
                    }
                    if (z3 != z2 || !matches((String) messageAddressParts.get(api), (String) patternParts2.get(ppi))) {
                        return false;
                    }
                    api++;
                    ppi++;
                } else if (ppi == patternParts2.size()) {
                    return true;
                } else {
                    while (api < messageAddressParts.size()) {
                        if (matches((String) messageAddressParts.get(api), (String) patternParts2.get(ppi)) && matches(patternParts2, ppi + 1, messageAddressParts, api + 1)) {
                            return true;
                        }
                        api++;
                    }
                    return false;
                }
            } else {
                if (api == messageAddressParts.size()) {
                    z = true;
                }
                return z;
            }
        }
    }

    private static boolean matches(String str, String p) {
        int si;
        int pi;
        boolean negate;
        int pi2 = 0;
        boolean z = false;
        int si2 = 0;
        while (pi2 < p.length()) {
            if (si == str.length() && p.charAt(pi2) != '*') {
                return false;
            }
            int pi3 = pi2 + 1;
            char c = p.charAt(pi2);
            if (c != '*') {
                if (c != '?') {
                    if (c == '[') {
                        if (p.charAt(pi3) == '!') {
                            negate = true;
                            pi3++;
                        } else {
                            negate = false;
                        }
                        boolean match = false;
                        while (true) {
                            if (match || pi3 >= p.length()) {
                                break;
                            }
                            int pi4 = pi3 + 1;
                            int c2 = p.charAt(pi3);
                            if (pi4 != p.length()) {
                                if (p.charAt(pi4) != '-') {
                                    if (c2 == str.charAt(si)) {
                                        match = true;
                                    }
                                    if (p.charAt(pi4) == ']') {
                                        pi3 = pi4;
                                        break;
                                    } else if (p.charAt(pi4) == str.charAt(si)) {
                                        match = true;
                                        pi3 = pi4;
                                    } else {
                                        pi3 = pi4;
                                    }
                                } else {
                                    pi3 = pi4 + 1;
                                    if (pi3 == p.length()) {
                                        return false;
                                    }
                                    if (p.charAt(pi3) != ']') {
                                        if (str.charAt(si) == c2 || str.charAt(si) == p.charAt(pi3) || (str.charAt(si) > c2 && str.charAt(si) < p.charAt(pi3))) {
                                            match = true;
                                        }
                                    } else if (str.charAt(si) >= c2) {
                                        match = true;
                                    }
                                }
                            } else {
                                return false;
                            }
                        }
                        if (negate == match) {
                            return false;
                        }
                        while (pi3 < p.length() && p.charAt(pi3) != ']') {
                            pi3++;
                        }
                        pi = pi3 + 1;
                        if (pi3 == p.length()) {
                            return false;
                        }
                    } else if (c == '{') {
                        int place = si;
                        int remainder = pi3;
                        while (remainder < p.length() && p.charAt(remainder) != '}') {
                            remainder++;
                        }
                        if (remainder == p.length()) {
                            return false;
                        }
                        int remainder2 = remainder + 1;
                        pi = pi3 + 1;
                        char c3 = p.charAt(pi3);
                        while (true) {
                            if (pi > p.length()) {
                                continue;
                                break;
                            }
                            if (c3 == ',') {
                                if (matches(str.substring(si), p.substring(remainder2))) {
                                    return true;
                                }
                                si = place;
                                int pi5 = pi + 1;
                                if (pi == p.length()) {
                                    return false;
                                }
                                pi = pi5;
                            } else if (c3 == '}') {
                                if (pi == p.length() && si == str.length()) {
                                    return true;
                                }
                                si--;
                            } else if (c3 == str.charAt(si)) {
                                si++;
                                if (si == str.length() && remainder2 < p.length()) {
                                    return false;
                                }
                            } else {
                                si = place;
                                while (pi < p.length() && p.charAt(pi) != ',' && p.charAt(pi) != '}') {
                                    pi++;
                                }
                                if (pi >= p.length()) {
                                    continue;
                                } else if (p.charAt(pi) == ',') {
                                    pi++;
                                } else if (p.charAt(pi) == '}') {
                                    return false;
                                }
                            }
                            int pi6 = pi + 1;
                            c3 = p.charAt(pi);
                            pi = pi6;
                        }
                    } else if (c != str.charAt(si)) {
                        return false;
                    } else {
                        pi = pi3;
                    }
                } else if (si >= str.length()) {
                    return false;
                } else {
                    pi = pi3;
                }
                si2 = si + 1;
                pi2 = pi;
            } else {
                while (pi3 < p.length() && p.charAt(pi3) == '*' && p.charAt(pi3) != '/') {
                    pi3++;
                }
                if (pi3 == p.length()) {
                    return true;
                }
                if (!(p.charAt(pi3) == '?' || p.charAt(pi3) == '[' || p.charAt(pi3) == '{')) {
                    while (si < str.length() && p.charAt(pi3) != str.charAt(si)) {
                        si++;
                    }
                }
                while (si < str.length()) {
                    if (matches(str.substring(si), p.substring(pi3))) {
                        return true;
                    }
                    si++;
                }
                return false;
            }
        }
        if (si == str.length()) {
            z = true;
        }
        return z;
    }
}
