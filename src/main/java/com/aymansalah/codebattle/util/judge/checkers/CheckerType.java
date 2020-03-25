package com.aymansalah.codebattle.util.judge.checkers;

public enum CheckerType {
     LCMP("LINE_COMPARE"),
     WCMP("TOKENS_COMPARE");

     private final String label;

     CheckerType(String label) {
          this.label = label;
     }

     public String getLabel() {
          return this.label;
     }

}
