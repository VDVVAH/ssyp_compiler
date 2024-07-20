package Parsing;

import java.util.ArrayList;
import java.util.List;

record StapleTokens(List<String> tokens, boolean negative, String varName, int begin) {
}

public class Preparser {
    private final List<String> tokens;
    private final String[] keywords = {"=", "+", "-", "*", "/", "[", "]", "(", ")", "{", "}", "."};
    protected boolean firstOperand = true;
    protected int last_contains_index = 0;

    public Preparser(List<String> tokens) {
        this.tokens = tokens;
    }

    List<String> share(List<String> elms) {
        for (String keyword : keywords) {
            for (int k = 0; k < elms.size(); k++) {
                while (elms.get(k).contains(keyword) && !elms.get(k).equals(keyword)) {
                    int index = elms.get(k).indexOf(keyword);
                    String e = elms.get(k);
                    elms.remove(k);
                    elms.add(k, e.substring(index + 1));
                    elms.add(k, keyword);
                    elms.add(k, e.substring(0, index));
                }
            }
        }
        return elms;
    }

    StapleTokens getStaplesBlock(List<String> tokens, int from) {
        tokens = tokens.subList(from + 1, tokens.size());
        return new StapleTokens(getBlock("(", ")", tokens), tokens.get(from - 1).equals("-"), tokens.get(from + 1), from);
    }

    List<String> getBlock(String begin, String end, List<String> tokens) {
        int begin_count = 0, end_count = 0, begin_index = 0, end_index = 0;
        for (int k = 0; k < tokens.size(); k++) {
            if (tokens.get(k).equals(begin)) {
                begin_index = k;
                begin_count++;
            } else if (tokens.get(k).equals(end) && begin_count != end_count + 1) {
                end_count++;
            } else if (tokens.get(k).equals(end)) {
                end_index = k;
                break;
            }
        }
        return tokens.subList(begin_index + 1, end_index);
    }

    int nextContains(String str, List<String> tokens) {
        for (int k = last_contains_index; k < tokens.size(); k++) {
            if (tokens.get(k).equals(str)) {
                last_contains_index = k;
                return k;
            }
        }
        last_contains_index = 0;
        return -1;
    }

    List<String> arithmetic(StapleTokens stapleTokens) {
        List<String> result = arithmetic(stapleTokens.tokens());
        if (stapleTokens.negative()) {
            result.add("#");
            result.add("-");
            result.add(stapleTokens.varName());
            result.add("#0");
            result.add(stapleTokens.varName());
            result.add(";");
        }
        return result;
    }

    List<String> getVarOrLiteral(int index, List<String> tokens) {
        List<String> result = new ArrayList<>();
        if (tokens.get(index).equals("#")) {
            result.add("#");
            result.add(tokens.get(index + 1));
        } else {
            result.add(tokens.get(index));
        }
        return result;
    }

    List<String> arithmetic(List<String> tokens) {
        ArrayList<String> result = new ArrayList<>();
        List<String> arg1, arg2, varName;

        int index = nextContains("(", tokens);
        while (index != -1) {
            StapleTokens internal_result = getStaplesBlock(tokens, index + 1);
            List<String> arithmetic_internal_result = arithmetic(internal_result);
            result.addAll(arithmetic_internal_result);
            tokens.subList(internal_result.begin() - 1, internal_result.begin() + internal_result.tokens().size() + 1).clear();
            tokens.addAll(internal_result.begin() - 1, arithmetic_internal_result);
            last_contains_index -= internal_result.tokens().size() + 2 - arithmetic_internal_result.size();
            index = nextContains("(", tokens);
        }

        if (tokens.getFirst().equals("-")) {
            varName = getVarOrLiteral(1, tokens);
            result.add("#");
            result.add("-");
            result.addAll(varName);
            result.add("#0");
            result.addAll(varName);
            result.add(";");
        } else {
            varName = getVarOrLiteral(0, tokens);
        }

        index = nextContains("*", tokens);
        while (index != -1) {
            arg1 = getVarOrLiteral(index - 1, tokens);
            arg2 = getVarOrLiteral(index + 1, tokens);
            result.add("#");
            result.add("*");
            result.addAll(arg1);
            result.addAll(arg1);
            result.addAll(arg2);
            result.add(";");
            tokens.subList(index, index + 2).clear();
            last_contains_index -= 2;
            index = nextContains("*", tokens);
        }

        index = nextContains("/", tokens);
        while (index != -1) {
            arg1 = getVarOrLiteral(index - 1, tokens);
            arg2 = getVarOrLiteral(index + 1, tokens);
            result.add("#");
            result.add("/");
            result.addAll(arg1);
            result.addAll(arg1);
            result.addAll(arg2);
            result.add(";");
            tokens.subList(index, index + 2).clear();
            last_contains_index -= 2;
            index = nextContains("/", tokens);
        }

        for (int k = 0; k < tokens.size(); k++) {
            if (tokens.get(k).equals("+")) {
                arg2 = getVarOrLiteral(k + 1, tokens);
                result.add("#");
                result.add("+");
                result.addAll(varName);
                result.addAll(varName);
                result.addAll(arg2);
                result.add(";");
                tokens.subList(k, k + 2).clear();
                k--;
            } else if (tokens.get(k).equals("-")) {
                arg2 = getVarOrLiteral(k + 1, tokens);
                result.add("#");
                result.add("-");
                result.addAll(varName);
                result.addAll(varName);
                result.addAll(arg2);
                result.add(";");
                tokens.subList(k, k + 2).clear();
                k--;
            }
        }
        return result;
    }

    List<String> substitution(int begin, int end) {
        List<String> tokens = share(this.tokens.subList(begin, end));
        String instruct_name = tokens.getFirst();
        List<String> ready_instruct = new ArrayList<>();
        int begin_index = 1;
        for (int k = 1; k < tokens.size() - 1; k++) {
            if (tokens.get(k).equals(")") && tokens.get(k + 1).equals("(")) {
                ready_instruct.add(arithmetic(tokens.subList(begin_index, k + 1)).getFirst());
                begin_index = k + 2;
            }
        }
        ready_instruct.addFirst(instruct_name);
        return ready_instruct;
    }

    public List<String> preparse() {
        boolean InBody = false, InSugar = false;
        int begin = 0;
        for (int k = 0; k < tokens.size() - 1; k++) {
            String thisToken = tokens.get(k), nextToken = tokens.get(k + 1);
            if (thisToken.equals("#") && nextToken.equals("F_BODY_BEGIN")) {
                InBody = true;
            } else if (thisToken.equals("#") && nextToken.equals("F_BODY_END")) {
                InBody = false;
            } else if (InBody && thisToken.equals(";") && !nextToken.equals("#")) {
                begin = k + 1;
                InSugar = true;
            } else if (InBody && InSugar && nextToken.equals(";")) {
                tokens.subList(begin, k + 1).clear();
                tokens.addAll(begin, substitution(begin, k + 1));
                InSugar = false;
            }
        }
        return tokens;
    }
}
