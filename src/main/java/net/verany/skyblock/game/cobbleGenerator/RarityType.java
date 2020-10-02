package net.verany.skyblock.game.cobbleGenerator;

public enum RarityType {

    LEGENDARY(97, 100),
    EXCLUSIVE(88, 97),
    RARE(65, 88),
    NORMAL(0, 65);

    private final int percentageFrom;
    private final int percentageTo;

    RarityType(int percentageFrom, int percentageTo) {
        this.percentageFrom = percentageFrom;
        this.percentageTo = percentageTo;
    }

    public int getPercentageFrom() {
        return percentageFrom;
    }

    public int getPercentageTo() {
        return percentageTo;
    }

    public static RarityType getRarityTypeByPercentage(int percentage) {
        for (RarityType value : values())
            if (percentage >= value.getPercentageFrom() && percentage <= value.getPercentageTo())
                return value;
        return null;
    }

}
