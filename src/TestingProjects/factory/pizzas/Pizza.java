package TestingProjects.factory.pizzas;

import java.util.ArrayList;

abstract public class Pizza {
	String name;
	String dough;
	String sauce;
	ArrayList toppings = new ArrayList();

	public String getName() {
		return name;
	}

	public void prepare() {
		System.out.println("Preparing " + name);
	}

	public void bake() {
		System.out.println("Baking " + name);
	}

	public void cut() {
		System.out.println("Cutting " + name);
	}

	public void box() {
		System.out.println("Boxing " + name);
	}

	public String toString() {
		// code to display pizza name and ingredients
		StringBuilder display = new StringBuilder();
		display.append("---- ").append(name).append(" ----\n");
		display.append(dough).append("\n");
		display.append(sauce).append("\n");
		for (Object topping : toppings) {
			display.append(topping).append("\n");
		}
		return display.toString();
	}
}

