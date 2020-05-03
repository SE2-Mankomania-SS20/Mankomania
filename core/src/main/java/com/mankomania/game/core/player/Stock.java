package com.mankomania.game.core.player;

public enum Stock {

    BRUCHSTAHLAG {
        @Override
        public String getName() {
            return "Bruchstahl-AG";
        }
    },
    KURZSCHLUSSAG {
        @Override
        public String getName() {
            return "Kurzschluss-Versorungs-AG";
        }
    },
    TROCKENOEL {
        @Override
        public String getName() {
            return "Trockenöl-AG";
        }
    };

    public abstract String getName();

}
