package fr.baptistemasoud.vanillaframework.example;


import fr.baptistemasoud.vanillaframework.annotation.ScannedComponent;

@ScannedComponent
public class ElectricPowerSwitch {
    private final LightBulb client;
    private boolean on;

    public ElectricPowerSwitch(LightBulb client) {
        this.client = client;
        this.on = false;
    }

    public void press() {
        if (on) {
            client.turnOff();
            on = false;
        } else {
            client.turnOn();
            on = true;
        }
    }
}

