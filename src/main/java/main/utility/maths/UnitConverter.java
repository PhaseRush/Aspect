package main.utility.maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnitConverter {
    private HashMap<String, HashMap<String, Double>> allConversionTypes = new HashMap<>();
    private HashMap<String, Double> distanceUnits = new HashMap<>();
    private HashMap<String, Double> weightUnits = new HashMap<>();
    private HashMap<String, Double> volumeUnits = new HashMap<>();
    private HashMap<String, Double> temperatureUnits = new HashMap<>();
    private HashMap<String, Double> energyUnits = new HashMap<>();
    private HashMap<String, Double> powerUnits = new HashMap<>();
    private HashMap<String, Double> forceUnits = new HashMap<>();
    private HashMap<String, Double> velocityUnits = new HashMap<>();
    private HashMap<String, Double> timeUnits = new HashMap<>();

    private final double kelvinOffset = -273.15;
    private final double rankineOffset = -458.67;

    public UnitConverter() {
        distanceUnits.put("mm", 1.0);
        distanceUnits.put("cm", 10.0);
        distanceUnits.put("m", 1000.0);
        distanceUnits.put("km", 1000000.0);
        distanceUnits.put("yd", 914.4);
        distanceUnits.put("ly", 9.461E18);
        distanceUnits.put("pc", 3.086E19);
        distanceUnits.put("in", 25.4);
        distanceUnits.put("ft", 304.8);
        distanceUnits.put("mi", 1609344.0);

        weightUnits.put("g", 1.0);
        weightUnits.put("kg", 1000.0);
        weightUnits.put("lb", 453.592);
        weightUnits.put("st", 6350.29);
        weightUnits.put("oz", 1.0);

        volumeUnits.put("ml", 1.0);
        volumeUnits.put("l", 1000.0);
        volumeUnits.put("floz", 29.5735);
        volumeUnits.put("pt", 473.176);
        volumeUnits.put("qt", 946.353);
        volumeUnits.put("gal", 3785.41);

        temperatureUnits.put("c", 1.0);
        temperatureUnits.put("f", 1.8);
        temperatureUnits.put("k", 1.0);
        temperatureUnits.put("r", 1.8);

        energyUnits.put("j", 1.0);
        energyUnits.put("btu", 1055.06);
        energyUnits.put("cal", 4184.0);
        energyUnits.put("kwh", 3.6E6);
        energyUnits.put("ev", 1.6022E-19);
        energyUnits.put("thm", 1.055e+8);
        energyUnits.put("ft-lb", 1.355818);
        energyUnits.put("erg", 10E-7);

        powerUnits.put("w", 1.0);
        powerUnits.put("hp", 745.7);
        powerUnits.put("Mach one Mustang", 249809.0);

        forceUnits.put("N", 1.0);
        forceUnits.put("plankforce", 1.210295E44);
        forceUnits.put("lbf", 4.449222);
        forceUnits.put("dyn", 10E-5);
        forceUnits.put("apples on earth", 1.5);

        velocityUnits.put("m/s", 1.0);
        velocityUnits.put("ft/s", 0.30479999024);
        velocityUnits.put("km/hr", 3.6);
        velocityUnits.put("mi/hr", 2.23694);
        velocityUnits.put("kn", 1.94384);
        velocityUnits.put("speed of sound", 343.0);

        timeUnits.put("ms", 1.0);
        timeUnits.put("s", 1000.0);
        timeUnits.put("min", 60000.0);
        timeUnits.put("hr", 3.6e+6);
        timeUnits.put("day", 8.64e+7);
        timeUnits.put("week", 6.048e+8);
        timeUnits.put("mo", 2.628e+9);
        timeUnits.put("yr", 3.154e+10);
        timeUnits.put("decade", 3.154e+11);
        timeUnits.put("century", 3.154e+12);


        allConversionTypes.put("Distance", distanceUnits);
        allConversionTypes.put("Weight/Mass", weightUnits);
        allConversionTypes.put("Volume", volumeUnits);
        allConversionTypes.put("Temperature", temperatureUnits);
        allConversionTypes.put("Energy", energyUnits);
        allConversionTypes.put("Power", powerUnits);
        allConversionTypes.put("Force", forceUnits);
        allConversionTypes.put("Speed/Velocity", velocityUnits);
        allConversionTypes.put("Time", timeUnits);
    }

    public double convertDistance(String from, String to, double value) {
        return value * distanceUnits.get(from) / distanceUnits.get(to);
    }

    public double convertWeight(String from, String to, double value) {
        return value * weightUnits.get(from) / weightUnits.get(to);
    }

    public double convertVolume(String from, String to, double value) {
        return value * volumeUnits.get(from) / volumeUnits.get(to);
    }
    public double convertTemperature(String from, String to, double value) {
        switch (from) {
            case "k":
                return convertTemperature("c", to, value - kelvinOffset);
            case "r":
                return convertTemperature("f", to, value - rankineOffset);
            case "c":  // k, f, r
                return fromC(to, value);
            case "f":
                return fromF(to, value);
        }

        return 0;
    }
    public double convertEnergy(String from, String to, double value) {
        return value * energyUnits.get(from) / energyUnits.get(to);
    }
    public double convertPower(String from, String to, double value) {
        return value * powerUnits.get(from) / powerUnits.get(to);
    }
    public double convertForce(String from, String to, double value) {
        return value * forceUnits.get(from) / forceUnits.get(to);
    }
    public double convertVelocity(String from, String to, double value) {
        return value * velocityUnits.get(from) / velocityUnits.get(to);
    }
    public double convertTime(String from, String to, double value) {
        return value * timeUnits.get(from) / timeUnits.get(to);
    }


    private double fromC(String to, double value) { //to k, f, r
        switch (to) {
            case "k":
                return value + kelvinOffset;
            case "f":
                return value * 1.8 + 32;
            default:
                return (value - kelvinOffset) * 1.8;
        }
    }

    private double fromF(String to, double value) { //to c, k, r
        switch (to) {
            case "c":
                return (value - 32) / 1.8;
            case "k" :
                return (value - rankineOffset) / 1.8;
            default: // case "r"
                return value + rankineOffset;
        }
    }

    public List<String> allDistanceUnits() {
        return new ArrayList<>(distanceUnits.keySet());
    }
    public List<String> allWeightUnits() {
        return new ArrayList<>(weightUnits.keySet());
    }
    public List<String> allVolumeUnits() {
        return new ArrayList<>(volumeUnits.keySet());
    }
    public List<String> allTemperatureUnits() {return new ArrayList<>(temperatureUnits.keySet());}
    public List<String> allEnergyUnits() {return  new ArrayList<>(energyUnits.keySet());}
    public List<String> allPowerUnits() {return new ArrayList<>(powerUnits.keySet());}
    public List<String> allForceUnits() {return new ArrayList<>(forceUnits.keySet());}
    public List<String> allVelocityUnits(){return new ArrayList<>(velocityUnits.keySet());}

    public List<String> allTimeUnits() {
        return new ArrayList<>(timeUnits.keySet());
    }

    public HashMap<String, HashMap<String, Double>> getAllConversionTypes() {
        return allConversionTypes;
    }
}
