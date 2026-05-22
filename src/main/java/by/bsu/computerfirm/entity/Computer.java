package by.bsu.computerfirm.entity;

import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.prototype.Prototype;

public class Computer implements Prototype<Computer> {

    private String model;
    private int productionYear;
    private CompositeComponent rootComponent;

    public Computer(String model, int productionYear, CompositeComponent rootComponent) {
        this.model = model;
        this.productionYear = productionYear;
        this.rootComponent = rootComponent;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public CompositeComponent getRootComponent() {
        return rootComponent;
    }

    public void setRootComponent(CompositeComponent rootComponent) {
        this.rootComponent = rootComponent;
    }

    @Override
    public Computer copy() {
        CompositeComponent rootCopy = rootComponent == null ? null : rootComponent.copy();
        return new Computer(model, productionYear, rootCopy);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Computer that = (Computer) other;
        if (productionYear != that.productionYear) {
            return false;
        }
        if (model == null ? that.model != null : !model.equals(that.model)) {
            return false;
        }
        if (rootComponent == null) {
            return that.rootComponent == null;
        }
        return rootComponent.equals(that.rootComponent);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (model == null ? 0 : model.hashCode());
        result = 31 * result + productionYear;
        result = 31 * result + (rootComponent == null ? 0 : rootComponent.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Computer{model='").append(model).append('\'');
        builder.append(", productionYear=").append(productionYear);
        builder.append(", rootComponent=").append(rootComponent);
        builder.append('}');
        return builder.toString();
    }
}
