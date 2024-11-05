package fr.baptistemasoud.vanillaframework.example;

import fr.baptistemasoud.vanillaframework.annotation.ScannedComponent;

@ScannedComponent
public class LightBulb {
    public void turnOff() {
        System.out.println("LightBulb: Bulb turned off...");
    }

    public void turnOn() {
        System.out.println("LightBulb: Bulb turned on...");
    }
}
