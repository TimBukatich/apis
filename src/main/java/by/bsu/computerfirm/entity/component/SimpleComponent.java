package by.bsu.computerfirm.entity.component;

import by.bsu.computerfirm.entity.ComponentType;

public class SimpleComponent extends ComputerComponent {

    private String manufacturer;

    public SimpleComponent(String name, ComponentType type, double price, String manufacturer) {
        super(name, type, price);
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public SimpleComponent copy() {
        return new SimpleComponent(getName(), getType(), getPrice(), manufacturer);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        SimpleComponent that = (SimpleComponent) other;
        if (manufacturer == null) {
            return that.manufacturer == null;
        }
        return manufacturer.equals(that.manufacturer);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (manufacturer == null ? 0 : manufacturer.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleComponent{name='").append(getName()).append('\'');
        builder.append(", type=").append(getType());
        builder.append(", price=").append(getPrice());
        builder.append(", manufacturer='").append(manufacturer).append('\'');
        builder.append('}');
        return builder.toString();
    }
}
