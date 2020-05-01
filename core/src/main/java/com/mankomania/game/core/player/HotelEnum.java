package com.mankomania.game.core.player;

public enum HotelEnum {

    SCHLOSSDIETRICH {
        @Override
        public String getName() {
            return "Schloss Dietrich";
        }
    },
    HOTELGARNIE {
        @Override
        public String getName() {
            return "Hotel Garnie";
        }
    },
    HOTELSEHBLICK {
        @Override
        public String getName() {
            return "Hotel Sehblick";
        }
    },
    HOTELRUHESANFT {
        @Override
        public String getName() {
            return "Hotel Ruhe Sanft";
        }
    },
    HOTELWILLANICHT {
        @Override
        public String getName() {
            return "Hotel Willa Nicht";
        }
    },
    HOTELSANTAFU {
        @Override
        public String getName() {
            return "Hotel Santa Fu";
        }
    };

    public abstract String getName();
}
