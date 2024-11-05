package fr.baptistemasoud.vanillaframework.example;

import fr.baptistemasoud.vanillaframework.annotation.ScannedComponent;

@ScannedComponent
public class TestComponent {
    public TestComponent(ElectricPowerSwitch client) {
        client.press();
        client.press();
    }
}
