package by.bsu.computerfirm.entity.component;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.prototype.Prototype;

import java.util.Collections;
import java.util.List;

public abstract class ComputerComponent implements Prototype<ComputerComponent> {

    private String name;
    private ComponentType type;
    private double price;

    protected ComputerComponent(String name, ComponentType type, double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isComposite() {
        return false;
    }

    public List<ComputerComponent> getChildren() {
        return Collections.emptyList();
    }

    public void add(ComputerComponent component) {
        throw new UnsupportedOperationException(
                "Cannot add a child to a leaf component: " + getClass().getSimpleName());
    }

    public void remove(ComputerComponent component) {
        throw new UnsupportedOperationException(
                "Cannot remove a child from a leaf component: " + getClass().getSimpleName());
    }

    @Override
    public abstract ComputerComponent copy();

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ComputerComponent that = (ComputerComponent) other;
        if (Double.compare(that.price, price) != 0) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        if (name == null) {
            return that.name == null;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        result = 31 * result + (type == null ? 0 : type.hashCode());
        long priceBits = Double.doubleToLongBits(price);
        result = 31 * result + (int) (priceBits ^ (priceBits >>> 32));
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("{name='").append(name).append('\'');
        builder.append(", type=").append(type);
        builder.append(", price=").append(price);
        builder.append('}');
        return builder.toString();
    }
}
